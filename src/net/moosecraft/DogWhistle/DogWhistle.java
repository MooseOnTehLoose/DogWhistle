package net.moosecraft.DogWhistle;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DogWhistle extends JavaPlugin implements Listener{
	
	
	//Read variables from config file
	Integer radiusX;
	Integer radiusY;
	Integer radiusZ;
	Boolean dogEnabled;
	Boolean catEnabled;
	
	
	@Override
    public void onEnable(){
		getLogger().info("DogWhistle has been enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		
		
    	//Read variables from config file
    	radiusX = Integer.valueOf(this.getConfig().getString("radiusX"));
    	radiusY = Integer.valueOf(this.getConfig().getString("radiusY"));
    	radiusZ = Integer.valueOf(this.getConfig().getString("radiusZ"));
    	dogEnabled = Boolean.valueOf(this.getConfig().getString("dogs enabled"));
    	catEnabled = Boolean.valueOf(this.getConfig().getString("cats enabled"));
    	
    	//Check for bad input
    	if ( radiusX == null ){
    		radiusX = 100;
    	}
    	if ( radiusY == null ){
    		radiusY = 100;
    	}
    	if ( radiusZ == null ){
    		radiusZ = 100;
    	}
    	if (dogEnabled == null ){
    		dogEnabled = true;
    	}
    	if (catEnabled == null ){
    		catEnabled = true;
    	}
		
		
		
		
    }
    @Override
    public void onDisable() {
    	getLogger().info("DogWhistle has been disabled");
    }
    
    @EventHandler // EventPriority.NORMAL by default
    public void onInteract(PlayerInteractEvent event) {
    	

    	// only trigger the action if the player exists, clicked the air and had the action item
    	if ( event.getPlayer() != null && event.getAction() != null && event.getItem() != null ){

    		Player owner = event.getPlayer();
    		Action click = event.getAction();
    		Action properAction = Action.LEFT_CLICK_AIR;
    		ItemStack inHand = event.getItem();
    		Material bone = Material.BONE;
	
    		// make sure to check against correct usage
    		if ( owner.isOnline() ){
    			//check if they left clicked in the air with a bone 
    			if ( click != null && click == properAction && inHand.getType() != null ){	
    				for ( Entity entity : owner.getNearbyEntities(radiusX, radiusY, radiusZ) ){			
    					//check in the radius specified for all wolf entities
    					Material heldItem = inHand.getType();
    					if ( heldItem == bone ){						
    						if (  entity.getType() == EntityType.WOLF && dogEnabled ){				
    							//cast entity to wolf
    							Wolf dog = (Wolf)entity;  								
    							if ( dog.getOwner() != null ){ 	
    								//get the name of the wolf's owner
    								String aName =  dog.getOwner().getName();				
    								//get the name of the player who caused the InteractEvent
    								String oName = owner.getName();	
    								//If names are equal, toggle state of dog, else do not toggle state
    								if (aName == oName){
    									if ( dog.isSitting() == true ){
    										dog.setSitting(false);
    									}//if 
    									else{
    										dog.setSitting(true);
    									}//else
    								}//if correct player name
    							}//if not null
    						}//if wolf
    					}//if bone in hand			
    					//check in the radius specified for all cat entities
    					else if ( ( heldItem == Material.COD ||
    							    heldItem == Material.PUFFERFISH ||
    								heldItem == Material.TROPICAL_FISH ||
    								heldItem == Material.SALMON ) && catEnabled ){					
    						if (  entity.getType() == EntityType.CAT ){					
    							//cast entity to Ocelot
    							Ocelot cat = (Ocelot)entity;	
    							if ( ((Tameable) cat).getOwner() != null ){ 	
    								//get the name of the cat's owner
    								String aName =  ((Tameable) cat).getOwner().getName();		
    								//get the name of the player who caused the InteractEvent
    								String oName = owner.getName();		
    								//If names are equal, toggle state of cat, else do not toggle state
    								if ( aName == oName ){
    									if ( ((Sittable) cat).isSitting() == true ){
    										((Sittable) cat).setSitting(false);
    									}//if 	
    									else{
    										((Sittable) cat).setSitting(true);
    									}//else
    								}//if correct player name		
    							}//if not null	
    						}//if cat	
    					}//if fish in hand	
    				}//for all entity in radius			
    			}//if proper action not null and proper item 
    		}//if online
    	}//if getAction ! null
    }//on interact
}//class dogwhistle