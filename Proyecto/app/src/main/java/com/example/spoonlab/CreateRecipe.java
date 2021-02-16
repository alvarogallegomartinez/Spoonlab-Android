package com.example.spoonlab;

import android.content.Intent;
import android.os.Bundle;

import com.example.spoonlab.exceptions.DifferentPasswordException;
import com.example.spoonlab.exceptions.EmptyTextException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateRecipe extends AppCompatActivity {

    private EditText name, ingredients, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        description = findViewById(R.id.editSteps);
        ingredients = findViewById(R.id.editIngredients);
        name = findViewById(R.id.editName);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateData();

                    // Volver a la página principal
                    Intent intent = new Intent(CreateRecipe.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Recipe created successfully!", Toast.LENGTH_SHORT).show();
                    //TODO: AÑADIR la receta al USUARIO

                    finish();
                } catch (EmptyTextException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.getStackTrace();
                }
                catch (Exception ignored) {}
            }
        });
    }

    // Comprobar que no nos dejamos ningún campo vacío
    private void validateData() throws Exception {
        if (description.getText().toString().equals("") || ingredients.getText().toString().equals("") || name.getText().toString().equals(""))
            throw new EmptyTextException();
    }
}