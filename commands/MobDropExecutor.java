package com.github.elrol.mobdrop.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.common.registry.type.entity.EntityTypeRegistryModule;

import com.github.elrol.mobdrop.config.DefaultConfiguration;
import com.github.elrol.mobdrop.libs.Methods;
import com.github.elrol.mobdrop.libs.TextLibs;

public class MobDropExecutor implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("mob")) {
			if(EntityTypeRegistryModule.getInstance().getById(args.<String>getOne("mob").get()).isPresent()) {
				EntityType mobType = EntityTypeRegistryModule.getInstance().getById(args.<String>getOne("mob").get()).get();
				if(DefaultConfiguration.getInstance().hasCustomDrops(mobType.getName())) {
	            	TextLibs.sendMessage(src, "[" + mobType.getName() + "]");
					for(ItemStack stack : DefaultConfiguration.getInstance().getDrops(mobType.getName())) {
	            		int chance = DefaultConfiguration.getInstance().getChance(mobType.getName(), stack);
	            		int min = DefaultConfiguration.getInstance().getMinMax(mobType.getName(), stack)[0];
	            		int max = DefaultConfiguration.getInstance().getMinMax(mobType.getName(), stack)[1];
	            		String name = Methods.getName(stack);
	            		if(min != max) {
	            			TextLibs.sendMessageNoHeader(src, name + ":[ Chance: " + chance + ", Qty: " + min + "-" + max + " ]");
	            		} else {
	            			TextLibs.sendMessageNoHeader(src, name + ":[ Chance: " + chance + ", Qty: " + min + " ]");
	            		}
	            	}
				} else {
					TextLibs.sendMessage(src, "-No Custom Drops-");
				}
			} else {
				TextLibs.sendError(src, "Invalid mob id.");
			}
		}
		return CommandResult.success();
	}

}
