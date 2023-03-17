package com.example.lab_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_fragment);
        TextView textView = (TextView) findViewById(R.id.outText);
        textView.setText(getIntent().getStringExtra("text"));
        Button input = (Button) findViewById(R.id.ok);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("text", textView.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}