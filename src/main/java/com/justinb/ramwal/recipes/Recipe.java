package com.justinb.ramwal.recipes;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Recipe {
    private static ArrayList<Recipe> recipes = new ArrayList<>();

    private ArrayList<Item> inputs;
    private Item result;
    private int cost;

    private Recipe(Item[] in, Item result, int cost) {
        this(new ArrayList<>(Arrays.asList(in)), result, cost);
    }

    private Recipe(ArrayList<Item> in, Item result, int cost) {
        inputs = in;
        this.result = result;
        this.cost = cost;
    }

    public static ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<Item> getInputs() {
        return inputs;
    }

    public Item getResult() {
        return result;
    }

    public static Recipe registerRecipe(int cost, Item result, Item... in) {
        Arrays.sort(in, Comparator.comparing(o -> o.getName().getString()));

        Recipe rep = getMatch(result, in);

        if (rep != null) return rep;

        rep = new Recipe(in, result, cost);

        recipes.add(rep);

        return rep;
    }

    public static HashMap<Item, ArrayList<Recipe>> recipeMap() {
        HashMap<Item, ArrayList<Recipe>> map = new HashMap<>();

        for (Recipe r : recipes) {
            if (!map.containsKey(r.getResult())) {
                ArrayList<Recipe> hold = new ArrayList<>();
                hold.add(r);

                map.put(r.getResult(), hold);
            }
            else map.get(r.getResult()).add(r);
        }

        return map;
    }

    public static Recipe getRecipe(Item... in) {
        Arrays.sort(in, Comparator.comparing(o -> o.getName().getString()));

        ArrayList<Item> input = new ArrayList<>(Arrays.asList(in));

        for (Recipe recipe : recipes) if (isListEqual(recipe.getInputs(), input)) return recipe;

        return null;
    }

    public static ArrayList<Recipe> getRecipeFrom(Item res) {
        ArrayList<Recipe> hold = new ArrayList<>();

        for (Recipe r : recipes) {
            if (r.getResult() == res) hold.add(r);
        }

        return hold;
    }

    public static Recipe getRecipe(ArrayList<Item> in) {
        return getRecipe(in.toArray(new Item[0]));
    }

    private static Recipe getMatch(Item result, Item[] in) {
        ArrayList<Item> input = new ArrayList<>(Arrays.asList(in));

        for (Recipe recipe : recipes) if (isListEqual(recipe.getInputs(), input) && result == recipe.getResult()) return recipe;

        return null;
    }

    private static boolean isListEqual(ArrayList<Item> a, ArrayList<Item> b) {
        if (a.size() != b.size()) return false;

        for (int i = 0; i < a.size(); i++) if (a.get(i) != b.get(i)) return false;

        return true;
    }
}
