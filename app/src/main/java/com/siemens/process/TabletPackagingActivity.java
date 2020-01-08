package com.siemens.process;

import androidx.appcompat.app.AppCompatActivity;

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

import com.siemens.R;
import com.siemens.Simatic_S7.ReadTaskS7;
import com.siemens.Simatic_S7.WriteTaskS7;

import java.util.ArrayList;

public class TabletPackagingActivity extends AppCompatActivity implements View.OnClickListener {

    private ReadTaskS7 readTaskS7;
    private WriteTaskS7 writeTaskS7;

    private TextView tv_title, tv_bottles, tv_pill, tv_operation, tv_motor;
    private ArrayList<TextView> tvs = new ArrayList<>();
    private EditText caca;
    private Button connect, send;
    private SharedPreferences preferences = null;
    private String _ip, _rack, _slot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet_packaging);

        try {
            this.preferences = getSharedPreferences("tablet_packaging", MODE_PRIVATE);
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


        this.tv_title = findViewById(R.id.tv_tl_title);
        this.tv_bottles = findViewById(R.id.tv_tp_bottles);
        this.tv_pill = findViewById(R.id.tv_tp_pilldemand);
        this.tv_operation = findViewById(R.id.tv_tp_inoperation);
        this.tv_motor = findViewById(R.id.tv_tp_bandmotor);

        this.tvs.add(tv_title);
        this.tvs.add(tv_bottles);
        this.tvs.add(tv_pill);
        this.tvs.add(tv_operation);
        this.tvs.add(tv_motor);

        this.connect = findViewById(R.id.bt_connect);
        this.connect.setOnClickListener(this);
        this.send = findViewById(R.id.bt_test);
        this.send.setOnClickListener(this);

        caca = findViewById(R.id.caca);

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
                    readTaskS7 = new ReadTaskS7(v, tvs);
                    readTaskS7.Start(this._ip, this._rack, this._slot);



                } else {
                    this.connect.setText("CONNECT");
                    this.tv_bottles.setText(null);
                    this.tv_pill.setText(null);
                    this.tv_operation.setText(null);
                    this.tv_motor.setText(null);

                    readTaskS7.Stop();
                    writeTaskS7.Stop();
                }
                break;

            case R.id.bt_test:
                try {
                    Thread.sleep(1000);
                } catch (Exception e){}
                    writeTaskS7 = new WriteTaskS7();
                    writeTaskS7.Start(this._ip, this._rack, this._slot);
                    writeTaskS7.WriteByte(254);
                    writeTaskS7.Stop();
            default:

        }

    }

}
