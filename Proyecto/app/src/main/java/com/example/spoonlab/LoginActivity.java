package com.example.spoonlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spoonlab.exceptions.DifferentPasswordException;
import com.example.spoonlab.exceptions.EmptyTextException;
import com.example.spoonlab.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button sign_in_btn;
    private TextView create_account;

    private EditText emailEt, passwordEt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        sign_in_btn=findViewById(R.id.sign_in_btn);
        create_account=findViewById(R.id.create_account);

        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);

        // Autenticación con firebase
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Si el usuario ya esta logeado podemos iniciar directamente el MainActivity
        // TODO: Hacer esto de forma algo más elegante (Welcome + username)
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
            changeActivity();

            // Esto hace que no podamos volver a la pantalla de login
            // TODO: Añadir un botón para hacer log out en el MainActivity
            finish();
        }

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para el login con firebase
                try {
                    validateData();

                    String email = emailEt.getText().toString();
                    String password = passwordEt.getText().toString();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    changeActivity();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.getStackTrace();
                                }
                            });

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.getStackTrace();
                }
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

    // Comprueba que no haya campos de texto vacíos
    private void validateData() throws Exception {
        if (emailEt.getText().toString().equals("") || passwordEt.getText().toString().equals(""))
            throw new EmptyTextException();
    }

    // Cambiar a la MainActivity
    // Lo pongo en una función para evitar copiar código
    private void changeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}