package com.justinb.ramwal.structures;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.structures.pieces.LemonTreasurePiece;
import com.justinb.ramwal.structures.pieces.NBTPiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class ModStructurePieces {
    public static final IStructurePieceType LEMON_TREASURE_PIECE = LemonTreasurePiece::new;
    public static final IStructurePieceType NBT_PIECE = NBTPiece::new;

    public static void registerAllPieces() {
        registerStructurePiece(LEMON_TREASURE_PIECE, new ResourceLocation(Main.MODID, "lemon_treasure_piece"));
        registerStructurePiece(NBT_PIECE, new ResourceLocation(Main.MODID, "nbt_piece"));
    }

    static void registerStructurePiece(IStructurePieceType structurePiece, ResourceLocation rl) {
        Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
    }
}
