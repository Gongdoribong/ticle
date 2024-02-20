package org.tickle.ticle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Page1 extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int point;

    private ImageButton buyBtn1, buyBtn5, buyBtn10, buyBtn500, adBtn, navBtn;
    private TextView showPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);

        //Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //저장해둔 값 불러오기. 없으면 defValue로 설정
        point = pref.getInt("point", 1);

        showPoint = findViewById(R.id.pointNum);
        showPoint.setText(String.valueOf(point));



        //화면 이동(Page2 : 티끌이 창고)
        navBtn = findViewById(R.id.navBtn);

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Page2.class);
                startActivity(intent);
            }
        });

        //구매 로직

        buyBtn1 = findViewById(R.id.buy1btn);
        buyBtn5 = findViewById(R.id.buy5btn);
        buyBtn10 = findViewById(R.id.buy10btn);
        buyBtn500 = findViewById(R.id.buy500btn);
        //adBtn = findViewById(R.id.adBtn);

        buyBtn1.setTag("product_id_1");
        buyBtn5.setTag("product_id_5");
        buyBtn10.setTag("product_id_10");
        buyBtn500.setTag("product_id_500");

        buyBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Roulette.class);
                intent.putExtra("rouletteNum", 2);
                intent.putExtra("payPoint", 1);
                startActivity(intent);
            }
        });
        buyBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Roulette.class);
                intent.putExtra("rouletteNum", 3);
                intent.putExtra("payPoint", 5);
                startActivity(intent);
            }
        });
        buyBtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Roulette.class);
                intent.putExtra("rouletteNum", 4);
                intent.putExtra("payPoint", 10);
                startActivity(intent);
            }
        });
        buyBtn500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Roulette.class);
                intent.putExtra("rouletteNum", 100);
                intent.putExtra("payPoint", 500);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.commit();
    }

    protected void onDestroy() {
        super.onDestroy();
        editor.commit();
    }
}

