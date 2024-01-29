package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Map;

/**
 * A computer block that can change its appearance depending on its neighbors.
 *
 * Copied from {@link net.minecraft.block.ConnectingBlock}.
 */
public abstract class ConnectingComputerBlock extends ComputerBlock{
    private static final Direction[] FACINGS = Direction.values();
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES;
    protected final VoxelShape[] facingsToShape;

    protected ConnectingComputerBlock(float radius) {
        super();
        this.facingsToShape = this.generateFacingsToShapeMap(radius);
    }

    protected ConnectingComputerBlock(float radius, AbstractBlock.Settings settings) {
        super(settings);
        this.facingsToShape = this.generateFacingsToShapeMap(radius);
    }

    private VoxelShape[] generateFacingsToShapeMap(float radius) {
        float f = 0.5F - radius;
        float g = 0.5F + radius;
        VoxelShape voxelShape = Block.createCuboidShape((double)(f * 16.0F), (double)(f * 16.0F), (double)(f * 16.0F), (double)(g * 16.0F), (double)(g * 16.0F), (double)(g * 16.0F));
        VoxelShape[] voxelShapes = new VoxelShape[FACINGS.length];

        for(int i = 0; i < FACINGS.length; ++i) {
            Direction direction = FACINGS[i];
            voxelShapes[i] = VoxelShapes.cuboid(0.5 + Math.min((double)(-radius), (double)direction.getOffsetX() * 0.5), 0.5 + Math.min((double)(-radius), (double)direction.getOffsetY() * 0.5), 0.5 + Math.min((double)(-radius), (double)direction.getOffsetZ() * 0.5), 0.5 + Math.max((double)radius, (double)direction.getOffsetX() * 0.5), 0.5 + Math.max((double)radius, (double)direction.getOffsetY() * 0.5), 0.5 + Math.max((double)radius, (double)direction.getOffsetZ() * 0.5));
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

    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.facingsToShape[this.getConnectionMask(state)];
    }

    protected int getConnectionMask(BlockState state) {
        int i = 0;

        for(int j = 0; j < FACINGS.length; ++j) {
            if ((Boolean)state.get((Property)FACING_PROPERTIES.get(FACINGS[j]))) {
                i |= 1 << j;
            }
        }

        return i;
    }

    static {
        NORTH = Properties.NORTH;
        EAST = Properties.EAST;
        SOUTH = Properties.SOUTH;
        WEST = Properties.WEST;
        UP = Properties.UP;
        DOWN = Properties.DOWN;
        FACING_PROPERTIES = ImmutableMap.copyOf((Map) Util.make(Maps.newEnumMap(Direction.class), (directions) -> {
            directions.put(Direction.NORTH, NORTH);
            directions.put(Direction.EAST, EAST);
            directions.put(Direction.SOUTH, SOUTH);
            directions.put(Direction.WEST, WEST);
            directions.put(Direction.UP, UP);
            directions.put(Direction.DOWN, DOWN);
        }));
    }
}
