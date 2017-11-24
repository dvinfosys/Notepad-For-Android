package com.example.vaksys_49.notepad;

/**
 * Created by vaksys-49 on 15/6/17.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import hitesh.simplenotepad.R;

public class DisplayNote extends AppCompatActivity {
    private NDb mydb;
    EditText name;
    EditText content;
    private RelativeLayout coordinatorLayout;
    String dateString;
    Bundle extras;
    int id_To_Update = 0;
    Snackbar snackbar;
    Button yes,no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewnotepad);


        name = (EditText) findViewById(R.id.txtname);

        name.setFocusable(true);
        content = (EditText) findViewById(R.id.txtcontent);
        coordinatorLayout = (RelativeLayout) findViewById(R.id.coordinatorLayout);
        mydb = new NDb(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                snackbar = Snackbar
                        .make(coordinatorLayout, "Note Name : "+String.valueOf(Value), Snackbar.LENGTH_LONG);
                snackbar.show();
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String nam = rs.getString(rs.getColumnIndex(NDb.name));
                String contents = rs.getString(rs.getColumnIndex(NDb.remark));
                if (!rs.isClosed()) {
                    rs.close();
                }
                name.setText((CharSequence) nam);
                content.setText((CharSequence) contents);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            getMenuInflater().inflate(R.menu.display_menu, menu);
        }
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
       int id=item.getItemId();

        if (id==R.id.Delete) {

            final AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setMessage(R.string.DeleteNote).setTitle("Delete Note")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mydb.deleteNotes(id_To_Update);
                            Toast.makeText(DisplayNote.this, "Deleted Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    MyNotes.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.DeleteNote)
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        mydb.deleteNotes(id_To_Update);
                                        Toast.makeText(DisplayNote.this, "Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(
                                                getApplicationContext(),
                                                MyNotes.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();
                return true;*/
        }
        if (id==R.id.Save) {
            Bundle extras = getIntent().getExtras();
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c.getTime());
            dateString = formattedDate;
            if (extras != null) {
                int Value = extras.getInt("id");
                if (Value > 0) {
                    if (content.getText().toString().trim().equals("")
                            || name.getText().toString().trim().equals("")) {

                        snackbar = Snackbar
                                .make(coordinatorLayout, "Please fill in name of the note", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        if (mydb.updateNotes(id_To_Update, name.getText()
                                .toString(), dateString, content.getText()
                                .toString())) {
                            snackbar = Snackbar
                                    .make(coordinatorLayout, "Your note Updated Successfully!!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            snackbar = Snackbar
                                    .make(coordinatorLayout, "There's an error. That's all I can tell. Sorry!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                } else {
                    if (content.getText().toString().trim().equals("")
                            || name.getText().toString().trim().equals("")) {
                        snackbar = Snackbar
                                .make(coordinatorLayout, "Please fill in name of the note", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        if (mydb.insertNotes(name.getText().toString(), dateString,
                                content.getText().toString())) {
                            snackbar = Snackbar
                                    .make(coordinatorLayout, "Added Successfully.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            snackbar = Snackbar
                                    .make(coordinatorLayout, "Unfortunately Task Failed.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }
            }
        }

                return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(
                getApplicationContext(),
                MyNotes.class);
        startActivity(intent);
        finish();
        return;
    }
}