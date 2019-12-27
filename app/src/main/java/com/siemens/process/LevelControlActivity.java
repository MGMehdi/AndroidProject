package com.siemens.process;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.siemens.R;
import com.siemens.Simatic_S7.ReadTaskS7;
import com.siemens.Simatic_S7.WriteTaskS7;

public class LevelControlActivity extends AppCompatActivity implements View.OnClickListener {

    private ReadTaskS7 readTaskS7;
    private WriteTaskS7 writeTaskS7;

    private TextView test1, test2;
    private EditText et1, et2;
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



        this.test1 = findViewById(R.id.tv_test1);
        this.test2 = findViewById(R.id.tv_test2);

        this.et1 = findViewById(R.id.ed_1);
        this.et2 = findViewById(R.id.ed_2);

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
            readTaskS7 = new ReadTaskS7(v, test1, test2);
            readTaskS7.Start(this._ip, this._rack, this._slot);

            writeTaskS7 = new WriteTaskS7();
            writeTaskS7.Start(this._ip, this._rack, this._slot);

            break;
        case R.id.bt_test :
            int a = Integer.parseInt(String.valueOf(et1.getText()));
            int b = Integer.parseInt(String.valueOf(et2.getText()));
            writeTaskS7.setWriteBool(a, b);
            break;
        default:

    }
    }

}

