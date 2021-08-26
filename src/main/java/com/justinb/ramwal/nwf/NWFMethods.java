package com.justinb.ramwal.nwf;

public enum NWFMethods {
    RUN(0),
    STORE(1),
    IF(2);

    private int id;

    NWFMethods(int i) {
        id = i;
    }

    public int get() {
        return id;
    }

    public static int[] toIntArray() {
        return toIntArray(NWFMethods.values());
    }

    public static int[] toIntArray(NWFMethods[] methods) {
        int size = methods.length, i = 0;

        int[] ar = new int[size];

        for (NWFMethods m : methods)
            ar[i++] = m.get();

        return ar;
    }
}
