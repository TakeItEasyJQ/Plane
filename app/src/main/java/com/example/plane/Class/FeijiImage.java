package com.example.plane.Class;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My Computer on 2017/6/23.
 */

public class FeijiImage implements GameImage {   //自己的飞机


    private List<Bitmap> melist=new ArrayList<>();
    private List<Bitmap> meboom=new ArrayList<>();


    private Bitmap me1;
    private Bitmap me2;

    private Bitmap meb1;
    private Bitmap meb2;
    private Bitmap meb3;
    private Bitmap meb4;

    public  int x,y;
    public FeijiImage(Bitmap me1,Bitmap me2){
        this.me1=me1;
        this.me2=me2;
        melist.add(me1);
        melist.add(me2);
        meboom.add(meb1);
        meboom.add(meb2);
        meboom.add(meb3);
        meboom.add(meb4);

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
                index=0;
                GameView.gameImages.remove(this);
                GameView.pool.play(GameView.boom,1,1,2,0,1);
                progress=0;
            }

        }
        return bitmap;
    }

//    public void getHit(ArrayList<GameImage> images){
//        for(GameImage enemy:images){
//            if (enemy instanceof EnemyFeiji){
//                if (enemy.getX()<x&&
//                        enemy.getX()+enemy.getBitmap().getWidth()>x&&
//                        enemy.getY()<y&&
//                        enemy.getY()+enemy.getBitmap().getHeight()>y){
//                    melist=meboom;
//                }
//            }
//        }
//    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

}