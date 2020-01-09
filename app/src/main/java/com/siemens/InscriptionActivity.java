package com.siemens;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.siemens.database.DatabaseHelper;
import com.siemens.database.User;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText _edName, _edSurname, _edMail, _edPassword, _edPasswordConfirm;
    private TextView _tvName, _tvSurname, _tvMail, _tvPassword, _tvPasswordConfirm;
    private boolean _isFirstStart = false;
    private boolean _isUserDetail = false;
    private boolean _isChangingPassword = false;
    private boolean _whatCheck[] = {false, false}; //0=userDate, 1=userPass
    private boolean ismodify = false;
    private Button _btnSignUp;
    private Button _btnChangePass;
    private Button _btnModify;
    private Button _btnSave;
    private Button _btnDelete;
    private String _userMail;
    private CheckBox _cbPrivilege;
    private DatabaseHelper _db = new DatabaseHelper(this);
    private User _user = new User();

    /************************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        this._tvMail = findViewById(R.id.tv_mail);
        this._tvName = findViewById(R.id.tv_name);
        this._tvSurname = findViewById(R.id.tv_surname);
        this._tvPassword = findViewById(R.id.tv_password);
        this._tvPasswordConfirm = findViewById(R.id.tv_confirm_password);

        this._edName = findViewById(R.id.editName);
        this._edSurname = findViewById(R.id.editSurname);
        this._edMail = findViewById(R.id.editMail);
        this._edPassword = findViewById(R.id.editPassword);
        this._edPasswordConfirm = findViewById(R.id.editPasswordConfirm);

        this._btnSignUp = findViewById(R.id.btnSignUp);
        this._btnSignUp.setOnClickListener(this);
        this._btnChangePass = findViewById(R.id.btnChangePass);
        this._btnChangePass.setOnClickListener(this);
        this._btnModify = findViewById(R.id.btnModify);
        this._btnModify.setOnClickListener(this);
        this._btnSave = findViewById(R.id.btnSave);
        this._btnSave.setOnClickListener(this);
        this._btnDelete = findViewById(R.id.btnDelete);
        this._btnDelete.setOnClickListener(this);

        this._cbPrivilege = findViewById(R.id.cbPrivilege);
        this._cbPrivilege.setOnClickListener(this);

        /************************************************************************************************************/
        // GET EXTRA
        try {
            Bundle extraUserDetail = this.getIntent().getExtras();
            this._isUserDetail = extraUserDetail.getBoolean("is_user_detail"); //ListActivity ItemClick
            this._userMail = extraUserDetail.getString("mail"); //ListActivity ItemClick

            Bundle extraIsFirstStart = this.getIntent().getExtras();
            this._isFirstStart = extraIsFirstStart.getBoolean("isFirstStart"); //LoginActivity FirstStart
        } catch (Exception e) {

        }
        /************************************************************************************************************/

        if (_isFirstStart) {
            this._cbPrivilege.setVisibility(View.GONE);
            this._cbPrivilege.setChecked(true);
        } else {
            this._cbPrivilege.setVisibility(View.VISIBLE);
        }

        // Want to see detail of an user
        if (_isUserDetail) {
            Cursor cursor = _db.getOneUser(this._userMail);
            if (cursor.moveToFirst()) {
                this._user.set_mail(cursor.getString(0));
                this._user.set_name(cursor.getString(1));
                this._user.set_surname(cursor.getString(2));
                this._user.set_password(cursor.getString(3));
                this._user.set_privilege(cursor.getInt(4));
            }

            //Set display
            if (this._user.get_privilege() == 0) {
                this._cbPrivilege.setChecked(false);
            } else {
                this._cbPrivilege.setChecked(true);
            }

            this._edMail.setText(this._user.get_mail());
            this._edName.setText(this._user.get_name());
            this._edSurname.setText(this._user.get_surname());
            this._edMail.setEnabled(false);
            this._edName.setEnabled(false);
            this._edSurname.setEnabled(false);
            this._cbPrivilege.setEnabled(false);

            this._btnModify.setVisibility(View.VISIBLE);
            this._btnChangePass.setVisibility(View.GONE);
            this._btnDelete.setVisibility(View.VISIBLE);

            this._btnSignUp.setVisibility(View.GONE);
            this._tvPasswordConfirm.setVisibility(View.GONE);
            this._tvPasswordConfirm.setVisibility(View.GONE);
            this._edPassword.setVisibility(View.GONE);
            this._edPasswordConfirm.setVisibility(View.GONE);
        }
        /************************************************************************************************************/
    } //END ONCREATE

    /************************************************************************************************************/
    @Override
    public void onBackPressed() {
        if (_isFirstStart) {
            Toast.makeText(this, "Create an user first", Toast.LENGTH_SHORT).show();
        } else {
            Intent list = new Intent(this, ListActivity.class);
            startActivity(list);
            finish();
        }
    }

    /************************************************************************************************************/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbPrivilege:
                if (this._cbPrivilege.isChecked()) {
                    this._user.set_privilege(1);
                } else {
                    this._user.set_privilege(0);
                }
                break;
            /************************************************************************************************************/

            case R.id.btnSignUp:
                this._whatCheck[0] = true;
                this._whatCheck[1] = false;
                if (checkUserData(this._whatCheck)) {
                    this._whatCheck[0] = false;
                    this._whatCheck[1] = true;
                    if (checkUserData(this._whatCheck)) {
                        DatabaseHelper _db = new DatabaseHelper(this);
                        if (this._isFirstStart) {
                            this._user.set_name(this._edName.getText().toString());
                            this._user.set_surname(this._edSurname.getText().toString());
                            this._user.set_mail(this._edMail.getText().toString());
                            this._user.set_password(this._edPassword.getText().toString());
                            this._user.set_privilege(1);
                            _db.addUser(this._user);
                        } else {
                            this._user.set_name(this._edName.getText().toString());
                            this._user.set_surname(this._edSurname.getText().toString());
                            this._user.set_mail(this._edMail.getText().toString());
                            this._user.set_password(this._edPassword.getText().toString());
                            this._user.set_privilege(0);
                            _db.addUser(this._user);
                        }
                        if (_isFirstStart) {
                            Intent menu = new Intent(this, MenuActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", this._user);
                            menu.putExtras(bundle);
                            startActivity(menu);
                            finish();
                        } else {
                            Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                            Intent list = new Intent(this, ListActivity.class);
                            startActivity(list);
                            finish();
                        }

                    }
                }
                break;
            /************************************************************************************************************/

            case R.id.btnModify:
                this.ismodify = true;
                this._edName.setEnabled(true);
                this._edSurname.setEnabled(true);
                this._edMail.setEnabled(false);

                if (this._user.get_privilege() == 1 && this._db.getAllSuperior().getCount() == 1) {
                    this._cbPrivilege.setEnabled(false);
                } else {
                    this._cbPrivilege.setEnabled(true);
                }

                this._btnChangePass.setVisibility(View.VISIBLE);
                this._btnSave.setVisibility(View.VISIBLE);
                this._btnModify.setVisibility(View.GONE);
                this._btnDelete.setVisibility(View.GONE);

                break;
            /************************************************************************************************************/

            case R.id.btnChangePass:
                this._tvPassword.setVisibility(View.VISIBLE);
                this._tvPasswordConfirm.setVisibility(View.VISIBLE);
                this._tvMail.setVisibility(View.GONE);
                this._tvName.setVisibility(View.GONE);
                this._tvSurname.setVisibility(View.GONE);
                this._cbPrivilege.setVisibility(View.GONE);

                this._edMail.setVisibility(View.GONE);
                this._edSurname.setVisibility(View.GONE);
                this._edName.setVisibility(View.GONE);
                this._edPassword.setVisibility(View.VISIBLE);
                this._edPasswordConfirm.setVisibility(View.VISIBLE);

                this._btnSave.setVisibility(View.VISIBLE);
                this._btnChangePass.setVisibility(View.GONE);
                this._btnModify.setVisibility(View.GONE);
                this._btnDelete.setVisibility(View.GONE);

                this._isChangingPassword = true;
                break;
            /************************************************************************************************************/

            case R.id.btnSave:
                if (!this._isChangingPassword) {
                    this._whatCheck[0] = true;
                    this._whatCheck[1] = false;
                    if (checkUserData(this._whatCheck)) {
                        this._user.set_mail(this._edMail.getText().toString());
                        this._user.set_name(this._edName.getText().toString());
                        this._user.set_surname(this._edSurname.getText().toString());

                        _db.updateUser(this._user);
                        Intent list = new Intent(this, ListActivity.class);
                        startActivity(list);
                        finish();
                    }
                }
                if (this._isChangingPassword) {
                    this._whatCheck[0] = false;
                    this._whatCheck[1] = true;
                    if (checkUserData(this._whatCheck)) {
                        this._user.set_password(this._edPassword.getText().toString());
                        _db.updatePassword(this._user, this._userMail);

                        Intent list = new Intent(this, ListActivity.class);
                        startActivity(list);
                        finish();
                    }
                }
                break;
            /************************************************************************************************************/

            case R.id.btnDelete:
                if (this._user.get_privilege() == 1) {
                    if (this._db.getAllSuperior().getCount() == 1) showAlertDialog();
                    else showConfirmDialog(_user);
                } else showConfirmDialog(_user);
                break;
            /************************************************************************************************************/

            default:
        }
    } //END ONCLICK

    /************************************************************************************************************/

    private boolean checkUserData(boolean checkWhat[]) {
        if (checkWhat[0]) {
            if (this._edName.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            } else if (this._edSurname.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a surname", Toast.LENGTH_SHORT).show();
            } else if (this._edMail.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
            }
            if (!this.ismodify && this._db.getOneUser(this._edMail.getText().toString()).getCount() == 1) {
                Toast.makeText(getApplicationContext(), "Mail address already used", Toast.LENGTH_SHORT).show();
            } else {
                if (_isFirstStart) {
                    return true;
                } else {
                    return true;
                }
            }
            return false;
        }
        if (checkWhat[1]) {
            if (this._edPassword.getText().toString().isEmpty() || this._edPasswordConfirm.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            } else if (this._edPassword.getText().toString().length() < 4) {
                Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_SHORT).show();
            } else if (!this._edPassword.getText().toString().equals(this._edPasswordConfirm.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Password is different", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
            return false;
        }
        return false;
    } //END CHECKUSERDATA

    /************************************************************************************************************/

    private void showConfirmDialog(final User user1) {
        System.out.println(_user.get_mail() + " " + user1.get_mail());

        new AlertDialog.Builder(this)
                .setTitle("Delete user")
                .setMessage("Are you sure you want to delete " + user1.get_surname() + " " + user1.get_name())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        _db.deleteUser(user1);
                        if (user1.get_privilege() == 1) {
                            Intent list = new Intent(InscriptionActivity.this, LoginActivity.class);
                            startActivity(list);
                            finish();
                        } else {
                            Intent list = new Intent(InscriptionActivity.this, ListActivity.class);
                            startActivity(list);
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    } //END showConfirmDialog

    /************************************************************************************************************/

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("You can't delete the last superior")
                .setNeutralButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}