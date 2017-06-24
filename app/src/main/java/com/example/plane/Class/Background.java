package com.example.plane.Class;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by My Computer on 2017/6/23.
 */

public class Background implements GameImage {   //背景

    private int height=0;
    //        private int movoTo=display_h/200;
    private Bitmap newBg=null;
    private  Bitmap bg;

    public Background(Bitmap bg){
        this.bg=bg;
        newBg=Bitmap.createBitmap(GameView.display_w,GameView.display_h, Bitmap.Config.ARGB_8888);
    }
    public Bitmap getBitmap(){
        Paint paint=new Paint();
        if (height>=GameView.display_h){
            height=0;
        }

        Canvas canvas=new Canvas(newBg);
        canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),//第一个new Rect为截取的区域 ;第二个new Rect为缩放的区域(上下左右四个点 )
                new Rect(0,height,GameView.display_w,GameView.display_h+height),paint);//这个new Rect中的0,0往下移，下一个的也随之移动 |x不动，动Y就行
        //背景往下走 所以还有个照片在它的负位置
        canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),
                new Rect(0,-GameView.display_h+height,GameView.display_w,height),paint);
        height+=5;

        return newBg;
    }

    @Override
    public int getX() {
        return 0;
    }
    @Override
    public int getY() {
        return 0;
    }
}

