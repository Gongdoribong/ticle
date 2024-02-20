package org.tickle.ticle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class GachaRes extends AppCompatActivity {
    private LottieAnimationView animationView;
    private Button confirmBtn;
    private ImageView puzzle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gachares);
        animationView = findViewById(R.id.glow);
        confirmBtn = findViewById(R.id.confirmBtn);
        puzzle = findViewById(R.id.puzzle);

        Intent intent = getIntent();
        int result = intent.getIntExtra("gachaNum", 0);

        int resID = getResources().getIdentifier("ticle_slice"+(result+1), "drawable", getPackageName());
        puzzle.setImageResource(resID);
        Log.d("gacha result", "gacha_result : "+(result+1));
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Page2.class);
                startActivity(intent);
            }
        });
    }

}
