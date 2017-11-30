package mask.streetmaster;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Ennemy {
    int type; // 0=tree, 1=cops, 2=gangster, 3=coins
    int strength;
    int speed;
    Bitmap image;
    Rect box;
    int x, y;
    boolean active;
    boolean fight_done;

    public Ennemy(int t, Bitmap i) {
        type= t;
        image = i;
        x = 0;
        y = 0-image.getHeight();
        box = new Rect(x, y, x+image.getWidth(), y+image.getHeight());
        active = false;
        if(type==2) {
            speed = 30;
            strength = (int)(Math.random()*100);
        }else if(type==1) {
            strength=0;
            speed = 60;
        }else if(type==0) {
            strength = 0;
            speed = 30;
        }else if(type==3) {
            strength=0;
            speed=30;
        }
        fight_done = false;
    }

    public void setActive(boolean b) {
        y = 0 - image.getHeight();
        this.active=b;
    }

    public void initX(int width) {
        this.x = (int) (Math.random()* (width-image.getWidth()));
    }

    public void updateBox() {
        this.box.top = this.y;
        this.box.left = this.x;
        this.box.right = this.x + image.getWidth();
        this.box.bottom = this.y + image.getHeight();
    }

    public void replaceSpawn(int width) {
        setActive(true);
        fight_done=false;
        x = (int) (Math.random()*(width - image.getWidth()));
        this.updateBox();
    }

    public void reset() {
        x = 0;
        y = 0-image.getWidth();
        active=false;
        updateBox();
        fight_done=false;
    }
}
