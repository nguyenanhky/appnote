package com.example.appnotes.modelnotification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appnotes.EditTodoActivity;
import com.example.appnotes.R;

import java.util.List;


public class Todo_Adapter extends RecyclerView.Adapter<Todo_Adapter.TodoViewHolder> {
    Context mContext;
    List<MyTodo> myTodoList;
    public Todo_Adapter(Context mContext, List<MyTodo> myTodoList) {
        this.mContext = mContext;
        this.myTodoList = myTodoList;
    }
    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_todo,parent,false);
        TodoViewHolder todoViewHolder=new TodoViewHolder(view);
        return todoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder,
                                 final int position) {
        holder.title.setText(myTodoList.get(position).getTitle());
        holder.desc.setText(myTodoList.get(position).getDescribe());
        holder.date.setText(myTodoList.get(position).getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, EditTodoActivity.class);
                intent.putExtra("todo",myTodoList.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTodoList.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titletodo);
            desc =  itemView.findViewById(R.id.desctodo);
            date =  itemView.findViewById(R.id.datetodo);

        }
    }
}
