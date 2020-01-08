package com.siemens.process;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.siemens.R;

public class APISettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText _ed_IP, _ed_Rack, _ed_Slot;
    private Button _btn_apply;
    private String _apiSettings;
    private SharedPreferences preferences = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apisetting);



        Bundle intent = this.getIntent().getExtras();
        this._apiSettings = intent.getString("api");

        this.preferences = getSharedPreferences(this._apiSettings, MODE_PRIVATE);
        this._ed_IP = findViewById(R.id.ed_IP);
        this._ed_IP.setText(this.preferences.getString("IP", ""));
        this._ed_Rack = findViewById(R.id.ed_Rack);
        this._ed_Rack.setText(this.preferences.getString("RACK", ""));
        this._ed_Slot = findViewById(R.id.ed_Slot);
        this._ed_Slot.setText(this.preferences.getString("SLOT", ""));

        this._btn_apply = findViewById(R.id.btn_apply);
        this._btn_apply.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply:
                if (Patterns.IP_ADDRESS.matcher(this._ed_IP.getText().toString()).matches()){
                    if (this._ed_Rack.getText().toString().isEmpty() || this._ed_Slot.getText().toString().isEmpty()){
                        Toast.makeText(this, "Invalid rack or slot", Toast.LENGTH_SHORT).show();
                    } else {
                        this.preferences.edit()
                                .putString("IP", this._ed_IP.getText().toString())
                                .putString("RACK", this._ed_Rack.getText().toString())
                                .putString("SLOT", this._ed_Slot.getText().toString())
                                .apply();
                        finish();
                    }

                } else {
                    Toast.makeText(this, "Invalid IP", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
