package com.siemens.process;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.siemens.ListActivity;
import com.siemens.R;
import com.siemens.Simatic_S7.ReadTaskS7;
import com.siemens.Simatic_S7.WriteTaskS7;

import java.util.ArrayList;

public class LevelControlActivity extends AppCompatActivity implements View.OnClickListener {

    private ReadTaskS7 readTaskS7;
    private WriteTaskS7 writeTaskS7;

    private ArrayList<TextView> tvs = new ArrayList<>();
    private TextView tv_title, tv_manaut, tv_man, tv_setpoint, tv_level, tv_output;
    private Button btest, connect;
    private SharedPreferences preferences = null;
    private String _ip, _rack, _slot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_control);

        try {
            this.preferences = getSharedPreferences("level_control", MODE_PRIVATE);
            this._ip = this.preferences.getString("IP", "");
            this._rack = this.preferences.getString("RACK", "");
            this._slot = this.preferences.getString("SLOT", "");
            System.out.println(this._ip + " " + this.preferences);

            if (this._ip == "" || this._rack == "" || this._slot == "") {

                new AlertDialog.Builder(this)
                        .setTitle("No configuration found")
                        .setMessage("Please configure first this API. Long press on button to edit")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                }).show();
            }

        }catch (Exception e){}


        this.tv_title = findViewById(R.id.tv_l_title);
        this.tv_manaut = findViewById(R.id.tv_l_manaut);
        this.tv_man = findViewById(R.id.tv_l_man);
        this.tv_output = findViewById(R.id.tv_l_output);
        this.tv_setpoint = findViewById(R.id.tv_l_setpoint);
        this.tv_level = findViewById(R.id.tv_l_level);

        this.tvs.add(tv_title);
        this.tvs.add(tv_manaut);
        this.tvs.add(tv_man);
        this.tvs.add(tv_output);
        this.tvs.add(tv_setpoint);
        this.tvs.add(tv_level);

        this.btest = findViewById(R.id.bt_test);
        this.btest.setOnClickListener(this);
        this.connect = findViewById(R.id.bt_connect);
        this.connect.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        readTaskS7.Stop();
        writeTaskS7.Stop();
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
    switch (v.getId()) {
        case R.id.bt_connect:
            if (this.connect.getText().equals("CONNECT")){
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.connect.setText("DISCONNECT");
                readTaskS7 = new ReadTaskS7(v, this.tvs);
                readTaskS7.Start(this._ip, this._rack, this._slot);

                writeTaskS7 = new WriteTaskS7();
                writeTaskS7.Start(this._ip, this._rack, this._slot);
            } else {
                this.connect.setText("CONNECT");
                this.tv_output.setText(null);
                this.tv_manaut.setText(null);
                this.tv_man.setText(null);
                this.tv_level.setText(null);
                this.tv_setpoint.setText(null);

                readTaskS7.Stop();
                writeTaskS7.Stop();
            }



            break;
        default:

    }

    }

}

