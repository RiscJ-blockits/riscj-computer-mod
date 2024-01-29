package edu.kit.riscjblockits.view.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static edu.kit.riscjblockits.view.main.NetworkingConstants.SYNC_BLOCK_ENTITY_DATA;

@ExtendWith(TestSetupClient.class)
public class RISCJ_blockitsClientTest {

    @Test
    void onInitializeClient() {
        ArrayList<Identifier> list = new ArrayList<>(ClientPlayNetworking.getGlobalReceivers());
        assert list.contains(SYNC_BLOCK_ENTITY_DATA);
    }

}