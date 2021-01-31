/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author prabhasha
 */
public class Robot {
    
    int x;
    int y;
    String name;
    int id;
    int delay;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

   
    public Robot(int x, int y, String name, int id, int delay, int type) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.id = id;
        this.delay = delay;
        this.type = type;
    }
    
    public Robot(int x, int y, String name, int id, int delay) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.id = id;
        this.delay = delay;
        this.type = 0;
    }
    public Robot( String name, int id) {
        this.x = 0;
        this.y = 0;
        this.name = name;
        this.id = id;
         this.delay = 200;
         this.type = 0;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
