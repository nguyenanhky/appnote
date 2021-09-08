package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appnotes.modelnotification.MyTodo;
import com.example.appnotes.modelnotification.Todo_Adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatLich extends AppCompatActivity {
    private FloatingActionButton fab;
    // database :
    private FirebaseDatabase database;
    // de no doc tung cai bang ghi ra :
    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    List<MyTodo> myTodos;
    private Todo_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich);

        recyclerView=findViewById(R.id.todo_list_view);
        fab=findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // khoi tao firebase
        database = FirebaseDatabase.getInstance();
        // lay tung thanh phan  trong bay ghi
        myRef = database.getReference().child("mytodo");
        // neu ma chua co thi tao motj cai colection( tao ban ghi )
        // lang ghe su kien khi co su thay doi
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myTodos = new ArrayList<MyTodo>();
                for(DataSnapshot item: snapshot.getChildren())
                {
                    MyTodo myTodo = item.getValue(MyTodo.class);
                    myTodos.add(myTodo);
                }
                adapter = new Todo_Adapter(DatLich.this,
                        myTodos);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatLich.this,
                        NewTodoActivity.class);
                startActivity(intent);
            }
        });
    }

}