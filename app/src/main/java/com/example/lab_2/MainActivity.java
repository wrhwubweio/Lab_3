package com.example.lab_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Button input;
    private TextView textView;
    private EditText editText;
    private String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraint_layout);
        Button input = (Button) findViewById(R.id.ok);
        TextView textView = (TextView) findViewById(R.id.outText);
        EditText editText = (EditText) findViewById(R.id.editText);
        //textView.setText(editText.getText());
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("text",  String.valueOf(editText.getText()));
                startActivity(intent);
            }
        });
    }
}