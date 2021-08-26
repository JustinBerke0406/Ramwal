package com.justinb.ramwal.screencontainer;

import com.justinb.ramwal.containers.ProgrammerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammerScreenContainer extends ContainerScreen<ProgrammerContainer> {
    private ProgrammerContainer container;

    public ProgrammerScreenContainer(ProgrammerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.container = screenContainer;

        xSize = 176;
        ySize = 207;
    }

    final static  int FONT_Y_SPACING = 10;
    final static  int PLAYER_INV_LABEL_XPOS = ProgrammerContainer.PLAYER_INVENTORY_XPOS;
    final static  int PLAYER_INV_LABEL_YPOS = ProgrammerContainer.PLAYER_INVENTORY_YPOS - FONT_Y_SPACING;


    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        // width and height are the size provided to the window when initialised after creation.
        // xSize, ySize are the expected size of the texture-? usually seems to be left as a default.
        // The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
        //  the available window
        // draw the background for this window
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        // draw the label for the top of the screen
        final int LABEL_XPOS = 5;
        final int LABEL_YPOS = 5;
        this.font.drawString(matrixStack, this.title.getString(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());     ///    this.font.drawString

        // draw the label for the player inventory slots
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(),                  ///    this.font.drawString
                PLAYER_INV_LABEL_XPOS, PLAYER_INV_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (!this.minecraft.player.inventory.getItemStack().isEmpty()) return;  // no tooltip if the player is dragging something

        List<ITextComponent> hoveringText = new ArrayList<ITextComponent>();

        // If hoveringText is not empty draw the hovering text.  Otherwise, use vanilla to render tooltip for the slots
        if (!hoveringText.isEmpty()){
            func_243308_b(matrixStack, hoveringText, mouseX, mouseY);  //renderToolTip
        } else {
            super.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        }
    }

    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
        return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("ramwal", "textures/gui/programmer_bg.png");
}
