package com.siemens.process;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.siemens.R;
import com.siemens.Simatic_S7.ReadTaskS7;
import com.siemens.Simatic_S7.WriteTaskS7;
import com.siemens.database.User;

import java.util.ArrayList;

public class TabletPackagingActivity extends AppCompatActivity implements View.OnClickListener {

    private ReadTaskS7 readTaskS7;
    private WriteTaskS7 writeTaskS7;
    private User user;

    private GridLayout gridWrite;
    private TextView tv_title, tv_bottles, tv_pill, tv_operation, tv_motor;
    private ArrayList<TextView> tvs = new ArrayList<>();
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private EditText writeValue;
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
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }

        } catch (Exception e) {
        }


        Bundle userDetail = this.getIntent().getExtras();
        this.user = (User) userDetail.getSerializable("user");

        this.gridWrite = findViewById(R.id.gridWrite);

        this.tv_title = findViewById(R.id.tv_tl_title);
        this.tv_bottles = findViewById(R.id.tv_tp_bottles);
        this.tv_pill = findViewById(R.id.tv_tp_pilldemand);
        this.tv_operation = findViewById(R.id.tv_tp_inoperation);
        this.tv_motor = findViewById(R.id.tv_tp_bandmotor);
        this.writeValue = findViewById(R.id.et_writeValue);

        this.rb1 = findViewById(R.id.radio1);
        this.rb2 = findViewById(R.id.radio2);
        this.rb3 = findViewById(R.id.radio3);
        this.rb4 = findViewById(R.id.radio4);
        this.rb5 = findViewById(R.id.radio5);

        this.tvs.add(tv_title);
        this.tvs.add(tv_bottles);
        this.tvs.add(tv_pill);
        this.tvs.add(tv_operation);
        this.tvs.add(tv_motor);

        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);
        radioButtons.add(rb4);
        radioButtons.add(rb5);

        this.connect = findViewById(R.id.bt_connect);
        this.connect.setOnClickListener(this);
        this.send = findViewById(R.id.bt_send);
        this.send.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (this.connect.getText().toString().equals("CONNECT")) {
            finish();
        } else {
            readTaskS7.Stop();
            writeTaskS7.Stop();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_connect:
                if (this.connect.getText().equals("CONNECT")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.connect.setText("DISCONNECT");
                    if (this.user.get_privilege() == 1) {
                        gridWrite.setVisibility(View.VISIBLE);
                    }
                    readTaskS7 = new ReadTaskS7(v, tvs);
                    readTaskS7.Start(this._ip, this._rack, this._slot);

                } else {
                    this.connect.setText("CONNECT");
                    gridWrite.setVisibility(View.GONE);
                    this.tv_bottles.setText(null);
                    this.tv_pill.setText(null);
                    this.tv_operation.setText(null);
                    this.tv_motor.setText(null);

                    readTaskS7.Stop();
                }
                break;

            case R.id.bt_send:
                Boolean checked = false;
                for (RadioButton r : radioButtons) if (r.isChecked()) checked = true;
                if (this.writeValue.getText().toString().isEmpty()) Toast.makeText(this, "Enter a value", Toast.LENGTH_SHORT).show();
                else if (!checked) Toast.makeText(this, "Choose a DDB", Toast.LENGTH_SHORT).show();
                else if (checked) {
                    writeTaskS7 = new WriteTaskS7();
                    writeTaskS7.Start(this._ip, this._rack, this._slot);
                    int value = Integer.parseInt(this.writeValue.getText().toString());
                    if (rb1.isChecked()) writeTaskS7.WriteByte(5, value);
                    else if (rb2.isChecked()) writeTaskS7.WriteByte(6, value);
                    else if (rb3.isChecked()) writeTaskS7.WriteByte(7, value);
                    else if (rb4.isChecked()) writeTaskS7.WriteByte(8, value);
                    else if (rb5.isChecked()) writeTaskS7.WriteInt(18, value);
                    writeTaskS7.Stop();
                }

            default:
        }
    }
}
