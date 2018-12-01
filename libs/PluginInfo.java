package com.github.elrol.mobdrop.libs;

import org.spongepowered.api.text.Text;

public class PluginInfo {

	public static final String NAME = "MobDropModifier";
	public static final String ID = "mobdrop";
	public static final String VERSION = "Alpha v0.1";
	public static final String DESC = "A Mob Drop Configuring plugin commissioned by the PokeFreaks network";
	
	//Permissions
	public class Permissions{
		public static final String mobDrop = "mobdrop.command.mobdrop";
		public static final String mobDropAdd = "mobdrop.command.add";
		public static final String mobDropRemove = "mobdrop.command.remove";
		public static final String mobDropAddAll = "mobdrop.command.addall";
		public static final String mobDropRemoveAll = "mobdrop.command.removeall";
	}
	
	//Descriptions
	public static class Descriptions{
		public static final Text mobDrop = Text.of("The general help command for MobDrop");
		public static final Text mobDropAdd = Text.of("Adds the item currently held in the main hand to the specified Mob, with the chances and drop range");
		public static final Text mobDropRemove = Text.of("Removes the item currently held in the main hand from the specified Mob");
		public static final Text mobDropAddAll = Text.of("Adds the item currently held in the main hand to all mobs, with the chance and drop range");
		public static final Text mobDropRemoveAll = Text.of("Removes the currently held item from all mobs");
	}
}
