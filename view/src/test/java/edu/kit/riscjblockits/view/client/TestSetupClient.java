package edu.kit.riscjblockits.view.client;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class TestSetupClient implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    static void setUp() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
        RISCJ_blockitsClient riscJ_blockitsClient = new RISCJ_blockitsClient();
        riscJ_blockitsClient.onInitializeClient();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
            // before all-tests startup logic goes here
            setUp();
            //
            context.getRoot().getStore(GLOBAL).put("any unique name", this);
        }
    }

    @Override
    public void close() throws Throwable {
        //after all tests logic goes here
    }

}
