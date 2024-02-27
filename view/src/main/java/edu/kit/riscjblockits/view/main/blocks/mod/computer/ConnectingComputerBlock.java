package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;

//ToDo javadoc
/**
 * Class to manage the BlockState of a BusBlock.
 * A {@link ComputerBlock} that can change its appearance depending on its neighbors.
 * Copied from {@link net.minecraft.block.ConnectingBlock}.
 */
public abstract class ConnectingComputerBlock extends ComputerBlock {
    /**
     * The property for the north side of the block.
     */
    public static final EnumProperty<Side> NORTH = EnumProperty.of("north", Side.class);
    /**
     * The property for the east side of the block.
     */
    public static final EnumProperty<Side> EAST = EnumProperty.of("east", Side.class);
    /**
     * The property for the south side of the block.
     */
    public static final EnumProperty<Side> SOUTH = EnumProperty.of("south", Side.class);
    /**
     * The property for the west side of the block.
     */
    public static final EnumProperty<Side> WEST = EnumProperty.of("west", Side.class);
    /**
     * The property for the upside of the block.
     */
    public static final EnumProperty<Side> UP = EnumProperty.of("up", Side.class);
    /**
     * The property for the downside of the block.
     */
    public static final EnumProperty<Side> DOWN = EnumProperty.of("down", Side.class);
    /**
     * The map of properties for each facing direction.
     */
    public static final Map<Direction, EnumProperty<Side>> FACING_PROPERTIES;
    /**
     * directions the Block is facing to.
     */
    private static final Direction[] FACINGS = Direction.values();
    /**
     * The map of shapes for each possible connection mask.
     */
    protected final VoxelShape[] facingsToShape;

    /**
     * Constructor for the ConnectingComputerBlock.
     * @param radius the radius of the blocks connections.
     */
    protected ConnectingComputerBlock(float radius) {
        super();
        this.facingsToShape = this.generateFacingsToShapeMap(radius);
    }

    /**
     * Constructor for the ConnectingComputerBlock.
     * @param radius the radius of the blocks connections.
     * @param settings the settings for the block.
     */
    protected ConnectingComputerBlock(float radius, AbstractBlock.Settings settings) {
        super(settings);
        this.facingsToShape = this.generateFacingsToShapeMap(radius);
    }

    private VoxelShape[] generateFacingsToShapeMap(float radius) {
        float f = 0.5F - radius;
        float g = 0.5F + radius;
        VoxelShape voxelShape = Block.createCuboidShape(
                (f * 16.0F), (f * 16.0F),
                (f * 16.0F), (g * 16.0F),
                (g * 16.0F), (g * 16.0F));
        VoxelShape[] voxelShapes = new VoxelShape[FACINGS.length];

        for (int i = 0; i < FACINGS.length; ++i) {
            Direction direction = FACINGS[i];
            voxelShapes[i] = VoxelShapes.cuboid(
                    0.5 + Math.min((-radius),
                    (double)direction.getOffsetX() * 0.5),
                    0.5 + Math.min((-radius),(double)direction.getOffsetY() * 0.5),
                    0.5 + Math.min((-radius), (double)direction.getOffsetZ() * 0.5),
                    0.5 + Math.max(radius, (double)direction.getOffsetX() * 0.5),
                    0.5 + Math.max(radius, (double)direction.getOffsetY() * 0.5),
                    0.5 + Math.max(radius, (double)direction.getOffsetZ() * 0.5));
        }

        VoxelShape[] voxelShapes2 = new VoxelShape[64];

        for(int j = 0; j < 64; ++j) {
            VoxelShape voxelShape2 = voxelShape;

            for(int k = 0; k < FACINGS.length; ++k) {
                if ((j & 1 << k) != 0) {
                    voxelShape2 = VoxelShapes.union(voxelShape2, voxelShapes[k]);
                }
            }

            voxelShapes2[j] = voxelShape2;
        }

        return voxelShapes2;
    }

    /**
     * Returns the BlockState of the Block.
     * @param state the current BlockState.
     * @param world the world the block is in.
     * @param pos the position of the block.
     * @return whether the block is transparent or not.
     */
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    /**
     * Returns the VoxelShape of the Block.
     * @param state the current BlockState.
     * @param world the world the block is in.
     * @param pos the position of the block.
     * @param context the context of the shape.
     * @return the outline VoxelShape of the block.
     */
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.facingsToShape[this.getConnectionMask(state)];
    }

    /**
     * Returns the connection mask of the BlockState.
     * @param state the current BlockState.
     * @return the connection mask of the block.
     */
    protected int getConnectionMask(BlockState state) {
        int i = 0;

        for (int j = 0; j < FACINGS.length; ++j) {
            if ((state.get(FACING_PROPERTIES.get(FACINGS[j])) == Side.PRESENT
                || state.get(FACING_PROPERTIES.get(FACINGS[j])) == Side.ACTIVE)) {
                i |= 1 << j;
            }
        }

        return i;
    }

    static {
        FACING_PROPERTIES = ImmutableMap.copyOf((Map) Util.make(Maps.newEnumMap(Direction.class), (directions) -> {
            directions.put(Direction.NORTH, NORTH);
            directions.put(Direction.EAST, EAST);
            directions.put(Direction.SOUTH, SOUTH);
            directions.put(Direction.WEST, WEST);
            directions.put(Direction.UP, UP);
            directions.put(Direction.DOWN, DOWN);
        }));
    }

    /**
     * Enum for the different sides of the block.
     */
    public enum Side implements StringIdentifiable {
        /**
         * The side is not present.
         */
        NONE("none"),
        /**
         * The side is present.
         */
        PRESENT("present"),
        /**
         * The side is present and active.
         */
        ACTIVE("active");

        private final String name;

        Side(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.asString();
        }

        /**
         * Returns the name of the side as a string.
         * @return the name of the side as a string.
         */
        public String asString() {
            return this.name;
        }
    }

}
