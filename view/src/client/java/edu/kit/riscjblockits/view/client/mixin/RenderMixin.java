package edu.kit.riscjblockits.view.client.mixin;

import edu.kit.riscjblockits.view.client.renderlistener.GoggleUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class will be injected into Minecraft's {@link InGameHud} class.
 * will create and call the {@link GoggleUI} class.
 */
@Mixin(InGameHud.class)
public abstract class RenderMixin {

    /**
     * The {@link GoggleUI} instance.
     */
    private static GoggleUI gui;

    /**
     * This method will be injected into the render method of the {@link InGameHud} class.
     * Therefor it will be called every rendering Frame.
     * Will call the {@link GoggleUI#onRenderGameOverlay(DrawContext)} method.
     * @param context the drawing context
     * @param f the delta
     * @param ci the callback info
     */
    @Inject(method="render", at=@At(
        value="INVOKE",
        target="Lnet/minecraft/client/gui/hud/DebugHud;shouldShowDebugHud()Z",
        opcode = Opcodes.GETFIELD, args = {"log=false"}))

    private void beforeRenderDebugScreen(DrawContext context, float f, CallbackInfo ci) {
        if (gui==null)
            gui=new GoggleUI();
        gui.onRenderGameOverlay(context);
    }
}