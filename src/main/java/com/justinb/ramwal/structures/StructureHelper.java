package com.justinb.ramwal.structures;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StructureHelper
{
    public static Template readStructure(ResourceLocation resource)
    {
        String ns = resource.getNamespace();
        String nm = resource.getPath();
        return readStructure("/data/" + ns + "/structures/" + nm + ".nbt");
    }

    public static Template readStructure(File datapack, String path)
    {
        if (datapack.isDirectory())
        {
            return readStructure(datapack.toString() + "/" + path);
        }
        else if (datapack.isFile() && datapack.getName().endsWith(".zip"))
        {
            try
            {
                ZipFile zipFile = new ZipFile(datapack);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    long compressedSize = entry.getCompressedSize();
                    long normalSize = entry.getSize();
                    String type = entry.isDirectory() ? "DIR" : "FILE";

                    System.out.println(name);
                    System.out.format("\t %s - %d - %d\n", type, compressedSize, normalSize);
                }
                zipFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Template readStructure(String path)
    {
        try
        {
            InputStream inputstream = StructureHelper.class.getResourceAsStream(path);
            return readStructureFromStream(inputstream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static Template readStructureFromStream(InputStream stream) throws IOException
    {
        CompoundNBT nbttagcompound = CompressedStreamTools.readCompressed(stream);

        Template template = new Template();
        template.read(nbttagcompound);

        return template;
    }

    public static BlockPos offsetPos(BlockPos pos, Template structure, Rotation rotation, Mirror mirror)
    {
        BlockPos offset = Template.getTransformedPos(structure.getSize(), mirror, rotation, BlockPos.ZERO);
        return pos.add(-offset.getX() * 0.5, 0, -offset.getZ() * 0.5);
    }

    public static void placeCenteredBottom(ISeedReader world, BlockPos pos, Template structure, Rotation rotation, Mirror mirror, Random random)
    {
        placeCenteredBottom(world, pos, structure, rotation, mirror, makeBox(pos), random);
    }

    public static void placeCenteredBottom(ISeedReader world, BlockPos pos, Template structure, Rotation rotation, Mirror mirror, MutableBoundingBox bounds, Random random)
    {
        BlockPos offset = offsetPos(pos, structure, rotation, mirror);
        PlacementSettings placementData = new PlacementSettings().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
        structure.func_237144_a_(world, offset, placementData, random);
    }

    private static MutableBoundingBox makeBox(BlockPos pos)
    {
        int sx = ((pos.getX() >> 4) << 4) - 16;
        int sz = ((pos.getZ() >> 4) << 4) - 16;
        int ex = sx + 47;
        int ez = sz + 47;
        return MutableBoundingBox.createProper(sx, 0, sz, ex, 255, ez);
    }

    public static MutableBoundingBox getStructureBounds(BlockPos pos, Template structure, Rotation rotation, Mirror mirror)
    {
        BlockPos max = structure.getSize();
        BlockPos min = Template.getTransformedPos(structure.getSize(), mirror, rotation, BlockPos.ZERO);
        max = max.subtract(min);
        return new MutableBoundingBox(min.add(pos), max.add(pos));
    }

    public static MutableBoundingBox intersectBoxes(MutableBoundingBox box1, MutableBoundingBox box2)
    {
        int x1 = Math.max(box1.minX, box2.minX);
        int y1 = Math.max(box1.minY, box2.minY);
        int z1 = Math.max(box1.minZ, box2.minZ);

        int x2 = Math.min(box1.maxX, box2.maxX);
        int y2 = Math.min(box1.maxY, box2.maxY);
        int z2 = Math.min(box1.maxZ, box2.maxZ);

        return MutableBoundingBox.createProper(x1, y1, z1, x2, y2, z2);
    }

    public static void cover(MutableBoundingBox bounds)
    {
        BlockPos.Mutable mut = new BlockPos.Mutable();
        for (int x = bounds.minX; x <= bounds.maxX; x++)
        {
            mut.setX(x);
            for (int z = bounds.minZ; z <= bounds.maxZ; z++)
            {
                mut.setZ(z);
                for (int y = bounds.maxY; y >= bounds.minY; y--)
                {
                    mut.setY(y);
                }
            }
        }
    }
}
