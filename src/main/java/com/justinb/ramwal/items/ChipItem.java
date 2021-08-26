package com.justinb.ramwal.items;

import com.justinb.ramwal.nwf.NWFMethods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class ChipItem extends Item {
    public ChipItem(Properties properties) {
        super(properties);
    }

    public ItemStack onEdit(ItemStack stack, NWFMethods... methodsIn) {
        CompoundNBT nbt;

        int[] comp;

        if (stack.hasTag()) {
            nbt = stack.getTag();

            if (stack.getTag().contains("ramwal:nwfmethods")) {
                ArrayList<Integer> toAdd = new ArrayList<>(toArrayList(nbt.getIntArray("ramwal:nwfmethods")));

                toAdd.addAll(toArrayList(NWFMethods.toIntArray(methodsIn)));

                if (toAdd.size() <= 0 || !toAdd.get(0).equals(NWFMethods.RUN.get())) toAdd.add(0, NWFMethods.RUN.get());

                comp = new int[toAdd.size()];

                int i = 0;

                for (int in : toAdd) comp[i++] = in;
            }
            else {
                ArrayList<NWFMethods> methods = new ArrayList<>(Arrays.asList(methodsIn));

                if (methods.size() <= 0 || !methods.get(0).equals(NWFMethods.RUN)) methods.add(0, NWFMethods.RUN);

                comp = NWFMethods.toIntArray(methods.toArray(new NWFMethods[0]));
            }
        }
        else {
            nbt = new CompoundNBT();

            ArrayList<NWFMethods> methods = new ArrayList<>(Arrays.asList(methodsIn));

            if (methods.size() <= 0 || !methods.get(0).equals(NWFMethods.RUN)) methods.add(0, NWFMethods.RUN);

            comp = NWFMethods.toIntArray(methods.toArray(new NWFMethods[0]));
        }

        nbt.putIntArray("ramwal:nwfmethods", comp);

        stack.setTag(nbt);

        return stack;
    }

    private ArrayList<Integer> toArrayList(int[] list) {
        ArrayList<Integer> array = new ArrayList<>();

        for (int item : list) array.add(item);

        return array;
    }
}
