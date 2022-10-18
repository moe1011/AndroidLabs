package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTodoFromDb();

        ListView myList = findViewById(R.id.todoList);
        myList.setAdapter( myAdapter = new MyListAdapter());

        Button addButton = findViewById(R.id.addBtn);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        Switch urgentSwitch = findViewById(R.id.urgentSwitch);

        addButton.setOnClickListener(click -> {
            // Add to db
            ContentValues newRowValues = new ContentValues();

            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the NAME column:
            newRowValues.put(MyOpener.COL_TASK, editText.getText().toString());
            //put string email in the EMAIL column:
            newRowValues.put(MyOpener.COL_URGENT, urgentSwitch.isChecked() ? 1 : 0);

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            elements.add(new TodoItem(editText.getText().toString(), urgentSwitch.isChecked(), newId));
            editText.setText("");
            myAdapter.notifyDataSetChanged();



        });

        myList.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title))
                    .setMessage(getResources().getString(R.string.alert_message)+ pos)
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteTodo(elements.get(pos));

                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();


                    })
                    .setNegativeButton("No", (click, arg) ->{})
                    .create().show();

            return true;
        });

    }
    protected void loadTodoFromDb(){
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getReadableDatabase();

        String [] columns = {MyOpener.COL_ID, MyOpener.COL_TASK, MyOpener.COL_URGENT};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results);

        int taskColIndex = results.getColumnIndex(MyOpener.COL_TASK);
        int urgentColIndex = results.getColumnIndex(MyOpener.COL_URGENT);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:

        while(results.moveToNext())
        {
            String task = results.getString(taskColIndex);
            int urgent = results.getInt(urgentColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            elements.add(new TodoItem(task, urgent == 1 ? true : false, id));
        }

    }

    protected void deleteTodo(TodoItem todo)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(todo.getId())});
    }

    protected void printCursor(Cursor c){
        System.out.println("Database version: "+ db.getVersion());
        System.out.println("Column count: "+ c.getColumnCount());
        System.out.println("Column names: "+ Arrays.toString(c.getColumnNames()));
        System.out.println("Number of results: "+ c.getCount());

        int taskColIndex = c.getColumnIndex(MyOpener.COL_TASK);
        int urgentColIndex = c.getColumnIndex(MyOpener.COL_URGENT);
        int idColIndex = c.getColumnIndex(MyOpener.COL_ID);
        ArrayList<String> results = new ArrayList<>();

        while(c.moveToNext())
        {
            String task = c.getString(taskColIndex);
            int urgent = c.getInt(urgentColIndex);
            long id = c.getLong(idColIndex);

            //add the new Contact to the array list:
            results.add(String.format("{%s | %b | %d}", task, urgent == 1 ? true : false, id));

        }
        System.out.println("Results: "+ results);
        c.moveToPosition(-1);
    }


    private class TodoItem {
        private String todo;
        private boolean isUrgent;
        private Long id;

        public TodoItem(String todo, boolean isUrgent, Long id) {
            this.todo = todo;
            this.isUrgent = isUrgent;
            this.id = id;
        }

        public Long getId() {
            return id;
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