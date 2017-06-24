package com.example.plane.Class;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My Computer on 2017/6/23.
 */

public class FeijiImage implements GameImage {   //自己的飞机


    private List<Bitmap> melist=new ArrayList<>();
    private Bitmap me1;
    private Bitmap me2;
    public  int x,y;
    public FeijiImage(Bitmap me1,Bitmap me2){
        this.me1=me1;
        this.me2=me2;
        melist.add(me1);
        melist.add(me2);
        x=(GameView.display_w-me1.getWidth())/2;
        y=GameView.display_h-me1.getHeight()-15;
    }
    private int index;
    @Override
    public Bitmap getBitmap() {
        Bitmap bitmap=melist.get(index);
        index++;
        if (index==melist.size()){
            index=0;
        }
        return bitmap;
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