package com.example.appnotes.Adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnotes.R;
import com.example.appnotes.entities.Note;
import com.example.appnotes.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;
    // View and Update Note
    private NotesListener notesListener;
    // tim kiem
    private Timer timer;
    private List<Note>noteSource;

    public NotesAdapter(List<Note> notes,NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;
        noteSource = notes;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_notes,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClickend(notes.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(notes!=null){
            return notes.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView txttitle, txtsubtitle, txtdatetime;
        LinearLayout layoutNote;

        RoundedImageView imgaNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txttitle);
            txtsubtitle = itemView.findViewById(R.id.txtsubtitle);
            txtdatetime = itemView.findViewById(R.id.txtdatetime);
            layoutNote = itemView.findViewById(R.id.layoutnote);
            imgaNote = itemView.findViewById(R.id.imageNote);
        }

        public void setNote(Note note) {
            txttitle.setText(note.getTitle());
            if (note.getSubtile().trim().isEmpty()) {
                txtsubtitle.setVisibility(View.GONE);
            } else {
                txtsubtitle.setText(note.getSubtile());
            }
            txtdatetime.setText(note.getDatetime());
            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if(note.getColor()!=null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            // hien thi anh la o day
            if(note.getImgpath()!=null){
                imgaNote.setImageBitmap(BitmapFactory.decodeFile(note.getImgpath()));
                imgaNote.setVisibility(View.VISIBLE);
            }else{
                imgaNote.setVisibility(View.GONE);
            }
        }
    }
    // tim kiem
    public void searchNotes(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()){
                    notes = noteSource;
                }else{
                    ArrayList<Note>temp = new ArrayList<>();
                    for(Note note:noteSource){
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                        || note.getSubtile().toLowerCase().contains(searchKeyword.toLowerCase())
                        || note.getNotetext().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }
    //
    public void cancelTimer(){
        if(timer!=null){
            timer.cancel();
        }
    }
}
