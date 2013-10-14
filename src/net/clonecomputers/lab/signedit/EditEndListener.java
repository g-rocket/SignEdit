package net.clonecomputers.lab.signedit;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.block.*;

public class EditEndListener implements Listener {
	SignEdit plugin;

	public EditEndListener(SignEdit plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBookClosed(PlayerEditBookEvent e){
		Player p = e.getPlayer();
		int[] loc = plugin.editsInProgress.get(p.getName());
		if(loc == null) return; // nothing to do here
		plugin.editsInProgress.remove(p.getName());
		//p.sendMessage(plugin.editsInProgress.toString());
		BlockState state = p.getWorld().getBlockAt(loc[0], loc[1], loc[2]).getState();
		BookMeta book = e.getNewBookMeta();
		switch(state.getBlock().getType()){
			case SIGN:case SIGN_POST:case WALL_SIGN:
				writeToSign(book, state, p);
				break;
			default:
				System.err.println("Invalid edit at "+Arrays.toString(loc));
				System.err.println("BlockType not supported:"+state.getBlock().getType());
				return;
		}
		ItemStack bookItem = p.getInventory().getItem(e.getSlot());
		bookItem.setType(Material.BOOK);
		bookItem.setAmount(1);
	}
	
	public void writeToSign(BookMeta book, BlockState state, Player p){
		Sign sign = (Sign)state;
		String[] lines = book.getPages().get(0).split("\n");
		for(int i = 0; i < 4; i++){
			if(i >= lines.length){
				sign.setLine(i, "");
				continue;
			}
			sign.setLine(i, lines[i]);
		}
		sign.update(false, false);
		Block block = sign.getBlock();
		@SuppressWarnings("deprecation") // deprecated, but no equivalent method exists yet
		org.bukkit.material.Sign data = new org.bukkit.material.Sign(block.getType(), block.getData());
		BlockPlaceEvent fakeEvent = new BlockPlaceEvent(block, sign,
				block.getRelative(data.getAttachedFace()), block.getDrops().iterator().next(), p, true);
		plugin.getServer().getPluginManager().callEvent(fakeEvent); // tell plugins sign changed
	}
}
