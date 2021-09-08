package com.example.appnotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.appnotes.entities.Note;

import java.util.List;
// DAO : noi tạo ra các phương thức như Update , Delete,...,

@Dao
public interface NoteDao {
    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    List<Note> getAllnotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insertnote(Note note);
    @Delete
    void deletenote(Note note);

}
