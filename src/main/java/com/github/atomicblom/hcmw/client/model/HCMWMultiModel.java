package com.github.atomicblom.hcmw.client.model;

import com.google.common.base.Function;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class HCMWMultiModel {
    protected static final DefaultStateMapper stateMapper = new DefaultStateMapper();
    protected static final Function<ResourceLocation, TextureAtlasSprite> textureGetter =
            location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

    public abstract void loadModel(ModelBakeEvent event);

    protected static IModel loadModel(ResourceLocation rl)
    {
        try
        {
            return ModelLoaderRegistry.getModel(rl);
        } catch (Exception e)
        {
            return ModelLoaderRegistry.getMissingModel();
        }
    }

    //TODO: Is this actually neccessary? Mincraft provides a Default state mapper that may be sufficient
    protected static class DefaultStateMapper extends StateMapperBase
    {
        public ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return new ModelResourceLocation(state.getBlock().getRegistryName(), getPropertyString(state.getProperties()));
        }
    }
}
