/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.github.atomicblom.hcmw.client.model.obj;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

/*
 * Loader for OBJ models.
 * To enable your mod call instance.addDomain(modid).
 * If you need more control over accepted resources - extend the class, and register a new instance with ModelLoaderRegistry.
 */
public enum OBJLoader implements ICustomModelLoader {
    INSTANCE;

    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<String>();
    private final Map<ResourceLocation, OBJModel> cache = new HashMap<ResourceLocation, OBJModel>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<ResourceLocation, Exception>();

    public void addDomain(String domain)
    {
        enabledDomains.add(domain.toLowerCase());
        FMLLog.log(Level.INFO, "OBJLoader: Domain %s has been added.", domain.toLowerCase());
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.manager = resourceManager;
        cache.clear();
        errors.clear();
    }

    public boolean accepts(ResourceLocation modelLocation)
    {
        return enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".obj");
    }

    public IModel loadModel(ResourceLocation modelLocation) throws Exception
    {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        if (!cache.containsKey(file))
        {
            IResource resource = null;
            try
            {
                resource = manager.getResource(file);
            }
            catch (FileNotFoundException e)
            {
                if (modelLocation.getResourcePath().startsWith("models/block/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/block/".length())));
                else if (modelLocation.getResourcePath().startsWith("models/item/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/item/".length())));
                else throw e;
            }
            Parser parser = new Parser(resource, manager);
            OBJModel model = null;
            try
            {
                model = parser.parse();
            }
            catch (Exception e)
            {
                errors.put(modelLocation, e);
            }
            finally
            {
                cache.put(modelLocation, model);
            }
        }
        OBJModel model = cache.get(file);
        if (model == null) throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + file, errors.get(modelLocation));
        return model;
    }
}