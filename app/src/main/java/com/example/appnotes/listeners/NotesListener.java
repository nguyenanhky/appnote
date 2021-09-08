package com.example.appnotes.listeners;

import com.example.appnotes.entities.Note;

public interface NotesListener {
    public void onNoteClickend(Note note, int position);
}
