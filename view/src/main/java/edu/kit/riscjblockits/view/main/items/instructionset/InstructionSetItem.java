package edu.kit.riscjblockits.view.main.items.instructionset;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class InstructionSetItem extends Item {

    private final String defaultInstructionSetJson;

    public InstructionSetItem(Settings settings, InputStream inputStream) {
        super(settings);
    }
}
