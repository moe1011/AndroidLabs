package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Intent dataSent = getIntent();
        String nameSent = dataSent.getStringExtra("name");
        System.out.println(nameSent);

        TextView welcomeText = findViewById(R.id.welcome_text);
        if(!nameSent.equals("")) {
            welcomeText.setText(getResources().getString(R.string.welcome_text) + " " + nameSent + "!");
        }else{
            welcomeText.setText(getResources().getString(R.string.welcome_text)+"!");
        }

        Button wrongNameBtn = findViewById(R.id.wrongNameBtn);
        Button rightNameBtn = findViewById(R.id.rightNameBtn);

        wrongNameBtn.setOnClickListener(click -> {
            setResult(0, dataSent);
            finish();
        });
        rightNameBtn.setOnClickListener(click -> {
            setResult(1, dataSent);
            finish();
        });

    }
}