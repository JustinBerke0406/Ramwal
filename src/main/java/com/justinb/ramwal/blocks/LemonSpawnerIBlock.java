package com.justinb.ramwal.blocks;

import com.justinb.ramwal.init.LemonInit;

public class LemonSpawnerIBlock extends AbstractLemonSpawnerBlock {
    public LemonSpawnerIBlock(Properties properties) {
        super(properties);

        toDispense = () -> LemonInit.LEMON.get();
    }
}
