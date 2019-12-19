package com.siemens.process;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.siemens.R;
import com.siemens.Simatic_S7.ReadTaskS7;

public class LevelControlActivity extends AppCompatActivity implements View.OnClickListener {

    private ReadTaskS7 readTaskS7;


    private TextView test;
    private Button btest, connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_control);

        this.test = findViewById(R.id.ed_test);
        this.btest = findViewById(R.id.bt_test);
        this.btest.setOnClickListener(this);
        this.connect = findViewById(R.id.bt_connect);
        this.connect.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
    switch (v.getId()) {
        case R.id.bt_connect:
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            readTaskS7 = new ReadTaskS7(v, test);
            readTaskS7.Start("192.168.0.100","0", "2");

            break;
        case R.id.bt_test :

            break;
        default:

    }
    }

}

