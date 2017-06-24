package com.example.plane.Class;

import android.graphics.Bitmap;

/**
 * Created by My Computer on 2017/6/23.
 */

public class Missile implements GameImage{

    private Bitmap missile;
    private FeijiImage me;
    private int x,y;

    public Missile(FeijiImage me, Bitmap missile){
        this.me=me;
        this.missile=missile;
        x=me.getX()+GameView.me1.getWidth()/2;
        y=me.getY()-missile.getHeight();
    }
    @Override
    public Bitmap getBitmap() {
        y-=30;
        if (y<30){
            GameView.missiles.remove(this);
        }

        return missile;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
