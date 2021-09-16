package com.justinb.ramwal.blocks;

import com.justinb.ramwal.init.LemonInit;

public class LemonSpawnerIIBlock extends AbstractLemonSpawnerBlock {
    public LemonSpawnerIIBlock(Properties properties) {
        super(properties);

        toDispense = () -> LemonInit.PINK_LEMON.get();
    }
}
