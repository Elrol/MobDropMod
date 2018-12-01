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

public class MobDropAddExecutor implements CommandExecutor {

	private boolean isAll;
	
	public MobDropAddExecutor(boolean isAll) {
		this.isAll = isAll;
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player && ((Player)src).getEquipped(EquipmentTypes.MAIN_HAND).isPresent()) {
			Player player = (Player)src;
			ItemStack stack = player.getEquipped(EquipmentTypes.MAIN_HAND).get(); 
			int min = args.<Integer>getOne("min").get();
			int max = min;
			int chance = args.<Integer>getOne("chance").get();
			String name;
			if(isAll) {
				name = "all";
			} else {
				EntityType type = EntityTypeRegistryModule.getInstance().getById(args.<String>getOne("mob").get()).get();
				name = type.getName();
			}
			if(args.hasAny("max"))
				max = args.<Integer>getOne("max").get();
			 
			DefaultConfiguration.getInstance().addDrop(name, stack, chance, min, max);
			TextLibs.sendMessage(src, "Successfully added " + Methods.getName(stack) + " to " + name);
		} else {
			TextLibs.sendError(src, "You must be a player, and have an item in your main hand, to use this command.");
		}
		return CommandResult.success();
	}

}
