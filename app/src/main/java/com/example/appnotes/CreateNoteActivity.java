package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnotes.database.NotesDatabase;
import com.example.appnotes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {
    private ImageView imageback, imagesave;
    private EditText edtinputnotetitle, edtinputsubtitle, edtinpunote;
    private TextView txtdatetime;
    private ImageView imageNote;

    private String selectedNoteColor;
    private String selectImagePath;

    private View viewSubtitleIndicator;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    // add URL
    private TextView txtWebURL;
    private LinearLayout layoutWebURL;
    private AlertDialog dialogAddURL;
    // delete
    private AlertDialog dialogDeleteNote;

    // update
    private Note alreadyAvaiableNote;
    // remove image, url
    private ImageView imageRemoveWebURL, imageRemoveImage;

    private ImageView imageupdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        // anh xa
        txtdatetime = findViewById(R.id.txtdatetime);

        edtinputnotetitle = findViewById(R.id.edtinputnotetitle);
        edtinputsubtitle = findViewById(R.id.edtinputsubtitle);
        edtinpunote = findViewById(R.id.edtinpunote);

        imageback = findViewById(R.id.imageback);
        imagesave = findViewById(R.id.imagesave);
        imageNote = findViewById(R.id.imageNote);

        // remove URL, Image
        imageRemoveImage = findViewById(R.id.imageRemoveImage);
        imageRemoveWebURL = findViewById(R.id.imageRemoveWebURL);


        viewSubtitleIndicator = findViewById(R.id.viewsubtitle);

        txtWebURL = findViewById(R.id.txtWebURL);
        layoutWebURL = findViewById(R.id.layoutWebURL);

        imageupdate = findViewById(R.id.imageupdate);

        // su kien
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtdatetime.setText(
                new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );
        imagesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });
        selectedNoteColor = "#333333";// mau mac dinh
        selectImagePath = "";
        // view and update
        // nhan du dieu ben activigui sang
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            // tuc la khi nhan duoc
            alreadyAvaiableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();

        }

        // remove url and image
        // remove URL
        imageRemoveWebURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWebURL.setText(null);
                layoutWebURL.setVisibility(View.GONE);
            }
        });
        // remove IMAGE
        imageRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                imageRemoveImage.setVisibility(View.GONE);
                selectImagePath = "";
            }
        });
        iniMiscellaneous();
        setSubtitleIndicatorColor();

    }



    // view and update
    private void setViewOrUpdateNote() {
        edtinputnotetitle.setText(alreadyAvaiableNote.getTitle());
        edtinputsubtitle.setText(alreadyAvaiableNote.getSubtile());
        edtinpunote.setText(alreadyAvaiableNote.getNotetext());
        txtdatetime.setText(alreadyAvaiableNote.getDatetime());
        imageupdate.setVisibility(View.VISIBLE);


        if (alreadyAvaiableNote.getImgpath() != null && !alreadyAvaiableNote.getImgpath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvaiableNote.getImgpath()));
            imageNote.setVisibility(View.VISIBLE);
            // remove url and image
            imageRemoveImage.setVisibility(View.VISIBLE);
            selectImagePath = alreadyAvaiableNote.getImgpath();
        }

        if (alreadyAvaiableNote.getWeblink() != null && !alreadyAvaiableNote.getWeblink().trim().isEmpty()) {
            txtWebURL.setText(alreadyAvaiableNote.getWeblink());
            layoutWebURL.setVisibility(View.VISIBLE);
        }
        // xy ly update
        imageupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chuyen sang mot activiti khac
                Intent intent = new Intent(CreateNoteActivity.this,NewTodoActivity.class);
                startActivity(intent);
            }
        });

    }


    private void saveNotes() {
        if (edtinputsubtitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "note title can not be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtinputsubtitle.getText().toString().trim().isEmpty()
                && edtinputnotetitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "note title can be not empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note();
        note.setTitle(edtinputnotetitle.getText().toString());
        note.setSubtile(edtinputsubtitle.getText().toString());
        note.setNotetext(edtinpunote.getText().toString());
        note.setDatetime(txtdatetime.getText().toString());
        note.setColor(selectedNoteColor);
        note.setImgpath(selectImagePath);


        // add url
        // kiem tra layoutWebURL là có thể nhìn thấy hoặc không
        // neu có thể nhìn thấy thì điều đó có nghĩa là  url WEb được thêm vào vi tôi đã làm cho nó hiển thị  thêm
        // URL web từ hộp thoại  thêm URL
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            note.setWeblink(txtWebURL.getText().toString());
        }
        // view and update
        if (alreadyAvaiableNote != null) {
            // đang thiết lập id của id mới từ một ghi chú đã có sắn vì đã thiết lập onConflictStrategy để "THAY THẾ" trong NoteDao
            // điều này có nghĩa nếu id note đã có sắn trong cơ sở dữ liệu  sau đó sẽ được thay thế bằng ghi chú mới và ghi chú sẽ được update
            note.setId(alreadyAvaiableNote.getId());
        }
        @SuppressLint("StaticFieldLeak")
        class SavenoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                // luu vao trong csdl
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().Insertnote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SavenoteTask().execute();
    }

    private void iniMiscellaneous() {
        final LinearLayout layoutMiscellanous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellanous);

        layoutMiscellanous.findViewById(R.id.txtMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BottomSheetBehavior.STATE_EXPANDED : trang thái mở rộng hoang toàn dưới cùng , nơi có thể nhìn thấy toàn bộ trang dưới
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    // đặt trạng thái  của BottomSheetBehavior bằng phương thức setState
                    // Đặt trạng thái của trang tính dưới cùng. Trang tính dưới cùng sẽ chuyển sang trạng thái đó với hình ảnh động.
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    // BottomSheetBehavior.STATE_COLLAPSED : trang tính dưới cùng được thu gọn
                    // trang thái thu gọn này  là mặc định  và chỉ hiển thị một phần của bố cục  dọc ở phía dưới
                    // chiều cao được kiểm soát bằng thuộc tính behavior_peekHeight (mặc định là 0)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });
        //
        final ImageView imageColor1 = layoutMiscellanous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellanous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellanous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellanous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellanous.findViewById(R.id.imageColor5);


        layoutMiscellanous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });
        layoutMiscellanous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });
        layoutMiscellanous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });
        layoutMiscellanous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellanous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_baseline_done_24);
                setSubtitleIndicatorColor();
            }
        });
        // view and update
        if (alreadyAvaiableNote != null && alreadyAvaiableNote.getColor() != null
                && !alreadyAvaiableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvaiableNote.getColor()) {
                case "#FDBE3B":
                    layoutMiscellanous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    layoutMiscellanous.findViewById(R.id.viewColor3).performClick();
                case "#3A52Fc":
                    layoutMiscellanous.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000":
                    layoutMiscellanous.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }
        // add image
        layoutMiscellanous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Requesting runtime storage permission( yêu cầu quyền lưu trữ thời gian)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            CreateNoteActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        // add URL
        layoutMiscellanous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddURLDialog();
            }
        });

        // delete note
        if (alreadyAvaiableNote != null) {
            // ở đây nếu alreadyAvaoableNote rỗng thì điều đó có nghĩa
            // là người dùng xem hoặc cập nhật ghi chú  đã được thêm vào từ csdl và
            // do đó đang hiển thị tuy chon xoa note
            layoutMiscellanous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellanous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteNoteDialog();
                }
            });
        }
    }

    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    findViewById(R.id.layoutDeleteNoteContainer)

            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if(dialogDeleteNote.getWindow()!=null){
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.txtDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void,Void,Void>{
                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao()
                                    .deletenote(alreadyAvaiableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted",true);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                    new DeleteNoteTask().execute();
                }
            });
            // thoat
            view.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
        dialogDeleteNote.show();
    }

    // do mau do hoa
    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    // lua chon anh
    private void selectImage() {
        // gui Intent ra ben ngoai ung dung
        // gui yeu cau muon chon anh tu thu vien
        // Intent.ACTION_PICK
        //
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);

    }

    // handling permission Result
    // hoat dong cua nguoi dung cho phep hay khong
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // neu nguoi dung cho phep
                selectImage();

            } else {// nguoi dung ko cho phep
                Toast.makeText(this, "Permission Denied !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handling result for selected image
    // xu ly ket qua cho mot hinh anh da chon
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                //( muc dich cho nguoi dung chin anh
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        // muc dich su dung cho nguoi dung chon anh
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        // ( giải mã luồng dữ liệu trở lại thành Bitmap hình ảnh mà android không hiểu )
                        // lấy ảnh ở thẻ nhớ trong điện thoại
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);// anh View ( se hien thi view  ma bi an len man hinh chinh)

                        // remove url and imgage
                        imageRemoveImage.setVisibility(View.VISIBLE);

                        selectImagePath = getPathFromUri(selectedImageUri);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //
    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToNext();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    // dialog
    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    findViewById(R.id.layoutAddUrContainer)
            );
            builder.setView(view);
            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                // set mau cho dialog
//                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorNotecolor2)));
//                Toast.makeText(this, "thoi toi can khong kip", Toast.LENGTH_SHORT).show();
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.edtinputURL);
            inputURL.requestFocus(); // Để yêu cầu một Chế độ xem cụ thể lấy tiêu điểm va ban phím se xuat hien

            view.findViewById(R.id.txtAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputURL.getText().toString().trim().isEmpty()) {// kiem tea neu chuoi chong
                        Toast.makeText(CreateNoteActivity.this, "khong duoc de URL trong ", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {// kiem tra xem co dung la trang web ko
                        Toast.makeText(CreateNoteActivity.this, "sai dia chi trang web ", Toast.LENGTH_SHORT).show();

                    } else {
                        txtWebURL.setText(inputURL.getText().toString());
                        layoutWebURL.setVisibility(View.VISIBLE);
                        dialogAddURL.dismiss();// dùng để đóng Dialog
                    }
                }
            });
            view.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();// dùng để đóng Dialog
                }
            });
        }
        dialogAddURL.show();// hien thi
    }
}