package com.github.atomicblom.hcmw.registration;

import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.item.StructureBlockItem;
import com.github.atomicblom.hcmw.HomecraftMineware;
import com.github.atomicblom.hcmw.client.CreativeTab;
import com.github.atomicblom.hcmw.library.BlockLibrary;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@EventBusSubscriber(modid = HomecraftMineware.MODID)
public final class ItemRegistration {

    @SubscribeEvent
    public static void registerItems(Register<Item> event) {
        final Items items = new Items(event.getRegistry());
        items.addStructure(BlockLibrary.bed_4post);
        items.addStructure(BlockLibrary.bed_canopy);
        items.addStructure(BlockLibrary.door_inner_gate);
        items.addStructure(BlockLibrary.door_tower);
        items.addStructure(BlockLibrary.door_grand);

        items.add(BlockLibrary.item_barrel);
        items.add(BlockLibrary.fluid_barrel);
        items.add(BlockLibrary.bed_side_drawers);
        items.add(BlockLibrary.candle_holder);
        items.add(BlockLibrary.lantern);
    }

    private static class Items {
        private final IForgeRegistry<Item> registry;

        Items(IForgeRegistry<Item> registry)
        {
            this.registry = registry;
        }

        ItemBlock add(Block block)
        {
            final ItemBlock item = new ItemBlock(block);
            add(item, block.getRegistryName());
            return item;
        }

        Item add(Item item, ResourceLocation registryName) {
            item
                    .setRegistryName(registryName)
                    .setUnlocalizedName("item." + registryName)
                    .setCreativeTab(CreativeTab.INSTANCE);

            registry.register(item);

            return item;
        }

        StructureBlockItem addStructure(StructureBlock block)
        {
            final StructureBlockItem item = new StructureBlockItem(block);
            add(item, block.getRegistryName());
            return item;
        }
    }
}
