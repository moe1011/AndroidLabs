package com.example.androidlabs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setting shared prefs
        SharedPreferences prefs = getSharedPreferences("Lab3", Context.MODE_PRIVATE);
        //Retrieving shared prefs data if it has been stored
        EditText editText = findViewById(R.id.editTextTextPersonName);
        editText.setText(prefs.getString("editText", ""));

        Button nextButton = findViewById(R.id.nextButton);
        Intent nextPage = new Intent(this, NameActivity.class);
        nextButton.setOnClickListener(click -> {
            nextPage.putExtra("name", editText.getText().toString());
            startActivityForResult(nextPage, 0);
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        //Storing pref data
        SharedPreferences prefs = getSharedPreferences("Lab3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        EditText editText = findViewById(R.id.editTextTextPersonName);
        editor.putString("editText", editText.getText().toString());
        editor.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            finish();
        }
    }
}

