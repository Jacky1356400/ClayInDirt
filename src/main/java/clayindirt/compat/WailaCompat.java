package clayindirt.compat;

import clayindirt.block.BlockFirePit;
import clayindirt.util.IWailaInfoProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class WailaCompat implements IWailaDataProvider {

    public static final WailaCompat INSTANCE = new WailaCompat();

    private WailaCompat() {}

    private static boolean registered;
    private static boolean loaded;

    public static void load(IWailaRegistrar registrar) {
        if (!registered) {
            throw new RuntimeException("Please register this handler using the provided method.");
        }
        if (!loaded) {
            registrar.registerHeadProvider(INSTANCE, BlockFirePit.class);
            registrar.registerBodyProvider(INSTANCE, BlockFirePit.class);
            registrar.registerTailProvider(INSTANCE, BlockFirePit.class);
            loaded = true;
        }
    }

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendMessage("waila", "register", "clayindirt.compat.WailaCompat.load");
    }

    @Nullable
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Nonnull
    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof IWailaInfoProvider) {
            return ((IWailaInfoProvider)accessor.getBlock()).getWailaBody(itemStack, currenttip, accessor, config);
        }
        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }

}