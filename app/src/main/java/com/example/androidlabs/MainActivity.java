package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_linear);
//        setContentView(R.layout.activity_main_grid);
        setContentView(R.layout.activity_main_constraint);

        //Get Edit Text Value
        EditText editText = findViewById(R.id.editText);

        //Press Me Button
        final Button pressMeBtn = findViewById(R.id.pressMeBtn);
        //Text View
        TextView textView = findViewById(R.id.textView);
        //Set Text View Value to the one stored in Edit Text
        pressMeBtn.setOnClickListener( (click) -> {
            textView.setText(editText.getText());
            //Toast
            String toastMsg = getResources().getString(R.string.toast_message);
            Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_LONG).show();
        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener( (cb, isChecked) -> {
            String status = "";
            String undoTxt = getResources().getString(R.string.snackbar_text_undo);
            if(isChecked == true){
                status = getResources().getString(R.string.snackbar_text_on);
            } else{
                status  = getResources().getString(R.string.snackbar_text_off);
            }
            Snackbar.make(cb, status, Snackbar.LENGTH_LONG)
                    .setAction(undoTxt, click->cb.setChecked(!isChecked))
                    .show();

        });


    }
}