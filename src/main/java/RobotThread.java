
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author prabhasha
 */
public class RobotThread implements Runnable{

    Robot robot;
    JFXArena arena;
    public RobotThread(Robot robot , JFXArena arena){
        this.robot = robot;
        this.arena = arena;
    }
    @Override
    public void run() {
        while( arena.is_available_robot(robot.getId())){
        try {
            Thread.sleep(robot.getDelay());
        } catch (InterruptedException ex) {
            Logger.getLogger(RobotThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        Random random = new Random();
        int x = robot.getX();
        int y = robot.getY();
        
        ArrayList<Robot> robots = arena.getRobots();
        
        boolean loop_end = false;
        int set_x = x;
        int set_y = y;
        
        do{
            boolean is_av = false;
            boolean is_x  = random.nextBoolean();
            boolean is_increment = random.nextBoolean();
     
            set_x = x;
            set_y = y;
            
            if(is_x){
              int value = is_increment ? x + 1 : x - 1;

              if(value < 0 ){
                  set_x = 0;
              }else if( value > 8 ){
                  set_x = 8;
              }else{
                  set_x = value;
              }
            }else{
              int value = is_increment ? y + 1 : y - 1;

              if(value < 0 ){
                  set_y = 0;
              }else if( value > 8 ){
                  set_y = 8;
              }else{
                  set_y = value;
              }
            }
            
            for (Robot rb : robots) {
                if(rb.getX() == set_x && rb.getY() == set_y ){
                    is_av = true;
                    break;
                }
            }
            
            loop_end  = is_av;     
        }while(loop_end);
        
        robot.setX(set_x);
        robot.setY(set_y);
        
         if(set_x == 4 && set_y == 4){
             arena.setNot_gameover(false);
         }
        arena.setRobotPosition(robot.getX(), robot.getY(), robot.getId());
        }
        
    }
    
}
