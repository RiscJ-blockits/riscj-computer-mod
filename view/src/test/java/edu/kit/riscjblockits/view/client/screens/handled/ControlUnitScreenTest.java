package edu.kit.riscjblockits.view.client.screens.handled;

import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;

@ExtendWith(TestSetupClient.class)
class ControlUnitScreenTest {

    @Disabled
    @Test
    void fetchEntries() {
        ControlUnitScreenHandler controlUnitScreenHandler = new ControlUnitScreenHandler(0, mock(PlayerInventory.class), mock(ModBlockEntity.class));
        ControlUnitScreen controlUnitScreen = new ControlUnitScreen(controlUnitScreenHandler, mock(PlayerInventory.class), Text.literal("Test"));
    }

}