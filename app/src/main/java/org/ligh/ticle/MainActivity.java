package org.ligh.ticle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 일정 시간 후에 Fragment로 전환
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent 생성
                Intent intent = new Intent(MainActivity.this, Page1.class);
                startActivity(intent);
            }
        }, 2000); // 2초(2000 밀리초) 후에 실행
    }
}