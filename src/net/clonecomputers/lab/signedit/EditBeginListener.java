package net.clonecomputers.lab.signedit;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.block.Sign;

public class EditBeginListener implements Listener {
	SignEdit plugin;

	public EditBeginListener(SignEdit plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void beginEdit(PlayerInteractEvent e){
		if(!e.hasBlock() || !e.hasItem()) return;
		if(!e.getItem().getType().equals(Material.BOOK)) return;
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		BlockState state = e.getClickedBlock().getState();
		ItemStack i = e.getItem();
		i.setType(Material.BOOK_AND_QUILL);
		i.setAmount(1);
		BookMeta book = (BookMeta)i.getItemMeta();
		switch(state.getBlock().getType()){
			case SIGN:case SIGN_POST:case WALL_SIGN:
				book = readSign(state, book);
				break;
			
			default:
				e.getPlayer().sendMessage("Editing is not supported for that block");
				return;
		}
		i.setItemMeta(book);
		plugin.editsInProgress.put(e.getPlayer().getName(), new int[]{state.getX(), state.getY(), state.getZ()});
	}
	
	public BookMeta readSign(BlockState state, BookMeta book){
		Sign sign = (Sign)state;
		String[] lines = sign.getLines();
		String signText = lines[0]+"\n"+lines[1]+"\n"+lines[2]+"\n"+lines[3];
		book.setPages(signText);
		return book;
	}
}
