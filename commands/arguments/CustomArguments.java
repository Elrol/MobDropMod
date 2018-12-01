package com.github.elrol.mobdrop.commands.arguments;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;
import org.spongepowered.common.registry.type.entity.EntityTypeRegistryModule;

public class CustomArguments {
	
	public static CommandElement mobName(Text text) {
		return new MobName(text);
	}
	
	public static class MobName extends CommandElement{

		public MobName(Text key) {
			super(key);
		}

		@Override
		protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
			return args.next();
		}

		@Override
		public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
			List<String> mobNames = new ArrayList<String>();
			for(EntityType type : EntityTypeRegistryModule.getInstance().getAll()) {
				mobNames.add(type.getId());
			}
			return mobNames;
		}
		
		public Text getUsage(CommandSource src) {
			return Text.of("<Mob>");
		}
	}
}
