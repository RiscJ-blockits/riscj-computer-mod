package edu.kit.riscjblockits.view.main;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class TestSetupMain implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!started) {
            started = true;
            //before all-tests startup logic goes here
            setUp();
            //
            context.getRoot().getStore(GLOBAL).put("any unique name", this);
        }
    }

    static void setUp() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
        RISCJ_blockits mod = new RISCJ_blockits();
        mod.onInitialize();
    }

    @Override
    public void close() {
        //after all tests logic goes here
    }

}
