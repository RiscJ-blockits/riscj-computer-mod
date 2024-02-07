package edu.kit.riscjblockits.view.main.items.instructionset;

import edu.kit.riscjblockits.view.main.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;

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

        ServerPlayNetworking.registerGlobalReceiver(
            NetworkingConstants.SYNC_IST_INPUT, (server, player, handler, buf, responseSender) -> {
                NbtCompound nbt = buf.readNbt();
                String hand = buf.readString();
                server.execute(() -> {
                    //player.getStackInHand(
                });
            });
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.get(CONTROL_IST_ITEM) == null) {
            nbt.putString(CONTROL_IST_ITEM, defaultInstructionSetJson);
            stack.setNbt(nbt);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    /**
     * Opens the instruction set screen when the item is used.
     * @param world the Minecraft world the item was used in
     * @param user the player who used the item
     * @param hand the hand used
     * @return action result determined by minecraft
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return super.use(world, user, hand);
        }
        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send((ServerPlayerEntity) user, NetworkingConstants.OPEN_IST_SCREEN, buf);
        return super.use(world, user, hand);
    }

}
