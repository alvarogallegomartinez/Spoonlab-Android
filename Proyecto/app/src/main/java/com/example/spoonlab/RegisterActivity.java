package com.example.spoonlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spoonlab.exceptions.DifferentPasswordException;
import com.example.spoonlab.exceptions.EmptyTextException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button signUpBtn;
    private EditText emailEt, passwordEt, confirmPassEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        // Cogemos todos los elementos del layout
        signUpBtn = findViewById(R.id.sign_up_btn);
        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        confirmPassEt = findViewById(R.id.confirm_password_et);

        // Autenticación con firebase
        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateData();

                    String email = emailEt.getText().toString();
                    String password = passwordEt.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getApplicationContext(), "Se ha registrado el usuario satisfactoriamente", Toast.LENGTH_SHORT).show();

                                    // TODO: Guardar los usuarios también en la base de datos
                                    // TODO: Se podrían guardar datos adicionales como el nombre

                                    // Pasamos a la MainActivity
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.getStackTrace();

                                }
                            });

                } catch (EmptyTextException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.getStackTrace();
                }
                catch (DifferentPasswordException e) {
                    passwordEt.setError(e.getMessage());
                    confirmPassEt.setError(e.getMessage());
                    e.getStackTrace();
                }
                catch (Exception ignored) {

                }
            }
        });
    }

    private void validateData() throws Exception{
        if (emailEt.getText().toString().equals("") || passwordEt.getText().toString().equals("") || confirmPassEt.getText().toString().equals(""))
            throw new EmptyTextException();
        else if (!passwordEt.getText().toString().equals(confirmPassEt.getText().toString()))
            throw new DifferentPasswordException();
    }
}