package com.example.spoonlab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.content.FileProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRecipe extends AppCompatActivity {

    private static final String TAG = "UploadActivity";

    private EditText name, ingredients, description;
    private ImageButton camera, gallery;
    private ImageView image;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private Uri myUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        description = findViewById(R.id.editSteps);
        ingredients = findViewById(R.id.editIngredients);
        name = findViewById(R.id.editName);
        image = findViewById(R.id.uploadImage);

        camera = findViewById(R.id.camera_photo);
        gallery = findViewById(R.id.gallery_photo);

        checkFilePermissions();

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

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 0);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
                myUri = FileProvider.getUriForFile(
                        CreateRecipe.this,
                        "com.example.spoonlab.provider", //(use your app signature + ".provider" )
                        file);
                cInt.putExtra(MediaStore.EXTRA_OUTPUT, myUri);
                startActivityForResult(cInt,1);
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
            if(ingredient != null && !ingredient.trim().isEmpty())
                newRecipe.addIngredient(ingredient);
        }

        return newRecipe;
    }

    // Selección de imagenes por medio de la galeria o cámara

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(reqCode == 0) { //gallery
                try {
                    myUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(myUri);
                    if (reqCode == 0) {
                        image.setImageBitmap(getResizedBitmap(BitmapFactory.decodeStream(imageStream), 1000));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateRecipe.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            else if(reqCode == 1) { //camera
                InputStream is = null;
                try {
                    is = getContentResolver().openInputStream(myUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bp = BitmapFactory.decodeStream(is);
                image.setImageBitmap(getResizedBitmap(bp, 1000));
            }

        }else {
            Toast.makeText(CreateRecipe.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NewApi")
    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = CreateRecipe.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += CreateRecipe.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            permissionCheck += CreateRecipe.this.checkSelfPermission("Manifest.permission.CAMERA");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}