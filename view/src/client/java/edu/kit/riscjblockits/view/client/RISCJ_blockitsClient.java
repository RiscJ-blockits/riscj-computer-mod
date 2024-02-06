package edu.kit.riscjblockits.view.client;

import edu.kit.riscjblockits.view.client.screens.ManualScreen;
import edu.kit.riscjblockits.view.client.screens.handled.ControlUnitScreen;
import edu.kit.riscjblockits.view.client.screens.handled.MemoryScreen;
import edu.kit.riscjblockits.view.client.screens.handled.ProgrammingScreen;
import edu.kit.riscjblockits.view.client.screens.handled.RegisterScreen;
import edu.kit.riscjblockits.view.client.screens.handled.SystemClockScreen;
import edu.kit.riscjblockits.view.client.screens.handled.TerminalScreen;
import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class RISCJ_blockitsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		HandledScreens.register(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER, ProgrammingScreen::new);
		HandledScreens.register(RISCJ_blockits.REGISTER_SCREEN_HANDLER, RegisterScreen::new);
		HandledScreens.register(RISCJ_blockits.CONTROL_UNIT_SCREEN_HANDLER, ControlUnitScreen::new);
		HandledScreens.register(RISCJ_blockits.MEMORY_BLOCK_SCREEN_HANDLER, MemoryScreen::new);
		HandledScreens.register(RISCJ_blockits.SYSTEM_CLOCK_SCREEN_HANDLER, SystemClockScreen::new);
		HandledScreens.register(RISCJ_blockits.TERMINAL_SCREEN_HANDLER, TerminalScreen::new);

		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.REGISTER_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.BUS_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.SYSTEM_CLOCK_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.MEMORY_BLOCK, RenderLayer.getTranslucent());

		registerManualScreenReceiver();

		ClientPlayNetworking.registerGlobalReceiver(
			NetworkingConstants.SYNC_BLOCK_ENTITY_DATA, (client, handler, buf, responseSender) -> {

				if (buf.readableBytes() == 0) {
					return;
				}

				// Read packet data on the event loop
				BlockPos target = buf.readBlockPos();
				NbtCompound nbt = buf.readNbt();
                if (client.world  == null) {
					return;
				}
                BlockEntity blockEntity = client.world.getBlockEntity(target);

				if (blockEntity == null) {
					//request Data again if send was not successful
					//ClientPlayNetworking.send(NetworkingConstants.REQUEST_DATA, PacketByteBufs.create().writeBlockPos(target));

					return;
				}
				blockEntity.readNbt(nbt);
		});

	}



	private void registerManualScreenReceiver() {
		ClientPlayNetworking.registerGlobalReceiver(
				NetworkingConstants.OPEN_MANUAL_SCREEN, (client, handler, buf, responseSender) -> {
					client.execute(() -> {
								client.setScreen(new ManualScreen(Text.translatable("manual.title")));
							}
					);
				});
	}
}