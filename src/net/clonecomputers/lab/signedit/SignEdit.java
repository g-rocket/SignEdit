package net.clonecomputers.lab.signedit;

import java.util.*;

import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;

public class SignEdit extends JavaPlugin {
	
	private final EditBeginListener editBeginListener = new EditBeginListener(this);
	private final EditEndListener editFinishedListener = new EditEndListener(this);
	
	public Map<String,int[]> editsInProgress = new HashMap<String,int[]>();
	
	public void onDisable() {
		getLogger().info("SignEdit 2.1 doesn't know how to disable itself");
	}

	public void onEnable() {
		getLogger().info("SignEdit 2.1 Enabled");
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(editBeginListener, this);
        pm.registerEvents(editFinishedListener, this);
	}
}
