package edu.kit.riscjblockits.view.client;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import edu.kit.riscjblockits.view.client.screens.handled.ProgrammingScreen;

public class RISCJ_blockitsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		HandledScreens.register(RISCJ_blockits.PROGRAMMING_SCREEN_HANDLER, ProgrammingScreen::new);
	}
}