package com.siemens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.siemens.database.DatabaseHelper;
import com.siemens.database.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText _etLogin, _etPassword;
    private Button _btnLogin;
    private DatabaseHelper _db = new DatabaseHelper(this);
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.user = new User();

        this._etLogin = findViewById(R.id.et_login);
        this._etPassword = findViewById(R.id.et_password);
        this._btnLogin = findViewById(R.id.btn_login);
        this._btnLogin.setOnClickListener(this);

        if (this._db.getAllUser().getCount() == 0) {
            FirstStart();
        }
    }

    public void FirstStart() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome")
                .setMessage("This is the first time you launch this application. You must first create an administrator account.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent adminSignUp = new Intent(LoginActivity.this, InscriptionActivity.class);
                        adminSignUp.putExtra("isFirstStart", true);
                        startActivity(adminSignUp);
                        finish();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (this._etLogin.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a mail address", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Cursor cursor = _db.getOneUser(this._etLogin.getText().toString().toLowerCase());
                        if (cursor.moveToFirst()) {
                            this.user.set_mail(cursor.getString(0));
                            this.user.set_name(cursor.getString(1));
                            this.user.set_surname(cursor.getString(2));
                            this.user.set_password(cursor.getString(3));
                            this.user.set_privilege(cursor.getInt(4));
                        }
                        if (this.user.get_mail().equals(this._etLogin.getText().toString().toLowerCase())) {
                            if (this.user.get_password().equals(this._etPassword.getText().toString())) {
                                Intent menu = new Intent(this, MenuActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", this.user);
                                menu.putExtras(bundle);
                                startActivity(menu);
                                finish();
                            } else {
                                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }
}
