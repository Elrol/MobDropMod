package com.github.elrol.mobdrop.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.common.registry.type.entity.EntityTypeRegistryModule;

import com.github.elrol.mobdrop.config.DefaultConfiguration;
import com.github.elrol.mobdrop.libs.Methods;
import com.github.elrol.mobdrop.libs.TextLibs;

public class MobDropRemoveExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player && ((Player)src).getEquipped(EquipmentTypes.MAIN_HAND).isPresent()) {
			if(args.hasAny("mob")) {
				EntityType type = EntityTypeRegistryModule.getInstance().getById(args.<String>getOne("mob").get()).get();
				Player player = (Player)src;
				ItemStack stack = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
				if(DefaultConfiguration.getInstance().hasCustomDrops(type.getName())) {
					DefaultConfiguration.getInstance().removeDrop(type.getName(), stack);
				}
				TextLibs.sendMessage(src, Methods.getName(stack) + " was removed from " + type.getName());
			} else {
				Player player = (Player)src;
				ItemStack stack = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
				DefaultConfiguration.getInstance().removeAllDrop(stack);
				TextLibs.sendMessage(src, Methods.getName(stack) + " was removed from all mobs");
			}
		} else {
			TextLibs.sendError(src, "You must be a player, and have an item in your main hand, to use this command.");
		}
		return CommandResult.success();
	}

}
