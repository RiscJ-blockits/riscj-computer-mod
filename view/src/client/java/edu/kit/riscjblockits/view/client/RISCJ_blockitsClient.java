package edu.kit.riscjblockits.view.client;

import edu.kit.riscjblockits.view.client.screens.InstructionSetScreen;
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
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * The client-side entrypoint for the RISCJ_blockits mod.
 */
public class RISCJ_blockitsClient implements ClientModInitializer {

	/**
	 * The Server world exists bevor the client world, so we need to store synced data until the client world is created.
	 */
	 private static final Map<BlockPos, NbtCompound> syncQueue = new HashMap<>();

	/**
	 * This method is called on the client when the game is starting up.
	 */
	@Override
	public void onInitializeClient() {
		//This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//Register the screens
		HandledScreens.register(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER, ProgrammingScreen::new);
		HandledScreens.register(RISCJ_blockits.REGISTER_SCREEN_HANDLER, RegisterScreen::new);
		HandledScreens.register(RISCJ_blockits.CONTROL_UNIT_SCREEN_HANDLER, ControlUnitScreen::new);
		HandledScreens.register(RISCJ_blockits.MEMORY_BLOCK_SCREEN_HANDLER, MemoryScreen::new);
		HandledScreens.register(RISCJ_blockits.SYSTEM_CLOCK_SCREEN_HANDLER, SystemClockScreen::new);
		HandledScreens.register(RISCJ_blockits.TERMINAL_SCREEN_HANDLER, TerminalScreen::new);
		//
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.REGISTER_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.BUS_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.SYSTEM_CLOCK_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.MEMORY_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.ALU_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.REDSTONE_INPUT_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.REDSTONE_OUTPUT_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(RISCJ_blockits.WIRELESS_REGISTER_BLOCK, RenderLayer.getTranslucent());
		//Register the networking
		registerManualScreenReceiver();
		registerIstItemScreenReceiver();
		ClientPlayNetworking.registerGlobalReceiver(		//is called by screens who want the serer to send new data
			NetworkingConstants.SYNC_BLOCK_ENTITY_DATA, (client, handler, buf, responseSender) -> {
				if (buf.readableBytes() == 0) return;
				// Read packet data on the event loop
				BlockPos target = buf.readBlockPos();
				NbtCompound nbt = buf.readNbt();
                if (client.world  == null) {
					syncQueue.put(target, nbt);
					return;
				}
                BlockEntity blockEntity = client.world.getBlockEntity(target);
				if (blockEntity == null) {
					syncQueue.put(target, nbt);
					return;
				}
				blockEntity.readNbt(nbt);
		});
		ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, clientWorld) -> {
			//if an entity is loaded, we check if it is in the syncQueue and if so, we sync it
			BlockPos pos = blockEntity.getPos();
			if (syncQueue.containsKey(pos)) {
				blockEntity.readNbt(syncQueue.get(pos));
				syncQueue.remove(pos);
			}
		});
	}

	/**
	 * Opens the manual screen when a message is received from the server.
	 * A message is sent from the server when the player right-clicks on the manual item.
	 */
	private void registerManualScreenReceiver() {
		ClientPlayNetworking.registerGlobalReceiver(
			NetworkingConstants.OPEN_MANUAL_SCREEN, (client, handler, buf, responseSender) -> client.execute(() -> client.setScreen(new ManualScreen(Text.translatable("manual.title")))
                ));
	}

	/**
	 * Opens the edit screen for the instruction set item when a message is received from the server.
	 * A message is sent from the server when the player right-clicks on an editable instruction set item.
	 */
	private void registerIstItemScreenReceiver() {
		ClientPlayNetworking.registerGlobalReceiver(
			NetworkingConstants.OPEN_IST_SCREEN, (client, handler, buf, responseSender) -> client.execute(() -> client.setScreen(new InstructionSetScreen(Text.translatable("istItem.title"), buf.readString()))
            ));
	}

}
