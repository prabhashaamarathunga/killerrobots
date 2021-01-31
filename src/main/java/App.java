
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    ArrayList<Robot> robots = new ArrayList<Robot>();
    //Creating ThreadPool with Unlimited Threads
    ExecutorService service = Executors.newCachedThreadPool();
    //Creating BlockingQueue
    private static BlockingQueue<Click> queue = new ArrayBlockingQueue<>(20);

    TextArea logger;
    int counter = 1;
    JFXArena arena;
    int score = 0;
    Label score_label;

    public static void main(String[] args) throws InterruptedException {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Killer Robots");
        arena = new JFXArena();
        arena.addListener((x, y)
                -> {

            try {
                //Adding click events to the BlockingQueue
                queue.put(new Click(x, y, System.currentTimeMillis()));
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

            Platform.runLater(() -> {
                logger.appendText("- - Player Fired - -\n");
            });

        });

        ToolBar toolbar = new ToolBar();

        score_label = new Label("Score: 0");

        toolbar.getItems().addAll(score_label);

        logger = new TextArea();
        logger.appendText("Welcome to the Robot Game\n");

        //-----------------Thread to Create Robot--------------------
        Thread robot_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {

                        if (checkavailable() && arena.isNot_gameover()) {
                            Robot new_robot = createRobot();
                            robots.add(new_robot);
                            counter++;
                            arena.setRobot(new_robot);

                            //Updating Logger
                            Platform.runLater(() -> {
                                logger.appendText("- - " + new_robot.getName() + " Robot Created - -\n");
                            });

                            service.execute(new RobotThread(new_robot, arena));
                        }
                    }
                };

                while (arena.isNot_gameover()) {
                    try {
                        //Creating Robots with a 2000milisecond delay 
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                    Platform.runLater(updater);
                }
            }

        });

        robot_thread.start();

        //----------Thread to take from BlockingQueue---------
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (arena.isNot_gameover()) {

                        Click click = queue.take();

                        Robot died = arena.removeRobot(click.getX(), click.getY());
                        if (died != null) {
                            double bonus = (100 * (System.currentTimeMillis() - click.getClicked_at()) / died.getDelay());

                            score = (int) (score + 10 + bonus);
                            System.out.println("clicked : " + bonus);
                            Platform.runLater(() -> {
                                score_label.setText("Score : " + score);
                            });

                            Platform.runLater(() -> {
                                logger.appendText("- - " + died.getName() + " Robot Destroyed - -\n");
                            });

                        } else {
                            Platform.runLater(() -> {
                                logger.appendText("- - Player Missed - -\n");
                            });

                        }
//Wait time when player fires(Click)
                        Thread.sleep(1000);
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        consumer.start();

        //----------Thread to get score----------
        Thread score_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    do {

                        Thread.sleep(1000);
                        score += 10;
                        Platform.runLater(() -> {
                            score_label.setText("Score : " + score);
                        });

                        //--------GameOver Alert----------------------
                        if (!arena.isNot_gameover()) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Game Over!");
                                alert.setHeaderText(" Congratulations! You Scored : " + score);
                                alert.showAndWait();
                                logger.appendText("- - Game Over - -\n");
                            });

                        }
                    } while (arena.isNot_gameover());

                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        score_thread.start();

        arena.setMinWidth(300.0);
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    //----------Creating Robot Logic-----------------
    public Robot createRobot() {
        Random random = new Random();
        int delay = random.nextInt(1500) + 500;
        int size = arena.getRobots().size();

        int x = 0;
        int y = 0;

        if (size > 3) {
            int[] arr = selectslot();
            x = arr[0];
            y = arr[1];
        } else if (size > 2) {
            x = 0;
            y = 8;
        } else if (size > 1) {
            x = 8;
            y = 8;
        } else if (size > 0) {
            x = 8;
            y = 0;
        } else {
            x = 0;
            y = 0;
        }
        return new Robot(x, y, "R" + counter, counter, delay, random.nextInt(3));
    }

    //Check Availability of four coners to generate new Robot
    public boolean checkavailable() {
        ArrayList<Robot> robots = arena.getRobots();

        boolean lt = false;
        boolean rt = false;
        boolean lb = false;
        boolean rb = false;

        for (Robot robot : robots) {
            if (robot.getX() == 0 && robot.getY() == 0) {
                lt = true;
            }

            if (robot.getX() == 8 && robot.getY() == 0) {
                rt = true;
            }

            if (robot.getX() == 0 && robot.getY() == 8) {
                lb = true;
            }

            if (robot.getX() == 8 && robot.getY() == 8) {
                rb = true;
            }
        }

        return !(lt && rt && lb && rb);
    }

    public int[] selectslot() {
        int[] arr = {};
        ArrayList<Robot> robots = arena.getRobots();
        boolean lt = false;
        boolean rt = false;
        boolean lb = false;
        boolean rb = false;

        for (Robot robot : robots) {
            if (robot.getX() == 0 && robot.getY() == 0) {
                lt = true;
            }

            if (robot.getX() == 8 && robot.getY() == 0) {
                rt = true;
            }

            if (robot.getX() == 0 && robot.getY() == 8) {
                lb = true;
            }

            if (robot.getX() == 8 && robot.getY() == 8) {
                rb = true;
            }
        }

        if (!lt) {
            return new int[]{0, 0};
        }

        if (!rt) {
            return new int[]{8, 0};
        }

        if (!lb) {
            return new int[]{0, 8};
        }

        if (!rb) {
            return new int[]{8, 8};
        }

        return new int[]{0, 0};
    }
}
