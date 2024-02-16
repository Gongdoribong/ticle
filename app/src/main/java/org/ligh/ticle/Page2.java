package org.ligh.ticle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Page2 extends AppCompatActivity {
    private Button gachaBtn;
    private ImageButton navBtn;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private TextView showPoint;
    int point;

    private ArrayList<TextView> tv_names;
    private ArrayList<ImageView> iv_names;
    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12;

    ArrayList<String> array = new ArrayList<>();
    private static final String TICLE_LIST_JSON = "ticle_list_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        //Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //저장해둔 값 불러오기. 없으면 defValue로 설정
        point = pref.getInt("point", 1);

        //티끌이 정보
        array = getStringArrayPref(TICLE_LIST_JSON);


        gachaBtn = findViewById(R.id.gachaBtn);
        navBtn = findViewById(R.id.navBtn);
        tv_names = new ArrayList<TextView>();
        for(int j = 0; j < 12; j++) {
            String tv_id = "textView" + (j+1);
            int resID = getResources().getIdentifier(tv_id, "id", getPackageName());
            tv_names.add(((TextView) findViewById(resID)));
            tv_names.get(j).setText(Collections.frequency(array, j+"")+"/10");
        }

        iv_names = new ArrayList<ImageView>();
        for(int j = 0; j < 12; j++) {
            String iv_id = "ticle" + (j+1);
            int resID = getResources().getIdentifier(iv_id, "id", getPackageName());
            iv_names.add(((ImageView) findViewById(resID)));
        }
        checkUnlock(array, iv_names, tv_names);

        showPoint = findViewById(R.id.pointNum);
        showPoint.setText(String.valueOf(point));

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Page1.class);
                startActivity(intent);
            }
        });

        gachaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //포인트 차감 필요
                //if(포인트 == 기준) {} else {경고}

                //랜덤 숫자 하나 뽑기 -> array에 추가 -> 조각 개수 카운팅 -> text update 및 해금
                Random random = new Random();
                String result = String.valueOf(random.nextInt(1000) % 12);
                array.add(result);

                Intent intent = new Intent(getApplicationContext(), GachaWait.class);
                intent.putExtra("gachaNum", Integer.valueOf(result));
                startActivity(intent);
            }
        });


    }

    private void checkUnlock(ArrayList<String> array, ArrayList<ImageView> iv_names, ArrayList<TextView> tv_names) {

        for(int i = 0; i < 12; i++) {
            if(Collections.frequency(array, i+"")>=10) {
                String imgloc = "ticle" + (i+1);
                int resID = getResources().getIdentifier(imgloc, "drawable", getPackageName());
                iv_names.get(i).setImageResource(resID);
                tv_names.get(i).setVisibility(View.INVISIBLE);
            }
        }


    }
    private void setStringArrayPref(String key, ArrayList<String> values) {

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
    }

    private ArrayList getStringArrayPref(String key) {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String json = pref.getString(key, null);
        ArrayList urls = new ArrayList();

        if(json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for(int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    @Override
    protected void onPause() {
        super.onPause();

        setStringArrayPref(TICLE_LIST_JSON, array);
    }
}
