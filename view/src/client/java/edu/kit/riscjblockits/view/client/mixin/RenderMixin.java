package edu.kit.riscjblockits.view.client.mixin;

import edu.kit.riscjblockits.view.client.renderlistener.GoggleUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class RenderMixin {

    private static GoggleUI gui;

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