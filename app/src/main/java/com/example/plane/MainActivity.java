package com.example.plane;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.example.plane.Class.GameView;

public class MainActivity extends AppCompatActivity {
    private  GameView  view;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=new GameView(this);
        setContentView(view);




        alert=new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("确定要退出？");
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                view.start();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== android.view.KeyEvent.KEYCODE_BACK){
            view.stop();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.stop();
    }

    @Override
    protected void onResume() {
        if (GameView.Firsttime){
            super.onResume();
            GameView.Firsttime=false;
        }else {
            view.start();
        }

    }
}
