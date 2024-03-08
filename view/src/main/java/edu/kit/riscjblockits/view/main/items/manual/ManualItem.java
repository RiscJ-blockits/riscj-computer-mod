package edu.kit.riscjblockits.view.main.items.manual;

import edu.kit.riscjblockits.view.main.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * This class defines the manual item in the game.
 */
public class ManualItem extends Item {

    /**
     * Creates a new manual item with the given settings.
     * @param settings The settings for the item as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public ManualItem(Settings settings) {
        super(settings);
    }

    /**
     * Is called when the player right-clicks with the manual item in hand.
     * Opens the manual screen.
     * @param world The world in which the player is.
     * @param user The player that right-clicked.
     * @param hand The hand in which the player is holding the manual.
     * @return super.use(world, user, hand)
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return super.use(world, user, hand);
        }
        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send((ServerPlayerEntity) user, NetworkingConstants.OPEN_MANUAL_SCREEN, buf);
        return super.use(world, user, hand);
    }

}
