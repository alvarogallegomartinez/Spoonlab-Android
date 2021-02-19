package com.example.spoonlab;

import android.content.Intent;
import android.os.Bundle;

import com.example.spoonlab.exceptions.DifferentPasswordException;
import com.example.spoonlab.exceptions.EmptyTextException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRecipe extends AppCompatActivity {

    private EditText name, ingredients, description;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

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
                    Toast.makeText(getApplicationContext(), "Recipe created successfully!", Toast.LENGTH_SHORT).show();

                    Recipe newRecipe = obtainRecipe();
                    addRecipeToUser(newRecipe);

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

    private void addRecipeToUser(Recipe recipe) {
        LoginActivity.currentUser.addRecipe(recipe);

        // Autenticación con firebase
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid()).child("Recipes");

        myRef.push().setValue(recipe);
    }

    private Recipe obtainRecipe() {
        String recipeName = name.getText().toString();
        String recipeDescription = description.getText().toString();

        // Obtener cada ingrediente del conjunto del texto que introducimos
        String recipeIngredients = ingredients.getText().toString();
        String[] splitIngredients = recipeIngredients.split("\n");

        Recipe newRecipe = new Recipe(recipeName, recipeDescription);

        for(String ingredient : splitIngredients) {
            System.out.println(ingredient);
            newRecipe.addIngredient(ingredient);
        }

        return newRecipe;
    }
}