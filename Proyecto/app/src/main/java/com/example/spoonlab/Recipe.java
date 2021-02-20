package com.example.spoonlab;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Recipe {
    private String title;
    private String description;
    // TODO: Crear lista de ingredientes
    private ArrayList<String> ingredients;

    public Recipe(String title, String description) {
        this.title = title;
        this. description = description;
        ingredients = new ArrayList<>();
    }

    public Recipe() {
        this("", "");
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
