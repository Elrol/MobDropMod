package com.github.elrol.mobdrop.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.item.inventory.ItemStack;

import com.github.elrol.mobdrop.libs.Methods;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class DefaultConfiguration {

	private static DefaultConfiguration instance = new DefaultConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	
	public static DefaultConfiguration getInstance() {
		return instance;
	}
	
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> loader) {
		this.loader = loader;
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
				saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			loadConfig();
		}
	}
	
	public CommentedConfigurationNode getConfig() {
		return config;
	}
	

	public void saveConfig() {
		try {
			loader.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		try {
			config = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasCustomDrops(String mobType) {
		System.out.println("Target type: " + mobType);
		for(Object node : config.getChildrenMap().keySet()) {
			System.out.println((String)node);
			if(((String)node).equalsIgnoreCase(mobType) || ((String)node).equalsIgnoreCase("all")) {
				System.out.println("Has custom drop");
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	public void addDrop(String mobType, ItemStack stack, float chance, int min, int max) {
		try {
			config.getNode(mobType, Methods.getName(stack), "item").setValue(new TypeToken<ItemStack>() {}, stack);
			config.getNode(mobType, Methods.getName(stack), "chance").setValue(chance);
			config.getNode(mobType, Methods.getName(stack), "min").setValue(min);
			config.getNode(mobType, Methods.getName(stack), "max").setValue(max);
			saveConfig();
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	public void removeDrop(String mobType, ItemStack stack) {
		loadConfig();
		config.getNode(mobType, Methods.getName(stack)).setValue(null);
		saveConfig();
	}
	
	public void removeAllDrop(ItemStack stack) {
		loadConfig();
		for(Object node : config.getChildrenMap().keySet()) {
			System.out.println(node);
			config.getNode((String)node, Methods.getName(stack)).setValue(null);
		}
		saveConfig();
	}
	
	@SuppressWarnings("serial")
	public List<ItemStack> getDrops(String mobType) {
		loadConfig();
		List<ItemStack> list = new ArrayList<ItemStack>();
		for(Object node : config.getNode(mobType).getChildrenMap().keySet()) {
			try {
				list.add(config.getNode(mobType, (String)node, "item").getValue(new TypeToken<ItemStack>() {}));
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public int getChance(String mobType, ItemStack stack) {
		loadConfig();
		int chance = config.getNode(mobType, Methods.getName(stack), "chance").getInt();
		System.out.println("Chance: " + chance);
		return chance;
	}
	
	public int[] getMinMax(String mobType, ItemStack stack) {
		loadConfig();
		int min = config.getNode(mobType, Methods.getName(stack), "min").getInt();
		int max = config.getNode(mobType, Methods.getName(stack), "max").getInt();
		System.out.println("Quantity: " + min + (min != max ? "to " + max : ""));
		return new int[] {min,max};
	}
}
