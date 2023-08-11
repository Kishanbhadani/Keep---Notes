package com.example.keep_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView RecyleNote;
    ExtendedFloatingActionButton fadeBtn;
    FloatingActionButton fadeAdd,fadeHelp;
    Button createBtn;
    LinearLayout NoNotes;
    TextView fadeAddTxt,fadeHelpTxt;
    Boolean isAllFabsVisible = false;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initvar();
        showNotes();

        fadeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAllFabsVisible){
                    fadeAdd.show();
                    fadeHelp.show();
                    fadeAddTxt.setVisibility(View.VISIBLE);
                    fadeHelpTxt.setVisibility(View.VISIBLE);
                    fadeBtn.extend();
                    isAllFabsVisible = true;
                }
                else{
                    fadeAdd.hide();
                    fadeHelp.hide();
                    fadeAddTxt.setVisibility(View.GONE);
                    fadeHelpTxt.setVisibility(View.GONE);
                    fadeBtn.shrink();
                    isAllFabsVisible = false;
                }
            }
        });

        fadeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_layout);

                Button dialogbtn;
                EditText editTitle,editContent;

                dialogbtn = dialog.findViewById(R.id.dialogbtn);
                editContent = dialog.findViewById(R.id.editContent);
                editTitle = dialog.findViewById(R.id.editTitle);

                dialogbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Title = editTitle.getText().toString();
                        String Content = editContent.getText().toString();

                        if(!Content.equals("")){
                            databaseHelper.noteDao().addNote(new Note(Title, Content));
                            showNotes();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "write Content", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();

            }
        });

        fadeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Help_screen.class);
                startActivity(i);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeBtn.performClick();
            }
        });
    }

    //disply note recycler view
    public void showNotes() {
        ArrayList<Note> arrrNote = (ArrayList<Note>) databaseHelper.noteDao().getNotes();

        if(arrrNote.size()>0){
            RecyleNote.setVisibility(View.VISIBLE);
            NoNotes.setVisibility(View.GONE);

            RecyleNote.setAdapter(new RecycleNoteAdapter(this,arrrNote,databaseHelper));
        }
        else{
            RecyleNote.setVisibility(View.GONE);
            NoNotes.setVisibility(View.VISIBLE);
        }
    }

    //intiallize varible
    private void initvar() {
        createBtn = findViewById(R.id.createBtn);
        RecyleNote = findViewById(R.id.RecyleNote);
        fadeBtn = findViewById(R.id.fadeBtn);
        fadeAdd = findViewById(R.id.fadeAdd);
        fadeAddTxt = findViewById(R.id.fadeAddTxt);
        fadeHelp = findViewById(R.id.fadeHelp);
        fadeHelpTxt = findViewById(R.id.fadeHelpTxt);
        NoNotes = findViewById(R.id.NoNotes);

        fadeBtn.shrink();
        fadeAdd.setVisibility(View.GONE);
        fadeAddTxt.setVisibility(View.GONE);
        fadeHelp.setVisibility(View.GONE);
        fadeHelpTxt.setVisibility(View.GONE);

//        set to gridLayot
//        RecyleNote.setLayoutManager(new GridLayoutManager(this, 2));

        // dyanamic grid like googleNote used
        StaggeredGridLayoutManager CustomLayoutManager;
        RecyleNote.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        databaseHelper = DatabaseHelper.getInstance(this);
    }

    //back press exit app
    @Override
    public void onBackPressed() {
        AlertDialog exitAlert = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit App")
                .setMessage("You want to sure exit app ?")
                .setIcon(R.drawable.baseline_exit_to_app_24)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}