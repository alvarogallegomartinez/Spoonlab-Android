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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button signUpBtn;
    private EditText emailEt, passwordEt, confirmPassEt, usernameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        // Cogemos todos los elementos del layout
        signUpBtn = findViewById(R.id.sign_up_btn);
        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        confirmPassEt = findViewById(R.id.confirm_password_et);
        usernameEt = findViewById(R.id.username_et);

        // Autenticación con firebase
        mAuth = FirebaseAuth.getInstance();
        // Base de datos de firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateData();

                    String email = emailEt.getText().toString();
                    String password = passwordEt.getText().toString();
                    String username = usernameEt.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getApplicationContext(), "Se ha registrado el usuario satisfactoriamente", Toast.LENGTH_SHORT).show();

                                    //Guardamos al usuario en la base de datos para almacenar datos adicionales como el username
                                    addUserToDataBase(authResult.getUser(), username, email);

                                    // Pasamos a la MainActivity
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
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

    private void validateData() throws Exception {
        if (emailEt.getText().toString().equals("") || passwordEt.getText().toString().equals("") || confirmPassEt.getText().toString().equals("") || usernameEt.getText().toString().equals(""))
            throw new EmptyTextException();
        else if (!passwordEt.getText().toString().equals(confirmPassEt.getText().toString()))
            throw new DifferentPasswordException();
    }

    private void addUserToDataBase(FirebaseUser newUser, String username, String email){
        User user = new User(username, email);
        String userId = newUser.getUid();

        myRef.child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Éxito al introducir al usuario en la base de datos, éxito al registrar", Toast.LENGTH_LONG).show();
                        LoginActivity.currentUser = user;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al introducir al usuario en la base de datos, exito al registrar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}