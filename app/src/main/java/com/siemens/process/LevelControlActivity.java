package com.siemens.process;

import android.annotation.SuppressLint;
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

public class LevelControlActivity extends AppCompatActivity implements View.OnClickListener {

    private ReadTaskS7 readTaskS7;
    private WriteTaskS7 writeTaskS7;
    private User user;
    private ArrayList<TextView> tvs = new ArrayList<>();

    private GridLayout gridWrite;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6;
    private TextView tv_title, tv_manaut, tv_man, tv_setpoint, tv_level, tv_output;
    private Button send, connect;
    private EditText writeValue;
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();

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

        this.send = findViewById(R.id.bt_send);
        this.send.setOnClickListener(this);
        this.connect = findViewById(R.id.bt_connect);
        this.connect.setOnClickListener(this);

        this.writeValue = findViewById(R.id.et_writeValue);

        this.rb1 = findViewById(R.id.radio1);
        this.rb2 = findViewById(R.id.radio2);
        this.rb3 = findViewById(R.id.radio3);
        this.rb4 = findViewById(R.id.radio4);
        this.rb5 = findViewById(R.id.radio5);
        this.rb6 = findViewById(R.id.radio6);

        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);
        radioButtons.add(rb4);
        radioButtons.add(rb5);
        radioButtons.add(rb6);

    }

    @Override
    public void onBackPressed() {
        if (this.connect.getText().equals("CONNECT")) {
            finish();
        } else {
            readTaskS7.Stop();
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
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
                    this.writeValue.setText(null);
                    for (RadioButton r : radioButtons) {
                        r.setChecked(false);
                    }
                    gridWrite.setVisibility(View.GONE);
                    this.tv_setpoint.setText(null);
                    this.tv_level.setText(null);
                    this.tv_man.setText(null);
                    this.tv_output.setText(null);
                    this.tv_manaut.setText(null);

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

                    if (rb1.isChecked()) writeTaskS7.WriteByte(2, value);
                    else if (rb2.isChecked()) writeTaskS7.WriteByte(3, value);
                    else if (rb3.isChecked()) writeTaskS7.WriteInt(24, value);
                    else if (rb4.isChecked()) writeTaskS7.WriteInt(26, value);
                    else if (rb5.isChecked()) writeTaskS7.WriteInt(28, value);
                    else if (rb6.isChecked()) writeTaskS7.WriteInt(30, value);

                    writeTaskS7.Stop();
                }

                break;
        }

    }


}

