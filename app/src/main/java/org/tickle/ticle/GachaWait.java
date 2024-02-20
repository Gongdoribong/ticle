package org.tickle.ticle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class GachaWait extends AppCompatActivity {
    private LottieAnimationView animationView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gachawait);
        animationView = findViewById(R.id.capsule);

        Intent intent = getIntent();
        int result = intent.getIntExtra("gachaNum", 0);
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GachaRes.class);
                intent.putExtra("gachaNum", Integer.valueOf(result));
                startActivity(intent);
            }
        });
    }

}
