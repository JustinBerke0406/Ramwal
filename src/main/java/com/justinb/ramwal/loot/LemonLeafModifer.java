package com.justinb.ramwal.loot;

import com.google.gson.JsonObject;
import com.justinb.ramwal.init.ItemInit;
import com.justinb.ramwal.init.LemonInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class LemonLeafModifer extends LootModifier {

    protected LemonLeafModifer(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(LemonInit.LEMON.get(), new Random().nextInt(3)));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LemonLeafModifer> {

        @Override
        public LemonLeafModifer read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new LemonLeafModifer(conditionsIn);
        }

        @Override
        public JsonObject write(LemonLeafModifer instance) {
            JsonObject json = makeConditions(instance.conditions);
            return json;
        }
    }

}
