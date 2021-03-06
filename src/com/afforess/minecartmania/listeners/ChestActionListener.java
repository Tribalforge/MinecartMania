package com.afforess.minecartmania.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.afforess.minecartmania.MMMinecart;
import com.afforess.minecartmania.entity.Item;
import com.afforess.minecartmania.entity.MinecartManiaChest;
import com.afforess.minecartmania.entity.MinecartManiaWorld;
import com.afforess.minecartmania.events.ChestPoweredEvent;
import com.afforess.minecartmania.events.MinecartActionEvent;
import com.afforess.minecartmania.utils.ChestStorageUtil;
import com.afforess.minecartmania.utils.ChestUtil;


public class ChestActionListener implements Listener{

	@EventHandler
	public void onChestPoweredEvent(ChestPoweredEvent event) {
		if (event.isPowered() && !event.isActionTaken()) {

			MinecartManiaChest chest = event.getChest();
			Item minecartType = getMinecartType(chest);
			Location spawnLocation = ChestUtil.getSpawnLocationSignOverride(chest);
			if (spawnLocation == null) {
				spawnLocation = ChestStorageUtil.getSpawnLocation(chest);
			}
			if (spawnLocation != null && chest.contains(minecartType)) {
				if (chest.canSpawnMinecart() && chest.removeItem(minecartType.getId())) {
					//			CompassDirection direction = ChestUtil.getDirection(chest.getLocation(), spawnLocation);
					MinecartManiaWorld.spawnMinecart(spawnLocation, minecartType, chest);
					//			minecart.setMotion(direction, (Double)MinecartManiaWorld.getConfigurationValue("SpawnAtSpeed",.4));
					event.setActionTaken(true);
				}
			}
		}
	}

	private Item getMinecartType(MinecartManiaChest chest) {

		if (chest.contains(Item.MINECART)) {
			return Item.MINECART;
		}
		else if (chest.contains(Item.STORAGE_MINECART)) {
			return Item.STORAGE_MINECART;
		}
		else if (chest.contains(Item.POWERED_MINECART)) {
			return Item.POWERED_MINECART;
		}

		//		ArrayList<com.afforess.minecartmania.MMSign> signList = SignUtils.getAdjacentMMSignList(chest.getLocation(), 2);
		//		for (com.afforess.minecartmania.MMSign sign : signList) {
		//			if (sign instanceof MinecartTypeSign) {
		//				MinecartTypeSign type = (MinecartTypeSign)sign;
		//				if (type.canDispenseMinecartType(Item.MINECART)) {
		//					if (chest.contains(Item.MINECART)) {
		//						return Item.MINECART;
		//					}
		//				}
		//				if (type.canDispenseMinecartType(Item.POWERED_MINECART)) {
		//					if (chest.contains(Item.POWERED_MINECART)) {
		//						return Item.POWERED_MINECART;
		//					}
		//				}
		//				if (type.canDispenseMinecartType(Item.STORAGE_MINECART)) {
		//					if (chest.contains(Item.STORAGE_MINECART)) {
		//						return Item.STORAGE_MINECART;
		//					}
		//				}
		//			}
		//		}
		//
		//
		//		//Returns standard minecart by default

		return Item.MINECART;

	}


	@EventHandler
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken()) {
			final MMMinecart minecart = event.getMinecart();

			boolean action = false;

			if (!action) {
				//put carts in chests
				action = ChestStorageUtil.doMinecartCollection(minecart);
			}
			if (!action) {
				//put carts in chest
				action = ChestStorageUtil.doCollectParallel(minecart);
			}

			/*
			 * item handling is now all done via Sign actions.
			 */
			
			//	if (!action && minecart.isStorageMinecart() && minecart.isOnRails()) {
			//process item collection chests

			//		MinecartManiaStorageCart mmscart = (MinecartManiaStorageCart)event.getMinecart();

			//		ArrayList<Sign> signs = com.afforess.minecartmania.utils.SignUtils.getAdjacentSignList(mmscart.getLocation(), 2);

			//	Logger.debug("Found " + signs.size() + " signs ");

			//	ArrayList<ItemContainer> containers = CollectionUtils.getItemContainers((MinecartManiaStorageCart)event.getMinecart(), signs);

			//	Logger.debug("Found " + containers.size() + " containers ");

			//				if (containers != null) {
			//					for (ItemContainer container : containers) {
			//						com.afforess.minecartmania.debug.Logger.debug("Processing container " + container.toString());
			//						container.addDirection(minecart.getDirectionOfMotion());
			//						container.doCollection((MinecartManiaInventory) minecart);				
			//					}
			//				}
			//	}

			event.setActionTaken(action);

		}
	}

	//	@EventHandler
	//	public void onMinecartDirectionChangeEvent(MinecartDirectionChangeEvent event) {
	//		if (event.getMinecart().isStorageMinecart()) {
	//			CollectionUtils.updateContainerDirections((MinecartManiaStorageCart)event.getMinecart());
	//		}
	//	}



}
