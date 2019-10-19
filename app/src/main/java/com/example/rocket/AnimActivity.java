package com.example.rocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.rocket.widget.HexagonAnimView;

public class AnimActivity extends AppCompatActivity {


    private HexagonAnimView mHexagonAnimView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mHexagonAnimView = findViewById(R.id.hexagon_anim_view);
        findViewById(R.id.start_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHexagonAnimView.startRadioAnim();
            }
        });
    }
}
