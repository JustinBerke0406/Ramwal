package com.justinb.ramwal.structures.pieces;

import com.justinb.ramwal.structures.ModStructurePieces;
import com.justinb.ramwal.structures.StructureHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IglooPieces;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class NBTPiece extends TemplateStructurePiece
{
    private ResourceLocation structureID;
    private Rotation rotation;
    private Mirror mirror;
    private Template structure;
    private BlockPos pos;

    public NBTPiece(TemplateManager templateManager, ResourceLocation structureID, Template structure, BlockPos pos, Random random)
    {
        super(ModStructurePieces.NBT_PIECE, 0);
        this.structureID = structureID;
        this.structure = structure;
        this.rotation = Rotation.randomRotation(random);
        //BlockPos blockpos = NBTPiece..get(p_i49313_2_);
        this.templatePosition = pos;
        this.mirror = Mirror.values()[random.nextInt(3)];
        this.pos = StructureHelper.offsetPos(pos, structure, rotation, mirror);
        makeBoundingBox();
        templateSetup(templateManager);
    }

    public NBTPiece(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(ModStructurePieces.NBT_PIECE, nbt);

        structureID = new ResourceLocation(nbt.getString("Template"));
        rotation = Rotation.values()[nbt.getInt("rotation")];
        mirror = Mirror.values()[nbt.getInt("mirror")];
        pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
        structure = StructureHelper.readStructure(structureID);
        makeBoundingBox();
        templateSetup(templateManager);
    }

    @Override
    protected void readAdditional(CompoundNBT tagCompound)
    {
        tagCompound.putString("Template", structureID.toString());
        tagCompound.putInt("rotation", rotation.ordinal());
        tagCompound.putInt("mirror", mirror.ordinal());
        tagCompound.put("pos", NBTUtil.writeBlockPos(pos));
    }

    private void makeBoundingBox()
    {
        this.boundingBox = StructureHelper.getStructureBounds(pos, structure, rotation, mirror);
    }

    protected void setup(Template templateIn, BlockPos pos, PlacementSettings settings) {
        this.template = templateIn;
        this.setCoordBaseMode(Direction.NORTH);
        this.templatePosition = pos;
        this.placeSettings = settings;
    }

    private void templateSetup(TemplateManager p_207614_1_) {
        MutableBoundingBox bounds = this.getBoundingBox();
        bounds.maxY = this.boundingBox.maxY;
        bounds.minY = this.boundingBox.minY;

        Template template = p_207614_1_.getTemplateDefaulted(this.structureID);
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(mirror).setBoundingBox(bounds).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, placementsettings);
    }

    @Override
    public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator chunkGenerator,
                                  Random random, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos)
    {
        MutableBoundingBox bounds = new MutableBoundingBox(box);
        bounds.maxY = this.boundingBox.maxY;
        bounds.minY = this.boundingBox.minY;
        PlacementSettings placementData = new PlacementSettings().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);;
        boolean flag = super.func_230383_a_(world, manager, chunkGenerator, random, box, chunkPos, blockPos);

        //structure.func_237152_b_(world, pos, placementData, random);

        return flag;
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {

    }
}
