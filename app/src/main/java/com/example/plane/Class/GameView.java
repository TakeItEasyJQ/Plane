package com.example.plane.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    public static Bitmap erjihuancun;

    public static int display_w;
    public static int display_h;

    public static Paint p=new Paint();
    private Paint score=new Paint();
    public static Canvas c;

    public  static boolean Dead=false;
    private SurfaceHolder holeder;
    private boolean state=false;

    public static ArrayList<GameImage> gameImages=new ArrayList<>(); //先加入背景照片
    public static ArrayList<Missile> missiles=new ArrayList<>();
    public static List<Bitmap> explores=new ArrayList<>();

    public static int great=0;

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
    }

    private void init(){
        erjihuancun=Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);

        gameImages.add(new Background(bg));              //意思就是每run一次就会遍历gameImage中所有的图片并画一次；
        gameImages.add(new FeijiImage(me1,me2));         //所以每个实现GameImage接口的类都要返回一个图片，而该类的构造方法可以
        gameImages.add(new EnemyFeiji(enemy,explores));           //传入多个自己的图片，每次返回一张即可,在此添加的先后顺序影响到触碰时的这档问题

    }
    @Override
    public void run() {     //绘画的中心方法
        int num=0;

        try {
            int h=160;
            while (state){
                if (selectedfeiji!=null){
                    if (h==160){
                        missiles.add(new Missile(selectedfeiji,missile));
                        h=0;
                    }
                    h+=20;
                }else {
                    h=160;
                }

                Canvas canvas= holeder.lockCanvas();
                c=new Canvas(erjihuancun);
                for (GameImage image:(List<GameImage> )gameImages.clone()){                       //里面有多少图形就画多少图形
                    if (image instanceof EnemyFeiji){
                        ((EnemyFeiji) image).getHit(missiles);                     //击中敌机
                        Dead=true;
                    }
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
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        display_w=width;
        display_h=height;
        init();
        this.holeder=holder;
        state=true;
        Thread t= new Thread(this);
        t.start();
//        try{
//            Thread.sleep(1000);
//            t=null;
//        }catch (InterruptedException e  ){
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
}


//    private interface GameImage{
//        Bitmap getBitmap();
//        int getX();
//        int getY();
//    }

//    private class FeijiImage implements GameImage {   //自己的飞机
//
//
//        private List<Bitmap> melist=new ArrayList<>();
//        private Bitmap me1;
//        private Bitmap me2;
//        public  int x,y;
//        private FeijiImage(Bitmap me1,Bitmap me2){
//            this.me1=me1;
//            this.me2=me2;
//            melist.add(me1);
//            melist.add(me2);
//            x=(display_w-me1.getWidth())/2;
//            y=display_h-me1.getHeight()-15;
//        }
//        private int index;
//        @Override
//        public Bitmap getBitmap() {
//            Bitmap bitmap=melist.get(index);
//            index++;
//            if (index==melist.size()){
//                index=0;
//            }
//            return bitmap;
//        }
//
//        @Override
//        public int getX() {
//            return x;
//        }
//
//        @Override
//        public int getY() {
//            return y;
//        }
//
//    }
//
//    private class EnemyFeiji implements GameImage{     //敌人飞机
//        private int x,y;
//        private Bitmap enemy;
//        Random r=new Random();
//
//        public EnemyFeiji(Bitmap enemy){
//            this.enemy=enemy;
//            int a=r.nextInt(display_w/enemy.getWidth());
//            this.x=a*enemy.getWidth();
//            Log.d(TAG, "EnemyFeiji: "+a);
//        }
//        @Override
//        public Bitmap getBitmap() {
//            y+=10;
//            if (y-enemy.getHeight()>=display_h){
//
//                gameImages.remove(this);
//                gameImages.add(new EnemyFeiji(enemy));
//            }
//            return enemy;
//        }
//
//        @Override
//        public int getX() {
//            return x;
//        }
//
//        @Override
//        public int getY() {
//            return y-enemy.getHeight();
//        }
//    }
//
//    private class Background implements GameImage{   //背景
//
//
//        private int height=0;
////        private int movoTo=display_h/200;
//        private Bitmap newBg=null;
//        private Bitmap bg;
//
//        private Background(Bitmap bg){
//             this.bg=bg;
//            newBg=Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
//         }
//        public Bitmap getBitmap(){
//            Paint paint=new Paint();
//            if (height>=display_h){
//                height=0;
//            }
//
//                Canvas canvas=new Canvas(newBg);
//                canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),//第一个new Rect为截取的区域 ;第二个new Rect为缩放的区域(上下左右四个点 )
//                        new Rect(0,height,display_w,display_h+height),paint);//这个new Rect中的0,0往下移，下一个的也随之移动 |x不动，动Y就行
//                //背景往下走 所以还有个照片在它的负位置
//                canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),
//                        new Rect(0,-display_h+height,display_w,height),paint);
//                height+=5;
//
//            return newBg;
//        }
//
//        @Override
//        public int getX() {
//            return 0;
//        }
//        @Override
//        public int getY() {
//            return 0;
//        }
//    }


