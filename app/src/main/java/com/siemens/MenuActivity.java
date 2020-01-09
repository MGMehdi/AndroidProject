package com.siemens;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.siemens.Simatic_S7.WriteTaskS7;
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
    private ConnectivityManager connexStatus;
    private NetworkInfo network;
    private WriteTaskS7 writeS7;

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

        if (this.user.get_privilege() == 1) {
            findViewById(R.id.view_management).setVisibility(View.VISIBLE);
            this._btn_management.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("You'll be disconnect")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent Login = new Intent(MenuActivity.this, LoginActivity.class);
                        startActivity(Login);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle;
        switch (v.getId()) {
            case R.id.btn_tablet_packaging:
                Intent tabletPackaging = new Intent(this, TabletPackagingActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("user", this.user);
                tabletPackaging.putExtras(bundle);
                startActivity(tabletPackaging);
                break;

            case R.id.btn_level_control:
                Intent levelControl = new Intent(this, LevelControlActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("user", this.user);
                levelControl.putExtras(bundle);
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
        if (this.user.get_privilege() == 1) {
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

        } else Toast.makeText(this, "Unauthorized action", Toast.LENGTH_SHORT).show();
        return false;
    }
}
