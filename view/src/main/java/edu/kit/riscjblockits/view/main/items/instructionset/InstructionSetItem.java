package edu.kit.riscjblockits.view.main.items.instructionset;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * This class defines the instruction set item in the game.
 */
public class InstructionSetItem extends Item {

    /**
     * The default instruction set json.
     */
    private final String defaultInstructionSetJson;

    /**
     * Creates a new instruction set item with the given settings.
     * @param settings The settings for the item as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     * @param inputStream The input stream to read the default instruction set json from.
     */
    public InstructionSetItem(Settings settings, InputStream inputStream) {
        super(settings);
        defaultInstructionSetJson = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.get("riscj_blockits.instructionSet") == null) {
            nbt.putString("riscj_blockits.instructionSet", defaultInstructionSetJson);
            stack.setNbt(nbt);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
