package com.example.spoonlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spoonlab.ui.home.HomeFragment;

public class LoginActivity extends AppCompatActivity {
    public Button sign_in_btn;
    public TextView create_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_in_btn=findViewById(R.id.sign_in_btn);
        create_account=findViewById(R.id.create_account);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });
    }
}