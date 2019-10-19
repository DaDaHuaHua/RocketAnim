package com.example.rocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.rocket.widget.HexagonAnimView;

public class AnimActivity extends AppCompatActivity implements View.OnClickListener {


    private HexagonAnimView mHexagonAnimView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mHexagonAnimView = findViewById(R.id.hexagon_anim_view);
        findViewById(R.id.start_tv).setOnClickListener(this);
        findViewById(R.id.clear_tv).setOnClickListener(this);
        findViewById(R.id.pause_tv).setOnClickListener(this);
    }


    private boolean isAnimPaused;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_tv:
                mHexagonAnimView.startRadioAnim();
                break;
            case R.id.clear_tv:
                mHexagonAnimView.clearView();
                break;
            case R.id.pause_tv:
                if (!isAnimPaused) {
                    mHexagonAnimView.pauseScaleAnim();
                    ((TextView) v).setText("继续播放");
                } else {
                    mHexagonAnimView.resumeScaleAnim();
                    ((TextView) v).setText("暂停播放");
                }
                isAnimPaused = !isAnimPaused;
                break;
        }
    }
}
