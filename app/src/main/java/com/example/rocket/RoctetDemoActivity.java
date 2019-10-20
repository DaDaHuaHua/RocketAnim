package com.example.rocket;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.rocket.widget.RocketAnimLayout;
import com.example.rocket.widget.RocketTailView;

public class RoctetDemoActivity extends AppCompatActivity implements View.OnClickListener{

    private RocketTailView mRocketTailView;
    private RocketAnimLayout mRocketAnimLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roctet_demo);
        findViewById(R.id.start_tail_tv).setOnClickListener(this);
        findViewById(R.id.rocket_start_tv).setOnClickListener(this);
        mRocketTailView = findViewById(R.id.tail_view);
        mRocketAnimLayout = findViewById(R.id.rocket_anim_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_tail_tv:
                mRocketTailView.start();
                break;
            case R.id.rocket_start_tv:
                mRocketAnimLayout.startAnim();
                break;

        }
    }
}
