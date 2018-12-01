package com.github.elrol.mobdrop.libs;

import java.util.List;
import java.util.Random;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.github.elrol.mobdrop.config.DefaultConfiguration;

public class Methods {
	
	public static String getName(ItemStack stack) {
		if(stack.get(DisplayNameData.class).isPresent()){
	    	DisplayNameData data = stack.get(DisplayNameData.class).get();
	    	Text displayName = data.displayName().get();
	    	return displayName.toPlain();
	    }
		return stack.getType().getName();
	}
	
	public static void spawnItems(Player player, String mobType, Location<World> pos) {
		Random rand = new Random();
		for(ItemStack stack : DefaultConfiguration.getInstance().getDrops(mobType)) {
    		int chance = DefaultConfiguration.getInstance().getChance(mobType, stack);
    		int min = DefaultConfiguration.getInstance().getMinMax(mobType, stack)[0];
    		int max = DefaultConfiguration.getInstance().getMinMax(mobType, stack)[1];
    		System.out.println("Trying to drop " + Methods.getName(stack));
    		if(rand.nextInt(100) < chance) {
    			int qty = min + (min != max ? rand.nextInt(max-min) : 0);
    			if(player.getEquipped(EquipmentTypes.MAIN_HAND).isPresent()) {
    				ItemStack mainHand = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
    				if(mainHand.get(Keys.ITEM_ENCHANTMENTS).isPresent()) {
        				List<Enchantment> enchantments = mainHand.get(Keys.ITEM_ENCHANTMENTS).get();
        				for(Enchantment enchant : enchantments) {
        					if(enchant.getType().equals(EnchantmentTypes.LOOTING)) {
        						qty += enchant.getLevel();
        						break;
        					}
        				}
    				}
    			}
    			stack.setQuantity(qty);
    			World world = player.getWorld();
    			Entity itemEntity = world.createEntity(EntityTypes.ITEM, new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
    	        Item items = (Item) itemEntity;
    	        items.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
    	        world.spawnEntity(items);
    	        System.out.println("Dropping Item");
    		}
    	}
	}
}
