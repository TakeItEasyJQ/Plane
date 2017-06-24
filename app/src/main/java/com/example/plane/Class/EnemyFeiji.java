package com.example.plane.Class;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by My Computer on 2017/6/23.
 */

public class EnemyFeiji implements GameImage {     //敌人飞机
    private int x,y;
    private Bitmap enemy;
    private Random r=new Random();
    private  List<Bitmap> diren=new ArrayList<>();
    private  List<Bitmap> explores=new ArrayList<>();
    private int index=0;
    private int progerss=0;
    public EnemyFeiji(Bitmap enemy,List<Bitmap> explores){
        this.enemy=enemy;
        this.explores=explores;
        int a=r.nextInt(GameView.display_w/enemy.getWidth());
        this.x=a*enemy.getWidth();
        this.y=y- enemy.getHeight();
        diren.add(enemy);

    }
    @Override
    public Bitmap getBitmap() {

        Bitmap enemy=diren.get(index);
        if (diren.size()==1){
            index=0;
        }else if (diren.size()==4){
            if (progerss==1){
                index++;
            }else {
                progerss++;
            }
            if (index == 3) {
                GameView.gameImages.remove(this);
                GameView.pool.play(GameView.boom,1,1,2,0,1);
                GameView.great+=10;
                progerss=0;
            }
        }
        y+=12;
        if (y-enemy.getHeight()>=GameView.display_h){
            GameView.gameImages.remove(this);
            GameView.gameImages.add(new EnemyFeiji(GameView.enemy,explores));
        }
        return enemy;
    }


   //受到攻击
    public void getHit(ArrayList<Missile> missiles){
        for (GameImage missile:(List<Missile>)GameView.missiles.clone()){

            if (missile.getX()>x &&
                    missile.getX()<x+enemy.getWidth()&&
                    missile.getY()>y &&
                    missile.getY()<y+enemy.getHeight()){
                    GameView.Dead=true;
                    diren=explores;
                    GameView.missiles.remove(missile);
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
