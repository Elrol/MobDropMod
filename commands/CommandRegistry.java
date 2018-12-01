package com.github.elrol.mobdrop.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.github.elrol.mobdrop.Main;
import com.github.elrol.mobdrop.commands.arguments.CustomArguments;
import com.github.elrol.mobdrop.libs.PluginInfo.Descriptions;
import com.github.elrol.mobdrop.libs.PluginInfo.Permissions;

public class CommandRegistry {

	public static void setup(Main main) {
		//MobDrop remove all
		CommandSpec mobDropRemoveAll = CommandSpec.builder()
				.description(Descriptions.mobDropRemoveAll)
				.permission(Permissions.mobDropRemoveAll)
				.executor(new MobDropRemoveExecutor())
				.build();
		//MobDrop add all [chance] [min] <max>
		CommandSpec mobDropAddAll = CommandSpec.builder()
				.description(Descriptions.mobDropAddAll)
				.permission(Permissions.mobDropAddAll)
				.arguments(GenericArguments.integer(Text.of("chance")),
						GenericArguments.integer(Text.of("min")),
						GenericArguments.optional(GenericArguments.integer(Text.of("max"))))
				.executor(new MobDropAddExecutor(true))
				.build();
				//MobDrop add [MobType] [chance] [min] <max>
		CommandSpec mobDropAdd = CommandSpec.builder()
				.description(Descriptions.mobDropAdd)
				.permission(Permissions.mobDropAdd)
				.arguments(CustomArguments.mobName(Text.of("mob")),
						GenericArguments.integer(Text.of("chance")),
						GenericArguments.integer(Text.of("min")),
						GenericArguments.optional(GenericArguments.integer(Text.of("max"))))
				.executor(new MobDropAddExecutor(false))
				.child(mobDropAddAll, "all")
				.build();
		//MobDrop remove [MobType]
		CommandSpec mobDropRemove = CommandSpec.builder()
				.description(Descriptions.mobDropRemove)
				.permission(Permissions.mobDropRemove)
				.arguments(CustomArguments.mobName(Text.of("mob")))
				.executor(new MobDropRemoveExecutor())
				.child(mobDropRemoveAll, "all")
				.build();
		//MobDrop
		CommandSpec mobDrop = CommandSpec.builder()
			    .description(Descriptions.mobDrop)
			    .permission(Permissions.mobDrop)
			    .arguments(GenericArguments.optional(CustomArguments.mobName(Text.of("mob"))))
			    .executor(new MobDropExecutor())
			    .child(mobDropAdd, "add")
			    .child(mobDropRemove, "remove")
			    .build();

		Sponge.getCommandManager().register(main, mobDrop, "mobdrop", "md");
	}	
}
