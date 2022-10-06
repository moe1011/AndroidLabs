package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TodoItem> elements = new ArrayList<>();
    private MyListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView myList = findViewById(R.id.todoList);
        myList.setAdapter( myAdapter = new MyListAdapter());

        Button addButton = findViewById(R.id.addBtn);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        Switch urgentSwitch = findViewById(R.id.urgentSwitch);

        addButton.setOnClickListener(click -> {
            elements.add(new TodoItem(editText.getText().toString(), urgentSwitch.isChecked()));
            editText.setText("");
            myAdapter.notifyDataSetChanged();
        });

        myList.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title))
                    .setMessage(getResources().getString(R.string.alert_message)+ pos)
                    .setPositiveButton("Yes", (click, arg) -> {
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) ->{})
                    .create().show();

            return true;
        });


    }

    private class TodoItem {
        private String todo;
        private boolean isUrgent;

        public TodoItem(String todo, boolean isUrgent) {
            this.todo = todo;
            this.isUrgent = isUrgent;
        }

        public String getTodo() {
            return todo;
        }

        public boolean isUrgent() {
            return isUrgent;
        }
    }

    private class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public TodoItem getItem(int pos) {
            return elements.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            View newView = view;
            LayoutInflater inflater = getLayoutInflater();
            Switch urgentSwitch = findViewById(R.id.urgentSwitch);

            //make a new row:
            if(newView == null) {
                newView = inflater.inflate(R.layout.todo_layout, parent, false);
            }
            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.todoText);
            tView.setText( getItem(pos).getTodo());


            if(getItem(pos).isUrgent()){
                newView.setBackgroundColor(Color.RED);
                tView.setTextColor(Color.WHITE);
            } else {
                newView.setBackgroundColor(Color.WHITE);
                tView.setTextColor(Color.BLACK);
            }

            //return it to be put in the table
            return newView;
        }
    }

}