package com.example.plane.Class;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.plane.R;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by My Computer on 2017/6/20.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class GameView extends SurfaceView implements SurfaceHolder.Callback ,Runnable,View.OnTouchListener {

    private static final String TAG = "GameView";
    
    public static Bitmap me1;
    public static Bitmap me2;
    public static Bitmap missile;
    public static Bitmap enemy;
    public static Bitmap bg;
    public static Bitmap explore1;
    public static Bitmap explore2;
    public static Bitmap explore3;
    public static Bitmap explore4;
    private Bitmap meb1;
    private Bitmap meb2;
    private Bitmap meb3;
    private Bitmap meb4;

    public static Bitmap erjihuancun;

    public static int display_w;
    public static int display_h;

    public static Paint p=new Paint();
    private Paint score=new Paint();
    public static Canvas c;

    public static boolean Firsttime=true;
    public  static boolean Dead=false;
    private SurfaceHolder holeder;
    private boolean state=false;
    public static int great=0;
    public  Thread thread=null;
    private  boolean stopStatus=false;


    public static ArrayList<GameImage> gameImages=new ArrayList<>(); //先加入背景照片
    public static ArrayList<Missile> missiles=new ArrayList<>();
    public static List<Bitmap> explores=new ArrayList<>();
    public static List<Bitmap> meboom=new ArrayList<>();

    public static int boom;
    public static int boom2;
    public static int boom3;
    public static int bullet;
    public static int game_music;
    public static SoundPool.Builder builder=new SoundPool.Builder();
    public static SoundPool pool=builder.build();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);
        me1=BitmapFactory.decodeResource(getResources(), R.drawable.img_me1);
        me2=BitmapFactory.decodeResource(getResources(), R.drawable.img_me2);
        missile= BitmapFactory.decodeResource(getResources(), R.drawable.img_missile);
        enemy= BitmapFactory.decodeResource(getResources(), R.drawable.img_enemy);
        bg= BitmapFactory.decodeResource(getResources(), R.drawable.img_background);
        explore1=BitmapFactory.decodeResource(getResources(), R.drawable.img_enemydown1);
        explore2=BitmapFactory.decodeResource(getResources(), R.drawable.img_enemydown2);
        explore3=BitmapFactory.decodeResource(getResources(), R.drawable.img_enemydonw3);
        explore4=BitmapFactory.decodeResource(getResources(), R.drawable.img_enemydonw4);
        explores.add(explore1);
        explores.add(explore2);
        explores.add(explore3);
        explores.add(explore4);

        meb1=BitmapFactory.decodeResource(getResources(),R.drawable.img_meboom1);
        meb2=BitmapFactory.decodeResource(getResources(),R.drawable.img_meboom2);
        meb3=BitmapFactory.decodeResource(getResources(),R.drawable.img_meboom3);
        meb4=BitmapFactory.decodeResource(getResources(),R.drawable.img_meboom4);

        meboom.add(meb1);
        meboom.add(meb2);
        meboom.add(meb3);
        meboom.add(meb4);

        builder.setMaxStreams(10);
        bullet=pool.load(getContext(),R.raw.bullet,1);
        boom=pool.load(getContext(),R.raw.boom,2);
        boom2=pool.load(getContext(),R.raw.boom2,2);
        boom3=pool.load(getContext(),R.raw.boom3,2);
        game_music=pool.load(getContext(),R.raw.game_music,3);

//        pool.play(game_music,1,1,2,0,1);
    }

    private void init(){
        erjihuancun=Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);

        gameImages.add(new Background(bg));              //意思就是每run一次就会遍历gameImage中所有的图片并画一次；
        gameImages.add(new FeijiImage(me1,me2,meboom));         //所以每个实现GameImage接口的类都要返回一个图片，而该类的构造方法可以
        gameImages.add(new EnemyFeiji(enemy,explores));           //传入多个自己的图片，每次返回一张即可,在此添加的先后顺序影响到触碰时的这档问题

        //加载声音池
    }
    @Override
    public void run() {     //绘画的中心方法
        int num=0;

        try {
            int h=160;
            while (state){
                while(stopStatus){
                    try {
                       Thread.sleep(100000000);
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                }

                if (selectedfeiji!=null){
                    if (h==160){
                        missiles.add(new Missile(selectedfeiji,missile));
                        pool.play(bullet,1,1,1,0,1);  //id，左声道，右声道，优先级，循环，速度
                        h=0;
                    }
                    h+=20;
                }else {
                    h=160;
                }

                Canvas canvas= holeder.lockCanvas();
                c=new Canvas(erjihuancun);
                for (GameImage image:(List<GameImage> )gameImages.clone()){                       //里面有多少图形就画多少图形
                    if (image instanceof EnemyFeiji){                   //如果是敌人，调用敌人的受攻击方法
                        ((EnemyFeiji) image).getHit(missiles);                     //击中敌机
                    }
//                    if (image instanceof FeijiImage){
//                        ((FeijiImage)image).getHit(gameImages);
//
//                    }
                    c.drawBitmap(image.getBitmap() ,image.getX(),image.getY(),p);

                }
                for (GameImage missile:(List<Missile>)missiles.clone()){
                    c.drawBitmap(missile.getBitmap(),missile.getX(),missile.getY(),p);

                }
                if (num==100){
                    gameImages.add(new EnemyFeiji(enemy,explores));
                    num=0;
                }else {
                    num+=5;
                }
                c.drawText("分数"+great,0,display_h-30,score);
                canvas.drawBitmap(erjihuancun,0,0,p);
                holeder.unlockCanvasAndPost(canvas);
                Thread.sleep(1);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        score.setColor(Color.BLACK);
        score.setTextSize(30);
        score.setDither(true);
        Log.d(TAG, "surfaceCreated: ");


        
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: ");
        display_w=width;
        display_h=height;
        init();
        this.holeder=holder;
        state=true;
        thread= new Thread(this);
        thread.start();
//        try{
//            Thread.sleep(1000);
//            t=null;
//        }catch (InterruptedException e  ){
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
        state=false;
    }


    private FeijiImage selectedfeiji;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            for (GameImage game:gameImages){
                if (game instanceof FeijiImage){
                    if (game.getX()<event.getX()&&event.getX()<game.getX()+me1.getWidth()&&
                            game.getY()<event.getY()&&event.getY()<game.getY()+me1.getHeight()){
                        selectedfeiji=(FeijiImage)game;

                    }else {
                        selectedfeiji=null;
                    }
                    break;
                }
            }
        }else if (event.getAction()==MotionEvent.ACTION_MOVE){
            if (selectedfeiji!=null){
                selectedfeiji.x=(int)event.getX()-me1.getWidth()/2;
                selectedfeiji.y=(int)event.getY()-me1.getHeight()/2;
            }


        }else if (event.getAction()==MotionEvent.ACTION_UP){
            selectedfeiji=null;
        }
        return true;
    }
    public  void stop(){  //按下暂停
        stopStatus=true;
    }
    public  void start(){
        stopStatus=false;
        thread.interrupt();//唤醒
    }


}


