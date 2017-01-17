package com.github.atomicblom.hcmw;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.renderer.HighlightBoundingBoxDebug;
import com.foudroyantfactotum.tool.structure.renderer.HighlightPreview;
import com.github.atomicblom.hcmw.gui.GuiHandler;
import com.github.atomicblom.hcmw.util.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = HomecraftMineware.MODID, version = HomecraftMineware.VERSION)
public class HomecraftMineware
{
    public static final String MODID = "hcmw";
    public static final String VERSION = "1.0";
    public static final String MOD_VERSION = "@MOD_VERSION@";
    public static boolean DEBUG = false;
    public static final String BUILT_BY_CI = "@BUILT_BY_CI@";



    @Mod.Instance
    public static HomecraftMineware INSTANCE = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        if (BUILT_BY_CI == "true") {
            DEBUG = false;
        }
        if (DEBUG) {
            Logger.info("Homecraft Mineware Debugging is enabled.");
        }

        StructureRegistry.setMOD_ID(MODID);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        StructureRegistry.loadRegisteredPatterns();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, GuiHandler.INSTANCE);
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new HighlightPreview());
        if (DEBUG)
        {
            MinecraftForge.EVENT_BUS.register(new HighlightBoundingBoxDebug());
            Logger.info("Homecraft Mineware Structure Highlighting enabled.");
        }

    }

    @EventHandler
    public static void serverStart(FMLServerStartingEvent event)
    {
        if (DEBUG)
        {

            event.registerServerCommand(new StructureRegistry.CommandReloadStructures());
            Logger.info("Reload Structures command enabled.");
        }
    }
}