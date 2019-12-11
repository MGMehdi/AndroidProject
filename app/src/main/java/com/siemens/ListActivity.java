package com.siemens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.siemens.database.DatabaseHelper;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener, View.OnClickListener {

    private DatabaseHelper db = new DatabaseHelper(this);

    private Button _btnNewUser;

    private ArrayList<String> listItem;
    private ListView userList;
    private ArrayAdapter adapter;

    /************************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this._btnNewUser = findViewById(R.id.btn_new_user);
        this._btnNewUser.setOnClickListener(this);
        this.listItem = new ArrayList<>();
        this.userList = findViewById(R.id.list);
        this.userList.setOnItemClickListener(this);

        listAllUser();
    }
    /************************************************************************************************************/

    public void listAllUser() {
        Cursor cursor = db.getAllUser();
        listItem = new ArrayList<>();

        while (cursor.moveToNext()) {
            listItem.add(cursor.getString(0) + "\n" + cursor.getString(1) + " " + cursor.getString(2));
        }
        this.adapter = new ArrayAdapter<>(this, R.layout.row, listItem);
        this.userList.setAdapter(adapter);
    }
    /************************************************************************************************************/

    // When item in the list is clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String[] mail = this.userList.getAdapter().getItem(position).toString().split("\n");
        Intent userDetail = new Intent(this, InscriptionActivity.class);
        userDetail.putExtra("is_user_detail", true);
        userDetail.putExtra("mail", mail[0]);
        startActivity(userDetail);
        finish();
    }
    /************************************************************************************************************/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_user:
                Intent newUser = new Intent(this, InscriptionActivity.class);
                startActivity(newUser);
                finish();
        }
    }
}
