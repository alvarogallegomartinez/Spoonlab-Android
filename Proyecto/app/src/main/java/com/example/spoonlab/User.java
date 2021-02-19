package com.example.spoonlab;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*
* Almacenar los datos del usuario
* Por ahora solo tendrá username y email pero es escalable para almacenar otros datos
* TODO: Esta clase deberá almacenar los objetos receta
* */
public class User {

    private String username;
    private String email;
    // TODO: Atributo lista que sean las recetas del usuario
    private LinkedList<Recipe> recipes;

    public User() {
        this("", "");
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.recipes = new LinkedList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    public LinkedList<Recipe> getRecipes() {
        return recipes;
    }

    // TODO: Estos métodos deben ser más complejos y deberían encapsular la modificación del mail y username en la base de datos también
    // TODO: Podrían enviar excepciones
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
