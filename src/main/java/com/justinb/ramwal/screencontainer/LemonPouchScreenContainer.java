package com.justinb.ramwal.screencontainer;

import com.justinb.ramwal.containers.LemonPouchContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class LemonPouchScreenContainer extends ContainerScreen<LemonPouchContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("ramwal", "textures/gui/lemonpouch_bg.png");

    public LemonPouchScreenContainer(LemonPouchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        xSize = 176;
        ySize = 207;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
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
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        // draw the label for the player inventory slots
        //this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(),                  ///    this.font.drawString
                //PLAYER_INV_LABEL_XPOS, PLAYER_INV_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void renderHoveredTooltip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        assert this.minecraft != null;
        assert this.minecraft.player != null;

        if (!this.minecraft.player.inventory.getItemStack().isEmpty()) return;  // no tooltip if the player is dragging something

        // If hoveringText is not empty draw the hovering text.  Otherwise, use vanilla to render tooltip for the slots
        super.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
