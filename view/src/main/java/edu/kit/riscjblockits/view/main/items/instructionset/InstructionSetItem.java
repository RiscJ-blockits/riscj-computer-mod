package edu.kit.riscjblockits.view.main.items.instructionset;

import edu.kit.riscjblockits.view.main.NetworkingConstants;
import edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingBlockEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_ITEM;
import static edu.kit.riscjblockits.model.data.DataConstants.PROGRAMMING_BLOCK_CODE;
import static edu.kit.riscjblockits.view.main.blocks.mod.programming.ProgrammingBlockEntity.CHUNK_SIZE;

/**
 * This class defines the instruction set item in the game.
 * In its nbt data is a json in which the instruction set is defined.
 */
public class InstructionSetItem extends Item {

    /**
     * The default instruction set json.
     */
    private final String defaultInstructionSetJson;
    private String text = "";

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

        ServerPlayNetworking.registerGlobalReceiver(        //receiver when the player edits the item
            NetworkingConstants.SYNC_IST_INPUT, (server, player, handler, buf, responseSender) -> {
                NbtCompound nbt = buf.readNbt();
                String hand = buf.readString();
                Hand currentHand;
                if (hand.equals("MAIN_HAND")) {
                    currentHand = Hand.MAIN_HAND;
                } else {
                    currentHand = Hand.OFF_HAND;
                }
                server.execute(() -> player.getStackInHand(currentHand).setNbt(nbt));
            });
        //--------------------------------------------------------------------------------------------------------------
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_IST_TEXT_C2S,
            (server, player, handler, buf, responseSender) -> server.execute(() -> {
                NbtCompound nbt = buf.readNbt();
                assert nbt != null;
                NbtCompound chunkContainer = nbt.getCompound(PROGRAMMING_BLOCK_CODE);
                int chunkIndex = chunkContainer.getInt("chunkIndex");
                String chunkCode = chunkContainer.getString("chunkData");
                server.execute(() -> {
                    if (chunkIndex == 0) {
                        ((InstructionSetItem) player.getMainHandStack().getItem()).setText("");
                    }
                    ((InstructionSetItem) player.getMainHandStack().getItem()).setText(chunkCode);
                });

                if (chunkCode.length() < CHUNK_SIZE) {
                    return;
                }
                responseSender.sendPacket(NetworkingConstants.SYNC_IST_TEXT_CONFIRMATION_C2S, new PacketByteBuf(
                    Unpooled.buffer())
                    .writeInt(chunkIndex + 1));
            }));
        ServerPlayNetworking.unregisterGlobalReceiver(NetworkingConstants.SYNC_IST_TEXT_CONFIRMATION_S2C);
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_IST_TEXT_CONFIRMATION_S2C,
            (server, player, handler, buf, responseSender) -> server.execute(() -> {
                int requestedChunk = buf.readInt();
                server.execute(() -> {
                    String tempText = ((InstructionSetItem) player.getMainHandStack().getItem()).getText();
                    if (requestedChunk * CHUNK_SIZE > tempText.length()) {
                        return;
                    }
                    ((InstructionSetItem) player.getMainHandStack().getItem()).sendChunk(player, requestedChunk, tempText);
                });
            }));
    }

    /**
     * If for some reason the nbt data is not set, the default instruction set json is set.
     * @param stack the item stack of this item.
     * @param world the world the item is in.
     * @param entity the entity the item is in.
     * @param slot the slot the item is in.
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
     * @param world the Minecraft world the item was used in
     * @param user the player who used the item
     * @param hand the hand used
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

    private void sendChunk(ServerPlayerEntity player, int chunkIndex, String text) {
        NbtCompound nbt = new NbtCompound();

        int start = chunkIndex * CHUNK_SIZE;
        int end = Math.min((chunkIndex + 1) * CHUNK_SIZE, text.length());
        // can't send as there is no more text
        if (start >= end || start >= text.length()) {
            return;
        }
        nbt.putInt("chunkIndex", chunkIndex);
        nbt.putString("chunkData", text.substring(start, end));

        NbtCompound container = new NbtCompound();
        container.put(PROGRAMMING_BLOCK_CODE, nbt);
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeNbt(container);
        ServerPlayNetworking.send(player, NetworkingConstants.SYNC_IST_TEXT_S2C, packet);
    }

    public void setText(String text) {
        if (text == null) {
            this.text = text;
        } else {
            this.text = this.text + text;
        }
    }

    public String getText() {
        return text;
    }

}
