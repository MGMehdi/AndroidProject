package com.siemens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.siemens.database.User;
import com.siemens.process.APISettingActivity;
import com.siemens.process.LevelControlActivity;
import com.siemens.process.TabletPackagingActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView _tv_welcome;
    private Button _btn_tablet_packaging, _btn_level_control, _btn_logout, _btn_management;
    private User user;
    private Intent settings;
    private APISettingActivity _apiSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        this._tv_welcome = findViewById(R.id.tv_welcome);
        this._btn_tablet_packaging = findViewById(R.id.btn_tablet_packaging);
        this._btn_level_control = findViewById(R.id.btn_level_control);
        this._btn_management = findViewById(R.id.btn_management);
        this._btn_logout = findViewById(R.id.btn_log_out);


        this._btn_tablet_packaging.setOnClickListener(this);
        this._btn_level_control.setOnClickListener(this);
        this._btn_management.setOnClickListener(this);
        this._btn_logout.setOnClickListener(this);

        this._btn_tablet_packaging.setOnLongClickListener(this);
        this._btn_level_control.setOnLongClickListener(this);

        Bundle userDetail = this.getIntent().getExtras();

        this.user = (User) userDetail.getSerializable("user");


        this._tv_welcome.setText("Welcome " + this.user.get_name() + " " + this.user.get_surname());

        if (this.user.get_privilege() == 1) {
            findViewById(R.id.view_management).setVisibility(View.VISIBLE);
            this._btn_management.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tablet_packaging:
                Intent tabletPackaging = new Intent(this, TabletPackagingActivity.class);
                startActivity(tabletPackaging);
                break;

            case R.id.btn_level_control:
                Intent levelControl = new Intent(this, LevelControlActivity.class);
                startActivity(levelControl);
                break;

            case R.id.btn_management:
                Intent list = new Intent(this, ListActivity.class);
                startActivity(list);
                break;

            case R.id.btn_log_out:
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                finish();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tablet_packaging:
                this.settings = new Intent(this, APISettingActivity.class);
                settings.putExtra("api", "tablet_packaging");
                startActivity(settings);
                break;
            case R.id.btn_level_control:
                this.settings = new Intent(this, APISettingActivity.class);
                settings.putExtra("api", "level_control");
                startActivity(settings);
                break;
        }

        return false;

    }
}
