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
    private GGLinearProgressBar verticalBar;
    private GGLinearProgressBar linearProgressBar;
    private GGArcProgressBar circularBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_btn = (Button) findViewById(R.id.add_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        verticalBar = (GGLinearProgressBar) findViewById(R.id.vertical_bar);
        linearProgressBar = (GGLinearProgressBar) findViewById(R.id.linear_bar);
        circularBar = (GGArcProgressBar) findViewById(R.id.circular_bar);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalBar.addProgressBy(10);
                linearProgressBar.addProgressBy(10);
                circularBar.addProgressBy(10);
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalBar.resetProgress();
                linearProgressBar.resetProgress();
                circularBar.resetProgress();
            }
        });
    }
}
