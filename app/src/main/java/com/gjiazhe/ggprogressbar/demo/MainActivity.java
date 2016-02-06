package com.gjiazhe.ggprogressbar.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gjiazhe.ggprogressbar.GGArcProgressBar;
import com.gjiazhe.ggprogressbar.GGLinearProgressBar;

/**
 * Created by gjz on 12/11/15.
 */
public class MainActivity extends AppCompatActivity {

    private Button add_btn;
    private Button reset_btn;
    private GGLinearProgressBar linearBar_tb;
    private GGLinearProgressBar linearBar_lr;
    private GGLinearProgressBar linearBar_bt;
    private GGLinearProgressBar linearBar_rl;
    private GGArcProgressBar circularBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_btn = (Button) findViewById(R.id.add_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        linearBar_tb = (GGLinearProgressBar) findViewById(R.id.linear_bar_tb);
        linearBar_lr = (GGLinearProgressBar) findViewById(R.id.linear_bar_lr);
        linearBar_bt = (GGLinearProgressBar) findViewById(R.id.linear_bar_bt);
        linearBar_rl = (GGLinearProgressBar) findViewById(R.id.linear_bar_rl);
        circularBar = (GGArcProgressBar) findViewById(R.id.circular_bar_1);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBar_tb.addProgressBy(10);
                linearBar_lr.addProgressBy(10);
                linearBar_bt.addProgressBy(10);
                linearBar_rl.addProgressBy(10);
                circularBar.addProgressBy(10);
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBar_tb.resetProgress();
                linearBar_lr.resetProgress();
                linearBar_bt.resetProgress();
                linearBar_rl.resetProgress();
                circularBar.resetProgress();
            }
        });
    }
}
