/*
 * This code is heavily borrowed from Immersive Engineering by BluSunrize
 *
 * It has been used with his permission
 *
 * Copyright 2016 BluSunrize, All rights Reserved
 */

package com.github.atomicblom.hcmw.client.rendering;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;

import java.lang.reflect.Field;

public class IEOBJModel extends OBJModel
{
    public IEOBJModel(MaterialLibrary matLib, ResourceLocation modelLocation)
    {
        super(matLib, modelLocation);
    }

    public IEOBJModel(MaterialLibrary matLib, ResourceLocation modelLocation, Object customData)
    {
        super(matLib, modelLocation);
        this.setCustomData(customData);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        IBakedModel preBaked = super.bake(state, format, bakedTextureGetter);
        return new IESmartObjModel(preBaked, this, state, format, IESmartObjModel.getTexturesForOBJModel(preBaked), null);
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData)
    {
        IEOBJModel ret = new IEOBJModel(this.getMatLib(), getResourceLocation(), getCustomData());
        return ret;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
        IEOBJModel ret = new IEOBJModel(this.getMatLib().makeLibWithReplacements(textures), getResourceLocation(), getCustomData());
        return ret;
    }

    static Field f_modelLocation;
    public ResourceLocation getResourceLocation()
    {
        try{
            if(f_modelLocation==null)
            {
                f_modelLocation = OBJModel.class.getDeclaredField("modelLocation");
                f_modelLocation.setAccessible(true);
            }
            if(f_modelLocation!=null)
                return (ResourceLocation)f_modelLocation.get(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    static Field f_customData;
    public Object getCustomData()
    {
        try{
            if(f_customData==null)
            {
                f_customData = OBJModel.class.getDeclaredField("customData");
                f_customData.setAccessible(true);
            }
            if(f_customData!=null)
                return f_customData.get(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void setCustomData(Object data)
    {
        try{
            if(f_customData==null)
            {
                f_customData = OBJModel.class.getDeclaredField("customData");
                f_customData.setAccessible(true);
            }
            if(f_customData!=null)
                f_customData.set(this, data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}