package com.example.appnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appnotes.Adapter.NotesAdapter;
import com.example.appnotes.database.NotesDatabase;
import com.example.appnotes.entities.Note;
import com.example.appnotes.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {
    private ImageView imgaddnotemain;
    //tim kiem
    private EditText edtsearch;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    // view and update
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    // view and update( hiển thị toàn bộ notes)
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    private Button btnchuongbao;
    private RecyclerView rcv_note;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    // khai báo (8)
    private int noteCickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // anh xa
        imgaddnotemain = findViewById(R.id.imageaddnotemain);
        edtsearch = findViewById(R.id.edtsearch);
        // su kien
        imgaddnotemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE
                );
            }
        });

        rcv_note = findViewById(R.id.rcv_note);
        rcv_note.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        rcv_note.setAdapter(notesAdapter);
        // view and updta
        // phương thức getNotes () này được gọi từ phương thức onCreate () của một hoạt động
        getNotes(REQUEST_CODE_SHOW_NOTES, false);

        // tim kiem
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size() != 0) {
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });
        btnchuongbao = findViewById(R.id.btnnotificaton);
        btnchuongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DatLich.class);
                startActivity(intent);
            }
        });

    }
    // Co su thay doi khi den : View and Update

    private void getNotes(final int requestcode, final boolean isNotDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<Note>> {
            // ham nay bat buoc phai co
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao().getAllnotes();// lay het trong co so du lieu ra
            }
            // nhan ket qua tu doBackground tra ve
            // sau khi tien trinh ket thuc thi ham nay  se tu dong say ra
            // lay ddoc tket qua  ve sau khi  thuc hien tien trinh ket thuc o day
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestcode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "hien thi tat ca cac item ", Toast.LENGTH_SHORT).show();
                }
                else if (requestcode == REQUEST_CODE_ADD_NOTE) {
                    // vi vậy thêm ghi chu đầu tiên duy nhất ( ghi chú mới đc thêm vào)
                    // từ csdl  để ghi chú danh sách và thông báo cho adapter mới đã chèn iteam và cuộn chế dộ xem tái chế lên đầu
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    rcv_note.smoothScrollToPosition(0);
                    Toast.makeText(MainActivity.this, "them vao co so du lieu  ", Toast.LENGTH_SHORT).show();
                } else if (requestcode == REQUEST_CODE_UPDATE_NOTE) {
                    // vị trí và thêm ghi chú cập nhật mới nhất từ cùng một vị trí từ cơ sở dữ liệu và thông báo
                    // cho adpater cho muc dich thay doi vi tri
                    noteList.remove(noteCickedPosition);
                    if (isNotDeleted) {
                        notesAdapter.notifyItemRemoved(noteCickedPosition);
                        Toast.makeText(MainActivity.this, "ban da bi xoa ", Toast.LENGTH_SHORT).show();
                    } else {
                        noteList.add(noteCickedPosition, notes.get(noteCickedPosition));
                        notesAdapter.notifyItemChanged(noteCickedPosition);
                        Toast.makeText(MainActivity.this, "da duoc cap nhat ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        // execute : goi khi kich hoat tien trinh
        new GetNoteTask().execute();
    }
    // nhan ket qua tra ve

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            // nó có nghĩa là ghi chú mới được thêm vào từ hoạt động CreateNote và kết quả là nó sẽ được gửi lại
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {

            //nó có nghĩa là đã ghi chú có sẵn được cập nhật từ Create acivity và kết quả của nó
            if (data != null) {
                // bi xoa trong csdl theo tham so isNoteDeleted
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }

    // ham (View and Update)
    @Override
    public void onNoteClickend(Note note, int position) {
        noteCickedPosition = position;
        Toast.makeText(this, "anh ky : "+noteCickedPosition, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }
}