package com.example.spoonlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.spoonlab.ui.home.HomeFragment;

public class LoginActivity extends AppCompatActivity {
    public Button sign_in_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_in_btn=findViewById(R.id.sign_in_btn);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });
    }
}