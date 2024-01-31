package org.ligh.ticle;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Roulette extends AppCompatActivity {

    int[] colors = { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
    String[] colorName = {"빨간색", "파란색", "노란색", "초록색"};
    private LuckyWheel luckyWheel;
    List<WheelItem> wheelItems;
    String result;
    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    private ImageButton navBtn2;
    boolean is500 = false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private TextView showPoint;
    int point;
    int cnt=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RouletteHelper rouletteHelper = new RouletteHelper();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roulette);

        Intent intent = getIntent();
        int rNum = intent.getIntExtra("rouletteNum", 0);
        int payPoint = intent.getIntExtra("payPoint", 0);
        if(rNum == 100) {
            is500 = true;
        }

        //Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //저장해둔 값 불러오기. 없으면 defValue로 설정
        point = pref.getInt("point", 1);

        showPoint = findViewById(R.id.pointNum);
        showPoint.setText(String.valueOf(point));

        //화면 이동(룰렛 선택)
        navBtn2 = findViewById(R.id.navBtn2);

        navBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = 0;
                Intent intent = new Intent(getApplicationContext(), Page1.class);
                startActivity(intent);
            }
        });
        if(!is500) {
            arrayList = new ArrayList<>();
            for(int i = 0; i < rNum; i++) {
                arrayList.add(colorName[i]);
            }
        } else {
            arrayList = new ArrayList<>();
            arrayList.add("검은색 (단일)");
        }


        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner = (Spinner)findViewById(R.id.chooseColor);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rouletteHelper.setTarget(Integer.toString(position+1));
                rouletteHelper.setSelected(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rouletteHelper.setSelected(false);
            }
        });

        //룰렛판 변수
        luckyWheel = findViewById(R.id.lwv);

        //룰렛판 데이터 생성
        generateWheelItems(rNum);
        System.out.println("함수 호출 후");
        //룰렛판 타겟 정해지면 이벤트 발생
        luckyWheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {

                //아이템 변수에 담기
                WheelItem wheelItem = wheelItems.get(Integer.parseInt(result)-1);
                //메시지
                if(rouletteHelper.getIsSuccess()) {
                    Toast.makeText(Roulette.this, "성공!", Toast.LENGTH_SHORT).show();
                    editor.putInt("point", point+(payPoint*2)).apply();
                    point = pref.getInt("point", 1);
                    showPoint.setText(String.valueOf(point));
                } else {
                    Toast.makeText(Roulette.this, "실패!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button goGacha = findViewById(R.id.goGacha);
        goGacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rouletteHelper.getSelected() && (point-payPoint)>=0) {
                    editor.putInt("point", point - payPoint).apply();
                    point = pref.getInt("point", 1);
                    showPoint.setText(String.valueOf(point));
                    Random random = new Random();
                    result = String.valueOf((random.nextInt(1000)%(rNum))+1);
                    System.out.println("result : " + result);
                    rouletteHelper.setIsSuccess(result);
                    luckyWheel.rotateWheelTo(Integer.parseInt(result));

                } else if ((point-payPoint)<0 && cnt!=5) {
                    AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Roulette.this);
                    myAlertBuilder.setTitle("돌아가!");
                    myAlertBuilder.setMessage("룰렛을 돌릴 포인트가 부족해요.");
                    myAlertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    myAlertBuilder.show();
                    cnt ++;

                } else if(cnt == 5 && payPoint == 1) {
                    AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Roulette.this);
                    myAlertBuilder.setTitle("돌아가!");
                    myAlertBuilder.setMessage("룰렛을 돌릴 포인트가 부족해요.\n제가 좀 나눠 드릴까요?");
                    myAlertBuilder.setPositiveButton("아니", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    myAlertBuilder.setNeutralButton("고마워!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putInt("point", point + 1).apply();
                            point = pref.getInt("point", 1);
                            showPoint.setText(String.valueOf(point));
                        }
                    });
                    myAlertBuilder.show();
                    cnt = 0;
                }

            }
        });



    }
    private void generateWheelItems(int rNum) {
        wheelItems = new ArrayList<>();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable d = getResources().getDrawable(R.drawable.ic_money,null);
        Bitmap bitmap = drawableToBitmap(d);
        if(is500) {
            for(int i = 0; i < rNum-1; i++) {
                wheelItems.add(new WheelItem(Color.WHITE, bitmap));
            }
            wheelItems.add(new WheelItem(Color.BLACK, bitmap));
        } else {
            for (int i = 0; i < rNum; i++) {
                wheelItems.add(new WheelItem(colors[i], bitmap));
            }
        }
        luckyWheel.addWheelItems(wheelItems);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.commit();
    }
}