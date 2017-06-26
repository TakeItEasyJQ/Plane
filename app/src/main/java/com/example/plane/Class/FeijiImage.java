package com.example.plane.Class;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My Computer on 2017/6/23.
 */

public class FeijiImage implements GameImage {   //自己的飞机

    public static boolean meDead=false;
    private List<Bitmap> melist=new ArrayList<>();
    private List<Bitmap> meboom=new ArrayList<>();
    private Bitmap me1;
    private Bitmap me2;

    public  int x,y;
    public FeijiImage(Bitmap me1,Bitmap me2,List<Bitmap> meboom){
        this.me1=me1;
        this.me2=me2;
        melist.add(me1);
        melist.add(me2);
        this.meboom=meboom;
        x=(GameView.display_w-me1.getWidth())/2;
        y=GameView.display_h-me1.getHeight()-15;
    }
    private int index=0;
    private int progress=0;
    @Override
    public Bitmap getBitmap() {
        Bitmap bitmap=melist.get(index);
        if (melist.size()==2){
            index++;
            if (index==melist.size()){
                index=0;
            }
        }else if (melist.size()==4){
            if (progress==1){
                index++;
            }else {
                progress++;
            }
            if (index==3){
                GameView.gameImages.remove(this);
                melist.clear();
                GameView.pool.play(GameView.boom,1,1,2,0,1);
                progress=0;
                meDead=true;
            }

        }
        return bitmap;
    }

    public void getHit(ArrayList<GameImage> images){        //    自己受伤
        for(GameImage enemy:(List<GameImage>)GameView.gameImages.clone()){
            if (enemy instanceof EnemyFeiji){
                if (enemy.getX()<x&&
                        enemy.getX()+enemy.getBitmap().getWidth()>x&&
                        enemy.getY()<y&&
                        enemy.getY()+enemy.getBitmap().getHeight()>y){
                    melist=meboom;
                }
            }
        }
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