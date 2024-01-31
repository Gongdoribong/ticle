package org.ligh.ticle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Page2 extends AppCompatActivity {
    private Button gachaBtn;
    private ImageButton navBtn;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private TextView showPoint;
    int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        gachaBtn = findViewById(R.id.gachaBtn);
        navBtn = findViewById(R.id.navBtn);
        //Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //저장해둔 값 불러오기. 없으면 defValue로 설정
        point = pref.getInt("point", 1);

        showPoint = findViewById(R.id.pointNum);
        showPoint.setText(String.valueOf(point));

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Page1.class);
                startActivity(intent);
            }
        });
    }
}
