package com.example.spoonlab;

import java.util.LinkedList;
import java.util.List;

public class Recipe {
    private String title;
    private String description;
    // TODO: Crear lista de ingredientes
    private List<String> ingredients;

    public Recipe(String title, String description) {
        this.title = title;
        this. description = description;
        ingredients = new LinkedList<>();
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
