package com.github.elrol.mobdrop;

import java.io.File;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.mobdrop.commands.CommandRegistry;
import com.github.elrol.mobdrop.config.DefaultConfiguration;
import com.github.elrol.mobdrop.libs.Methods;
import com.github.elrol.mobdrop.libs.PluginInfo;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESC)
public class Main {

	private static Main instance;
	//public EventBus EVENT_BUS = new EventBus();
	
	private Logger logger;
	private File defaultConfig;
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	@Inject
	public Main(Logger logger, @DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> loader, @ConfigDir(sharedRoot = false) File configDir){
		this.logger = logger;
		this.defaultConfig = new File(configDir + "/dropparty.conf");
		this.configManager =HoconConfigurationLoader.builder().setFile(defaultConfig).build();
		
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info("Started MobDrops");
	}
	
	@Listener
	public void onServerStop(GameStoppedServerEvent event) {
		logger.info("Stopping MobDrops");
	}
	
	@Listener
	public void preInit(GamePreInitializationEvent event){
		
	}
	
	@Listener
	public void init(GameInitializationEvent event){
		logger.info("Registering Configs");
		DefaultConfiguration.getInstance().setup(defaultConfig, configManager);
		
		CommandRegistry.setup(this);
	}
	
	@Listener
	public void postInit(GamePostInitializationEvent event){
		instance = this;
	}
	
	@Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
		System.out.println("Mob Died");
        Optional<EntityDamageSource> optDamageSource = event.getCause().first(EntityDamageSource.class);
        if (optDamageSource.isPresent()) {
        	System.out.println("Damage source is present");
            EntityDamageSource damageSource = optDamageSource.get();
            Entity killer = damageSource.getSource();
            if (killer instanceof Player) {
            	System.out.println("Killer is a player");
            	Player player = (Player)killer;
            	Entity target = event.getTargetEntity();
            	Location<World> pos = target.getLocation();
            	String mobType = target.getType().getName();
            	if(DefaultConfiguration.getInstance().hasCustomDrops(mobType)) {
                	System.out.println("Mob has drops");
                	Methods.spawnItems(player, mobType, pos);
                	Methods.spawnItems(player, "all", pos);
                }
            }
        }
    }

	public static Main getInstance() {
		return instance;
	}
	
	public Logger getLogger() {
		return logger;
	}
}
