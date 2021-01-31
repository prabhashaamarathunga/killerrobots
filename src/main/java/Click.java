/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author prabhasha
 */
public class Click {
    
    int x;
    int y;
    long clicked_at;

    public Click(int x, int y, long clicked_at) {
        this.x = x;
        this.y = y;
        this.clicked_at = clicked_at;
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

    public long getClicked_at() {
        return clicked_at;
    }

    public void setClicked_at(long clicked_at) {
        this.clicked_at = clicked_at;
    }
    

    

    
    
}
