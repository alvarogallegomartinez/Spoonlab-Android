package com.example.spoonlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button signUpBtn;
    private EditText usernameEt, passwordEt, confirmPassEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        // Cogemos todos los elementos del layout
        signUpBtn = findViewById(R.id.sign_up_btn);
        usernameEt = findViewById(R.id.username_et);
        passwordEt = findViewById(R.id.password_et);
        confirmPassEt = findViewById(R.id.confirm_password_et);

        // Autenticación con firebase
        // @TODO: Añadir el mail y la contraseña con la info del usuario y activar el registro con el botón
        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateData();

                    String username = usernameEt.getText().toString();
                    String password = passwordEt.getText().toString();

                    mAuth.createUserWithEmailAndPassword(username, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getApplicationContext(), "Se ha registrado el usuario satisfactoriamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Ha habido un error al registrar al usuario", Toast.LENGTH_SHORT).show();
                                }
                            });

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.getStackTrace();
                }
            }
        });


    }

    private void validateData() throws Exception{
        if (usernameEt.getText().toString().equals("") || passwordEt.getText().toString().equals("") || confirmPassEt.getText().toString().equals(""))
            throw new Exception("Todos los campos deben estar completos para registrar al usuario");
        else if (!passwordEt.getText().toString().equals(confirmPassEt.getText().toString()))
            throw new Exception("Ambos campos de la contraseña deben coincidir");
    }
}