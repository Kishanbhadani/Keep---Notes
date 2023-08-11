package com.example.keep_note;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleNoteAdapter extends RecyclerView.Adapter<RecycleNoteAdapter.ViewHolder> {
    Context context;
    ArrayList<Note> arrNotes = new ArrayList<>();
    DatabaseHelper databaseHelper;

    RecycleNoteAdapter(Context context, ArrayList<Note> arrNotes, DatabaseHelper databaseHelper){
        this.context = context;
        this.arrNotes = arrNotes;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public RecycleNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyler_list_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleNoteAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.TxtTitle.setText(arrNotes.get(position).getTitle());
        holder.TxtCnt.setText(arrNotes.get(position).getContent());
        holder.DeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
        holder.CardNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView TxtTitle, TxtCnt;
        ImageView DeleteItem;
        LinearLayout CardNote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CardNote = itemView.findViewById(R.id.CardNote);
            TxtTitle = itemView.findViewById(R.id.TxtTitle);
            TxtCnt = itemView.findViewById(R.id.TxtCnt);
            DeleteItem = itemView.findViewById(R.id.DeleteItem);
        }
    }

    //update note
    private void UpdateItem(int pos) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_item_dialog);

        Button UpdateBtn;
        EditText UpdateTitle,UpdateCnt;

        UpdateBtn = dialog.findViewById(R.id.UpdateBtn);
        UpdateCnt = dialog.findViewById(R.id.UpdateCnt);
        UpdateTitle = dialog.findViewById(R.id.UpdateTitle);

        UpdateCnt.setText(arrNotes.get(pos).getContent());
        UpdateTitle.setText(arrNotes.get(pos).getTitle());

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = "", Content = "";
                Title = UpdateTitle.getText().toString();
                Content = UpdateCnt.getText().toString();

                if(!Content.equals("")){
                    databaseHelper.noteDao().updateNote(new Note(arrNotes.get(pos).getId(),Title,Content));
                    ((MainActivity)context).showNotes();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(context, "write Content", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

    }


    //delete Note
    private void deleteItem(int pos) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Delete Note")
                .setMessage("Are you sure want to delete?")
                .setIcon(R.drawable.baseline_delete_sweep_24)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.noteDao().deleteNote(new Note(arrNotes.get(pos).getId(), arrNotes.get(pos).getTitle(), arrNotes.get(pos).getContent()));
                        ((MainActivity) context).showNotes();
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
