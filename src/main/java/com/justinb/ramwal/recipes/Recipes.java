package com.justinb.ramwal.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipes {
    public static final ResourceLocation location = new ResourceLocation("ramwal", "lemons/recipes.json");

    public static HashMap<Item, ArrayList<Recipe>> map;

    public static void registerAll() {
        JsonElement je = null;

        try {
            je = new Gson().fromJson(new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(location).getInputStream())), JsonElement.class);
        } catch (Exception e){
            e.printStackTrace();
        }

        assert je != null;

        JsonObject o = je.getAsJsonObject();

        for (Map.Entry<String, JsonElement> ent : o.entrySet()) {
            JsonArray ar = ent.getValue().getAsJsonArray();

            Item res = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ent.getKey()));

            for (int i = 0; i < ar.size(); i++) {
                JsonObject ob = ar.get(i).getAsJsonObject();
                JsonArray items = ob.get("items").getAsJsonArray();

                int cost = ob.get("cost").getAsInt();

                ArrayList<Item> al = new ArrayList<>();

                for (int b = 0; b < items.size(); b++)
                    al.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(items.get(b).getAsString())));

                Recipe.registerRecipe(cost, res, al.toArray(new Item[0]));
            }
        }

        map = Recipe.recipeMap();
    }
}
