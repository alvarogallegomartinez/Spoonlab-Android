package com.example.spoonlab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// TODO: Terminar de hacer el recycler view

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    List<Recipe> recipeList;

    ViewGroup parent;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_preview, parent,false);

        this.parent = parent;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = recipeList.get(position).getTitle();
        //String description = recipeList.get(position).getDescription();

        holder.title.setText(title);

        // Cuando pulsamos el layout de la receta nos abre un desplegable de la receta en grande
        holder.recipePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecipe(v, position);
            }
        });
        /*holder.description.setText(description);

        ArrayList<String> ingredients = recipeList.get(position).getIngredients();

        // Creo que de este modo ya funcionaría el añadir de forma dinámica los ingredientes
        for(String ingredient : ingredients) {
            View checkIngredient = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_view, null, false);

            CheckBox ingredientCheckBox = checkIngredient.findViewById(R.id.Ingredient);
            ingredientCheckBox.setText(ingredient);

            holder.ingredientsLayout.addView(checkIngredient);
        }*/
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /*TextView title, description;
        LinearLayout ingredientsLayout;*/

        TextView title;
        RelativeLayout recipePreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.recipe_name);
            recipePreview = itemView.findViewById(R.id.recipe_preview);
            /*description = itemView.findViewById(R.id.description);

            ingredientsLayout = itemView.findViewById(R.id.Ingredients);*/
        }
    }

    private void showRecipe(View v, int position) {
        Dialog recipeDialog = new Dialog(v.getContext());
        recipeDialog.setContentView(R.layout.recipe_layout);

        TextView titleTxt, descriptionTxt;
        LinearLayout ingredientsLayout;

        String title = recipeList.get(position).getTitle();
        String description = recipeList.get(position).getDescription();
        ArrayList<String> ingredients = recipeList.get(position).getIngredients();

        titleTxt = recipeDialog.findViewById(R.id.title);
        descriptionTxt = recipeDialog.findViewById(R.id.description);
        ingredientsLayout = recipeDialog.findViewById(R.id.Ingredients);

        titleTxt.setText(title);
        descriptionTxt.setText(description);

        for(String ingredient : ingredients) {
            View checkIngredient = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_view, null, false);

            CheckBox ingredientCheckBox = checkIngredient.findViewById(R.id.Ingredient);
            ingredientCheckBox.setText(ingredient);

            ingredientsLayout.addView(checkIngredient);
        }

        recipeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recipeDialog.show();
    }

}
