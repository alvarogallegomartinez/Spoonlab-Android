package com.example.spoonlab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    FirebaseDatabase database;
    DatabaseReference myRef;

    ViewGroup parent;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_layout, parent,false);

        database = FirebaseDatabase.getInstance();
        this.parent = parent;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = recipeList.get(position).getTitle();
        String description = recipeList.get(position).getDescription();

        holder.title.setText(title);
        holder.description.setText(description);

        ArrayList<String> ingredients = recipeList.get(position).getIngredients();

        // Creo que de este modo ya funcionaría el añadir de forma dinámica los ingredientes
        // TODO: Hacer debug de esta parte
        for(String ingredient : ingredients) {
            View checkIngredient = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_view, null, false);

            CheckBox ingredientCheckBox = checkIngredient.findViewById(R.id.Ingredient);
            ingredientCheckBox.setText(ingredient);

            holder.ingredientsLayout.addView(checkIngredient);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        LinearLayout ingredientsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            ingredientsLayout = itemView.findViewById(R.id.Ingredients);
        }
    }

}
