package com.example.rocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.rocket.widget.HexagonAnimLayout;
import com.example.rocket.widget.HexagonAnimView;
import com.example.rocket.widget.ParticleView;

public class AnimDemoActivity extends AppCompatActivity implements View.OnClickListener {


    private HexagonAnimView mHexagonAnimView;
    private HexagonAnimLayout mHexagonAnimLayout;
    ParticleView mParticleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mHexagonAnimView = findViewById(R.id.hexagon_anim_view);
        mHexagonAnimLayout = findViewById(R.id.hexagon_anim_layout);
        mParticleView = findViewById(R.id.particle_view);
        findViewById(R.id.particle_in).setOnClickListener(this);
        findViewById(R.id.particle_out).setOnClickListener(this);
        findViewById(R.id.clear_tv).setOnClickListener(this);
        findViewById(R.id.pause_tv).setOnClickListener(this);
        findViewById(R.id.all_tv).setOnClickListener(this);
        findViewById(R.id.rocket_tv).setOnClickListener(this);
    }


    private boolean isAnimPaused;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.particle_in:
                mParticleView.startBackCenterAnim(true);
                break;
            case R.id.particle_out:
                mParticleView.startBackCenterAnim(false);
                break;
            case R.id.clear_tv:
                showView(mHexagonAnimView);
                mHexagonAnimView.clearView();
                break;
            case R.id.pause_tv:
                showView(mHexagonAnimView);
                if (!isAnimPaused) {
                    mHexagonAnimView.pauseScaleAnim();
                    ((TextView) v).setText("继续播放");
                } else {
                    mHexagonAnimView.resumeScaleAnim();
                    ((TextView) v).setText("暂停播放");
                }
                isAnimPaused = !isAnimPaused;
                break;
            case R.id.all_tv:
                showView(mHexagonAnimLayout);
                startWholeAnim();
                break;
            case R.id.rocket_tv:
                startActivity(new Intent(this, RoctetDemoActivity.class));
                break;
        }
    }

    private void showView(View view){
        mHexagonAnimView.setVisibility(View.GONE);
        mHexagonAnimLayout.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    private void startWholeAnim() {
        mHexagonAnimLayout.setAnimListener(new HexagonAnimLayout.AnimListenerSimple(){
            @Override
            public void onRotationStart() {
                if(mParticleView!= null){
                    mParticleView.startBackCenterAnim(false);
                }
            }

            @Override
            public void onRotationEnd() {
                if(mParticleView!=null){
                    mParticleView.startBackCenterAnim(true);
                }
            }
        });
        mHexagonAnimLayout.setRotateStart();
        mHexagonAnimLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHexagonAnimLayout.setRotateEnd();
            }
        },2000);

    }
}
