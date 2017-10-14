package com.example.zes.playsongcircleprogresssdemo;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zes.playsongcircleprogresssdemo.view.CustomProgress;

public class MainActivity extends AppCompatActivity {
    private CustomProgress customProgress;
    private Button normalBtn, startBtn, finishBtn, changeBtn;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (msg.arg1 == 101) {
                    if (cbtNameTv.getEllipsize() != null) {
                        cbtNameTv.setEllipsize(TextUtils.TruncateAt.END);
                    } else {
                        cbtNameTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    }
                }
                customProgress.setProgress(msg.arg1);
            }
            super.handleMessage(msg);

        }
    };
    private TextView cbtNameTv;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customProgress = (CustomProgress) findViewById(R.id.custom_progress);
        customProgress.setmTotalProgress(100);
        customProgress.setStatus(CustomProgress.FINISH_STATUS);
        normalBtn = (Button) findViewById(R.id.btn_normal);
        startBtn = (Button) findViewById(R.id.btn_start);
        finishBtn = (Button) findViewById(R.id.btn_finish);
        cbtNameTv = (TextView) findViewById(R.id.tv_crbt_name);
        changeBtn = (Button) findViewById(R.id.btn_change);
        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                } else {
                    Toast.makeText(MainActivity.this, "请先开始播放", Toast.LENGTH_SHORT).show();
                    return;
                }
                customProgress.setText("1");
                customProgress.setStatus(CustomProgress.NORMAL_STATUS);
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgress.setStatus(CustomProgress.START_STATUS);
                cbtNameTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 1; i <= 101; i++) {
                                Thread.sleep(50);
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                message.arg1 = i;
                                handler.sendMessage(message);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                } else {
                    Toast.makeText(MainActivity.this, "请先开始播放", Toast.LENGTH_SHORT).show();
                    return;
                }
                customProgress.setStatus(CustomProgress.FINISH_STATUS);
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgress.setRectColor(Color.RED);
            }
        });
    }
}
