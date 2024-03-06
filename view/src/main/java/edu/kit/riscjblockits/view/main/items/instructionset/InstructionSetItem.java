package edu.kit.riscjblockits.view.main.items.instructionset;

import edu.kit.riscjblockits.view.main.NetworkingConstants;
import io.netty.buffer.Unpooled;
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

import static edu.kit.riscjblockits.model.data.DataConstants.CHUNK_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.CHUNK_INDEX;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.INSTRUCTION_SET;
import static edu.kit.riscjblockits.model.data.DataConstants.INSTRUCTION_SET_TEMP;
import static edu.kit.riscjblockits.model.data.DataConstants.TEMP_IST_NBT_TAG;
import static edu.kit.riscjblockits.view.main.NetworkingConstants.CHUNK_SIZE;

/**
 * This class defines the instruction set item in the game.
 * In its nbt data is a json in which the instruction set is defined.
 */
public class InstructionSetItem extends Item {

    /**
     * The default instruction set json.
     */
    private final String defaultInstructionSetJson;

    /**
     * Creates a new instruction set item with the given settings.
     *
     * @param settings    The settings for the item as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     * @param inputStream The input stream to read the default instruction set json from.
     */
    public InstructionSetItem(Settings settings, InputStream inputStream) {
        super(settings);
        defaultInstructionSetJson = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));
        //sync Ist Text
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_IST_TEXT_C2S,
            (server, player, handler, buf, responseSender) -> server.execute(() -> {
                NbtCompound nbt = buf.readNbt();
                assert nbt != null;
                NbtCompound chunkContainer = nbt.getCompound(INSTRUCTION_SET);
                int chunkIndex = chunkContainer.getInt(CHUNK_INDEX);
                String chunkCode = chunkContainer.getString(CHUNK_DATA);
                server.execute(() -> {
                    NbtCompound itemNbt = player.getMainHandStack().getOrCreateNbt();
                    String oldText = itemNbt.getString(CONTROL_IST_ITEM);
                    if (chunkIndex == 0) {
                        oldText = "";    //reset old text
                    }
                    oldText += chunkCode;
                    itemNbt.putString(CONTROL_IST_ITEM, oldText);    //reset old text
                    player.getMainHandStack().setNbt(itemNbt);
                });

                if (chunkCode.length() < CHUNK_SIZE) {
                    return;
                }
                responseSender.sendPacket(NetworkingConstants.SYNC_IST_TEXT_CONFIRMATION_C2S, new PacketByteBuf(
                    Unpooled.buffer())
                    .writeInt(chunkIndex + 1));
            }));
        //sync Temp Text
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_TEMP_TEXT_C2S,
            (server, player, handler, buf, responseSender) -> server.execute(() -> {
                NbtCompound nbt = buf.readNbt();
                assert nbt != null;
                NbtCompound chunkContainer = nbt.getCompound(INSTRUCTION_SET_TEMP);
                int chunkIndex = chunkContainer.getInt(CHUNK_INDEX);
                String chunkCode = chunkContainer.getString(CHUNK_DATA);
                server.execute(() -> {
                    NbtCompound itemNbt = player.getMainHandStack().getOrCreateNbt();
                    String oldText = itemNbt.getString(TEMP_IST_NBT_TAG);
                    if (chunkIndex == 0) {
                        oldText = "";    //reset old text
                    }
                    oldText += chunkCode;
                    itemNbt.putString(TEMP_IST_NBT_TAG, oldText);    //reset old text
                    player.getMainHandStack().setNbt(itemNbt);
                });

                if (chunkCode.length() < CHUNK_SIZE) {
                    return;
                }
                responseSender.sendPacket(NetworkingConstants.SYNC_TEMP_TEXT_CONFIRMATION_C2S, new PacketByteBuf(
                    Unpooled.buffer())
                    .writeInt(chunkIndex + 1));
            }));
    }

    /**
     * If for some reason the nbt data is not set, the default instruction set json is set.
     *
     * @param stack    the item stack of this item.
     * @param world    the world the item is in.
     * @param entity   the entity the item is in.
     * @param slot     the slot the item is in.
     * @param selected whether the item is selected.
     */
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
     *
     * @param world the Minecraft world the item was used in
     * @param user  the player who used the item
     * @param hand  the hand used
     * @return action result determined by minecraft
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient || hand == Hand.OFF_HAND) { //offhand is not allowed to simplify syncing
            return super.use(world, user, hand);
        }
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(hand.toString());
        ServerPlayNetworking.send((ServerPlayerEntity) user, NetworkingConstants.OPEN_IST_SCREEN, buf);
        return super.use(world, user, hand);
    }

}
