package us.fyrepvp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;

public class Hotspots extends BukkitRunnable implements Listener {
	
	private int i = 0;
	private boolean hasDied = false;

	private Map<Integer, UUID> village = new HashMap<>();
	private Map<UUID, Integer> villageWait = new HashMap<>();
	private Map<Integer, UUID> quarry = new HashMap<>();
	private Map<UUID, Integer> quarryWait = new HashMap<>();
	private Map<UUID, Long> time = new HashMap<>();
	private Map<UUID, Integer> count = new HashMap<>();
	private Map<UUID, Long> cooldown = new HashMap<>();

	FyreCore plugin;

	public Hotspots(FyreCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onRegionEnter(RegionEnterEvent e) {
		
		if (e.getRegion().getId().equals("village")) {
			UUID u = e.getPlayer().getUniqueId();
			if (!(village.isEmpty())) {
				e.getPlayer().sendMessage(ChatColor.RED + Bukkit.getPlayer(village.get(0)).getName() + ChatColor.GRAY
						+ " is currently in control of the " + ChatColor.RED + e.getRegion().getId() + ChatColor.GRAY
						+ " at (" + ChatColor.RED + (int) (Bukkit.getPlayer(village.get(0)).getLocation().getX()) + ChatColor.GRAY + ", "
						+ ChatColor.RED + (int) (Bukkit.getPlayer(village.get(0)).getLocation().getY()) + ChatColor.GRAY + ", "
						+ ChatColor.RED + (int) (Bukkit.getPlayer(village.get(0)).getLocation().getZ()) + ChatColor.GRAY + ")!");
				villageWait.put(u, i);
				i++;
			} else {
				if (!(cooldown.containsKey(u)) || cooldown.containsKey(e.getPlayer().getUniqueId())
						&& cooldown.get(e.getPlayer().getUniqueId()) + 10 <= (System.currentTimeMillis() / 1000)) {
					Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY
							+ " is now in control of the " + ChatColor.RED + "village " + ChatColor.GRAY + "at ("
							+ ChatColor.RED + (int) (e.getPlayer().getLocation().getX()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (e.getPlayer().getLocation().getY()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (e.getPlayer().getLocation().getZ()) + ChatColor.GRAY + ")!");
					cooldown.put(u, (System.currentTimeMillis() / 1000));
				}

				village.put(0, u);
				time.put(u, System.currentTimeMillis());
				count.put(u, 0);
			}
		}
		// (e.getRegion().getId().equals("village") ||
		// e.getRegion().getId().equals("quarry"))
		else if (e.getRegion().getId().equals("quarry")) {
			UUID u = e.getPlayer().getUniqueId();
			if (!(quarry.isEmpty())) {
				e.getPlayer().sendMessage(ChatColor.RED + Bukkit.getPlayer(quarry.get(0)).getName() + ChatColor.GRAY
						+ " is currently in control of the " + ChatColor.RED + e.getRegion().getId() + ChatColor.GRAY
						+ " at (" + ChatColor.RED + (int) (Bukkit.getPlayer(quarry.get(0)).getLocation().getX()) + ChatColor.GRAY + ", "
						+ ChatColor.RED + (int) (Bukkit.getPlayer(quarry.get(0)).getLocation().getY()) + ChatColor.GRAY + ", "
						+ ChatColor.RED + (int) (Bukkit.getPlayer(quarry.get(0)).getLocation().getZ()) + ChatColor.GRAY + ")!");
				quarryWait.put(u, i);
				i++;
			} else {
				if (!(cooldown.containsKey(u)) || cooldown.containsKey(e.getPlayer().getUniqueId())
						&& cooldown.get(e.getPlayer().getUniqueId()) + 10 <= (System.currentTimeMillis() / 1000)) {
					Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY
							+ " is now in control of the " + ChatColor.RED + "quarry " + ChatColor.GRAY + "at ("
							+ ChatColor.RED + (int) (e.getPlayer().getLocation().getX()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (e.getPlayer().getLocation().getY()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (e.getPlayer().getLocation().getZ()) + ChatColor.GRAY + ")!");
					cooldown.put(u, (System.currentTimeMillis() / 1000));
				}

				quarry.put(0, u);
				time.put(u, System.currentTimeMillis());
				count.put(u, 0);
			}
		}
	}

	@EventHandler
	public void onRegionLeave(RegionLeaveEvent e) {
		/*
		 * if (e.getRegion().getId().equals("village") ||
		 * e.getRegion().getId().equals("quarry")) { e.getPlayer()
		 * .sendMessage(ChatColor.GRAY + "You are no longer in the " + ChatColor.RED +
		 * e.getRegion().getId() + ChatColor.GRAY + ".");
		 */
		if (cooldown.containsKey(e.getPlayer().getUniqueId())) {
			if (cooldown.get(e.getPlayer().getUniqueId()) + 10 <= (System.currentTimeMillis() / 1000)) {
				cooldown.remove(e.getPlayer().getUniqueId());
			}
		}

		if (villageWait.containsKey(e.getPlayer().getUniqueId())) {
			i--;
			for (UUID uuid : villageWait.keySet()) {
				if (villageWait.get(uuid) > villageWait.get(e.getPlayer().getUniqueId())) {
					villageWait.put(uuid, villageWait.get(uuid) - 1);
				}
			}
			villageWait.remove(e.getPlayer().getUniqueId());
		}

		if (quarryWait.containsKey(e.getPlayer().getUniqueId())) {
			i--;
			for (UUID uuid : quarryWait.keySet()) {
				if (quarryWait.get(uuid) > quarryWait.get(e.getPlayer().getUniqueId())) {
					quarryWait.put(uuid, quarryWait.get(uuid) - 1);
				}
			}
			quarryWait.remove(e.getPlayer().getUniqueId());
		}

		if (e.getRegion().getId().equals("village")) {
			if (village.containsValue(e.getPlayer().getUniqueId())) {
				e.getPlayer().sendMessage(ChatColor.GRAY + "You are no longer in control of the " + ChatColor.RED
						+ "village" + ChatColor.GRAY + "!");
				time.remove(e.getPlayer().getUniqueId());
				if ((count.get(e.getPlayer().getUniqueId()) % 5 == 0 && count.get(e.getPlayer().getUniqueId()) > 0)
						|| (count.get(e.getPlayer().getUniqueId()) % 5 > 0
								&& count.get(e.getPlayer().getUniqueId()) > 5)) {
					Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY
							+ " is no longer in control of the " + ChatColor.RED + "village" + ChatColor.GRAY
							+ " after " + ChatColor.RED + count.get(e.getPlayer().getUniqueId()) + ChatColor.GRAY
							+ " minutes!");
				}
				count.remove(e.getPlayer().getUniqueId());
				village.remove(0);
				if (!hasDied) {
					UUID u = null;
					for (UUID uuid : villageWait.keySet()) {
						if (villageWait.get(uuid) == 0) {
							u = uuid;
						}
					}
					Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getPlayer(u).getName() + ChatColor.GRAY
							+ " is now in control of the " + ChatColor.RED + "village " + ChatColor.GRAY + "at ("
							+ ChatColor.RED + (int) (Bukkit.getPlayer(u).getLocation().getX()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (Bukkit.getPlayer(u).getLocation().getY()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (Bukkit.getPlayer(u).getLocation().getZ()) + ChatColor.GRAY + ")!");
					village.put(0, u);
					time.put(u, System.currentTimeMillis());
					count.put(u, 0);
				}
				hasDied = false;
			}
		}

		if (e.getRegion().getId().equals("quarry")) {
			if (quarry.containsValue(e.getPlayer().getUniqueId())) {
				e.getPlayer().sendMessage(ChatColor.GRAY + "You are no longer in control of the " + ChatColor.RED
						+ "quarry" + ChatColor.GRAY + "!");
				time.remove(e.getPlayer().getUniqueId());
				if ((count.get(e.getPlayer().getUniqueId()) % 5 == 0 && count.get(e.getPlayer().getUniqueId()) > 0)
						|| (count.get(e.getPlayer().getUniqueId()) % 5 > 0
								&& count.get(e.getPlayer().getUniqueId()) > 5)) {
					Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY
							+ " is no longer in control of the " + ChatColor.RED + "quarry" + ChatColor.GRAY + " after "
							+ ChatColor.RED + count.get(e.getPlayer().getUniqueId()) + ChatColor.GRAY + " minutes!");
				}
				count.remove(e.getPlayer().getUniqueId());
				quarry.remove(0);
				if (!hasDied) {
					UUID u = null;
					for (UUID uuid : quarryWait.keySet()) {
						if (quarryWait.get(uuid) == 0) {
							u = uuid;
						}
					}
					Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getPlayer(u).getName() + ChatColor.GRAY
							+ " is now in control of the " + ChatColor.RED + "village " + ChatColor.GRAY + "at ("
							+ ChatColor.RED + (int) (Bukkit.getPlayer(u).getLocation().getX()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (Bukkit.getPlayer(u).getLocation().getY()) + ChatColor.GRAY + ", "
							+ ChatColor.RED + (int) (Bukkit.getPlayer(u).getLocation().getZ()) + ChatColor.GRAY + ")!");
					quarry.put(0, u);
					time.put(u, System.currentTimeMillis());
					count.put(u, 0);
				}
				hasDied = false;
			}
		}
	}

	@EventHandler
	public void playerDeath(PlayerDeathEvent e) {
		
		Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Death " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY + " was killed by " + ChatColor.RED + e.getEntity().getKiller().getName() + ChatColor.GRAY + ".");

		if (cooldown.containsKey(e.getEntity().getUniqueId())) {
			cooldown.remove(e.getEntity().getUniqueId());
		}

		if (village.containsValue(e.getEntity().getUniqueId())) {
			hasDied = true;
			if (count.get(e.getEntity().getUniqueId()) > 4 && count.get(e.getEntity().getUniqueId()) < 10) {
				FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()), 5000);
				Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId())
						.sendMessage(ChatColor.GRAY + "You have received " + ChatColor.RED + "$5,000 " + ChatColor.GRAY
								+ "for killing " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY
								+ ", who was in the " + ChatColor.RED + "village" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY
								+ " minutes.");
			} else if (count.get(e.getEntity().getUniqueId()) > 9 && count.get(e.getEntity().getUniqueId()) < 15) {
				FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()), 10000);
				Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId())
						.sendMessage(ChatColor.GRAY + "You have received " + ChatColor.RED + "$10,000 " + ChatColor.GRAY
								+ "for killing " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY
								+ ", who was in the " + ChatColor.RED + "village" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY
								+ " minutes.");
			} else if (count.get(e.getEntity().getUniqueId()) > 14) {
				FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()), 15000);
				Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId())
						.sendMessage(ChatColor.GRAY + "You have received " + ChatColor.RED + "$15,000 " + ChatColor.GRAY
								+ "for killing " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY
								+ ", who was in the " + ChatColor.RED + "village" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY
								+ " minutes.");
			}
			Bukkit.broadcastMessage(ChatColor.RED + e.getEntity().getKiller().getName() + ChatColor.GRAY
					+ " is now in control of the " + ChatColor.RED + "village " + ChatColor.GRAY + "at ("
					+ ChatColor.RED + (int) (e.getEntity().getKiller().getLocation().getX()) + ChatColor.GRAY + ", "
					+ ChatColor.RED + (int) (e.getEntity().getKiller().getLocation().getY()) + ChatColor.GRAY + ", "
					+ ChatColor.RED + (int) (e.getEntity().getKiller().getLocation().getZ()) + ChatColor.GRAY + ")!");
			village.put(0, e.getEntity().getKiller().getUniqueId());
			time.put(e.getEntity().getKiller().getUniqueId(), System.currentTimeMillis());
			count.put(e.getEntity().getKiller().getUniqueId(), 0);
		}

		if (quarry.containsValue(e.getEntity().getUniqueId())) {
			hasDied = true;
			if (count.get(e.getEntity().getUniqueId()) > 4 && count.get(e.getEntity().getUniqueId()) < 10) {
				FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()), 5000);
				Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId())
						.sendMessage(ChatColor.GRAY + "You have received " + ChatColor.RED + "$5,000 " + ChatColor.GRAY
								+ "for killing " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY
								+ ", who was in the " + ChatColor.RED + "quarry" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY
								+ " minutes.");
			} else if (count.get(e.getEntity().getUniqueId()) > 9 && count.get(e.getEntity().getUniqueId()) < 15) {
				FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()), 10000);
				Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId())
						.sendMessage(ChatColor.GRAY + "You have received " + ChatColor.RED + "$10,000 " + ChatColor.GRAY
								+ "for killing " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY
								+ ", who was in the " + ChatColor.RED + "quarry" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY
								+ " minutes.");
			} else if (count.get(e.getEntity().getUniqueId()) > 14) {
				FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()), 15000);
				Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId())
						.sendMessage(ChatColor.GRAY + "You have received " + ChatColor.RED + "$15,000 " + ChatColor.GRAY
								+ "for killing " + ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY
								+ ", who was in the " + ChatColor.RED + "quarry" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY
								+ " minutes.");
			}
			Bukkit.broadcastMessage(ChatColor.RED + e.getEntity().getKiller().getName() + ChatColor.GRAY
					+ " is now in control of the " + ChatColor.RED + "quarry " + ChatColor.GRAY + "at (" + ChatColor.RED
					+ (int) (e.getEntity().getKiller().getLocation().getX()) + ChatColor.GRAY + ", " + ChatColor.RED
					+ (int) (e.getEntity().getKiller().getLocation().getY()) + ChatColor.GRAY + ", " + ChatColor.RED
					+ (int) (e.getEntity().getKiller().getLocation().getZ()) + ChatColor.GRAY + ")!");
			quarry.put(0, e.getEntity().getKiller().getUniqueId());
			time.put(e.getEntity().getKiller().getUniqueId(), System.currentTimeMillis());
			count.put(e.getEntity().getKiller().getUniqueId(), 0);
		}
		/*
		 * else if (count.get(e.getEntity().getUniqueId()) > 19 &&
		 * count.get(e.getEntity().getUniqueId()) < 25) {
		 * FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().
		 * getUniqueId()), 30000);
		 * Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()).sendMessage(
		 * ChatColor.GRAY + "You have received " + ChatColor.RED + "$30,000 " +
		 * ChatColor.GRAY + "for killing " + ChatColor.RED + e.getEntity().getName() +
		 * ChatColor.GRAY + ", who was in the " + ChatColor.RED +
		 * region.get(e.getEntity().getUniqueId()) + ChatColor.GRAY + " for " +
		 * ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY +
		 * " minutes."); } else if (count.get(e.getEntity().getUniqueId()) > 24 &&
		 * count.get(e.getEntity().getUniqueId()) < 30) {
		 * FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().
		 * getUniqueId()), 35000);
		 * Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()).sendMessage(
		 * ChatColor.GRAY + "You have received " + ChatColor.RED + "$35,000 " +
		 * ChatColor.GRAY + "for killing " + ChatColor.RED + e.getEntity().getName() +
		 * ChatColor.GRAY + ", who was in the " + ChatColor.RED +
		 * region.get(e.getEntity().getUniqueId()) + ChatColor.GRAY + " for " +
		 * ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY +
		 * " minutes."); } else if (count.get(e.getEntity().getUniqueId()) > 29) {
		 * FyreCore.econ.depositPlayer(Bukkit.getPlayer(e.getEntity().getKiller().
		 * getUniqueId()), 40000);
		 * Bukkit.getPlayer(e.getEntity().getKiller().getUniqueId()).sendMessage(
		 * ChatColor.GRAY + "You have received " + ChatColor.RED + "$40,000 " +
		 * ChatColor.GRAY + "for killing " + ChatColor.RED + e.getEntity().getName() +
		 * ChatColor.GRAY + ", who was in the " + ChatColor.RED +
		 * region.get(e.getEntity().getUniqueId()) + ChatColor.GRAY + " for " +
		 * ChatColor.RED + count.get(e.getEntity().getUniqueId()) + ChatColor.GRAY +
		 * " minutes."); }
		 */
		// }
		hasDied = false;
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {

		if (cooldown.containsKey(e.getPlayer().getUniqueId())) {
			cooldown.remove(e.getPlayer().getUniqueId());
		}

		if (villageWait.containsKey(e.getPlayer().getUniqueId())) {
			i--;
			villageWait.remove(e.getPlayer().getUniqueId());
			for (UUID uuid : villageWait.keySet()) {
				if (villageWait.get(uuid) > i) {
					villageWait.put(uuid, villageWait.get(uuid) - 1);
				}
			}
		}

		if (quarryWait.containsKey(e.getPlayer().getUniqueId())) {
			i--;
			quarryWait.remove(e.getPlayer().getUniqueId());
			for (UUID uuid : quarryWait.keySet()) {
				if (quarryWait.get(uuid) > i) {
					quarryWait.put(uuid, quarryWait.get(uuid) - 1);
				}
			}
		}

		if (village.containsValue(e.getPlayer().getUniqueId())) {
			time.remove(e.getPlayer().getUniqueId());
			if ((count.get(e.getPlayer().getUniqueId()) % 5 == 0 && count.get(e.getPlayer().getUniqueId()) > 0)
					|| (count.get(e.getPlayer().getUniqueId()) % 5 > 0 && count.get(e.getPlayer().getUniqueId()) > 5)) {
				Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY
						+ " is no longer in control of the " + ChatColor.RED + "village" + ChatColor.GRAY + " after "
						+ ChatColor.RED + count.get(e.getPlayer().getUniqueId()) + ChatColor.GRAY + " minutes!");
			}
			count.remove(e.getPlayer().getUniqueId());
			village.remove(0);
			UUID u = null;
			for (UUID uuid : villageWait.keySet()) {
				if (villageWait.get(uuid) == 0) {
					u = uuid;
				}
			}
			Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getPlayer(u).getName() + ChatColor.GRAY
					+ "You are now in control of the " + ChatColor.RED + "village" + ChatColor.GRAY + "!");
			village.put(0, u);
			time.put(u, System.currentTimeMillis());
			count.put(u, 0);
		}

		if (quarry.containsValue(e.getPlayer().getUniqueId())) {
			time.remove(e.getPlayer().getUniqueId());
			if ((count.get(e.getPlayer().getUniqueId()) % 5 == 0 && count.get(e.getPlayer().getUniqueId()) > 0)
					|| (count.get(e.getPlayer().getUniqueId()) % 5 > 0 && count.get(e.getPlayer().getUniqueId()) > 5)) {
				Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY
						+ " is no longer in control of the " + ChatColor.RED + "quarry" + ChatColor.GRAY + " after "
						+ ChatColor.RED + count.get(e.getPlayer().getUniqueId()) + ChatColor.GRAY + " minutes!");
			}
			count.remove(e.getPlayer().getUniqueId());
			quarry.remove(0);
			UUID u = null;
			for (UUID uuid : quarryWait.keySet()) {
				if (quarryWait.get(uuid) == 0) {
					u = uuid;
				}
			}
			Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getPlayer(u).getName() + ChatColor.GRAY
					+ "You are now in control of the " + ChatColor.RED + "quarry" + ChatColor.GRAY + "!");
			quarry.put(0, u);
			time.put(u, System.currentTimeMillis());
			count.put(u, 0);
		}
	}

	public void spawnFirework(Location location) {
		Location loc = location;
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		fwm.addEffect(
				FireworkEffect.builder().flicker(false).trail(true).with(Type.BALL_LARGE).withColor(Color.RED).build());

		fwm.setPower(2);
		fw.setFireworkMeta(fwm);
	}

	@EventHandler
	public void onPearl(PlayerTeleportEvent e) {

		if (!(e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
			if (cooldown.containsKey(e.getPlayer().getUniqueId())) {
				cooldown.remove(e.getPlayer().getUniqueId());
			}
		}

		else {
			if (e.getTo().getBlock().getX() >= -76 && e.getTo().getBlock().getX() <= 76) {
				if (e.getTo().getBlock().getZ() >= -76 && e.getTo().getBlock().getZ() <= 76) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onCegg(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.MOB_SPAWNER) {
				ItemStack i = new ItemStack(Material.MONSTER_EGG, e.getPlayer().getItemInHand().getAmount(),
						(short) 50);
				if (e.getPlayer().getItemInHand().equals(i)) {
					World w = e.getClickedBlock().getWorld();
					Location loc = new Location(w, e.getClickedBlock().getX(), e.getClickedBlock().getY(),
							e.getClickedBlock().getZ());
					w.spawnEntity(loc, EntityType.CREEPER);

					i.setAmount(1);
					e.getPlayer().getInventory().removeItem(i);
				}
			}
		}
	}

	@Override
	public void run() {
		for (UUID uuid : time.keySet()) {
			if (System.currentTimeMillis() - time.get(uuid) >= 60000) {
				count.put(uuid, count.get(uuid) + 1);
				if (village.containsValue(uuid)) {

					if (count.get(uuid) == 1) {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "You have been in control of the " + ChatColor.RED
								+ "village" + ChatColor.GRAY + " for a minute.");
					} else {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "You have been in control of the " + ChatColor.RED
								+ "village" + ChatColor.GRAY + " for " + (count.get(uuid)) + " minutes.");
					}
					if ((count.get(uuid) - 1) < 5) {
						FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 5000);
						Bukkit.getPlayer(uuid).sendMessage(
								ChatColor.RED + "$5,000" + ChatColor.GRAY + " has been added to your account.");
					} else if ((count.get(uuid) - 1) < 10) {
						FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 7500);
						Bukkit.getPlayer(uuid).sendMessage(
								ChatColor.RED + "$7,500" + ChatColor.GRAY + " has been added to your account.");
					} else if ((count.get(uuid) - 1) >= 10) {
						FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 10000);
						Bukkit.getPlayer(uuid).sendMessage(
								ChatColor.RED + "$10,000" + ChatColor.GRAY + " has been added to your account.");
					}
				}

				if (quarry.containsValue(uuid)) {

					if (count.get(uuid) == 1) {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "You have been in control of the " + ChatColor.RED
								+ "quarry" + ChatColor.GRAY + " for a minute.");
					} else {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.GRAY + "You have been in control of the " + ChatColor.RED
								+ "quarry" + ChatColor.GRAY + " for " + (count.get(uuid)) + " minutes.");
					}
					if ((count.get(uuid) - 1) < 5) {
						FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 5000);
						Bukkit.getPlayer(uuid).sendMessage(
								ChatColor.RED + "$5,000" + ChatColor.GRAY + " has been added to your account.");
					} else if ((count.get(uuid) - 1) < 10) {
						FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 7500);
						Bukkit.getPlayer(uuid).sendMessage(
								ChatColor.RED + "$7,500" + ChatColor.GRAY + " has been added to your account.");
					} else if ((count.get(uuid) - 1) >= 10) {
						FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 10000);
						Bukkit.getPlayer(uuid).sendMessage(
								ChatColor.RED + "$10,000" + ChatColor.GRAY + " has been added to your account.");
					}
				}

				/*
				 * else if ((count.get(uuid) - 1) < 20) {
				 * FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 25000);
				 * Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "$25,000" + ChatColor.GRAY
				 * + " has been added to your account."); } else if ((count.get(uuid) - 1) < 25)
				 * { FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 30000);
				 * Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "$30,000" + ChatColor.GRAY
				 * + " has been added to your account."); } else if ((count.get(uuid) - 1) < 30)
				 * { FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 35000);
				 * Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "$35,000" + ChatColor.GRAY
				 * + " has been added to your account."); } else if ((count.get(uuid) - 1) >=
				 * 30) { FyreCore.econ.depositPlayer(Bukkit.getPlayer(uuid), 40000);
				 * Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "$40,000" + ChatColor.GRAY
				 * + " has been added to your account."); }
				 */
				time.put(uuid, System.currentTimeMillis());
				if (count.get(uuid) % 5 == 0) {
					if (village.containsValue(uuid)) {
						Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getPlayer(uuid).getName() + ChatColor.GRAY
								+ " has been in control of the " + ChatColor.RED + "village" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(uuid) + ChatColor.GRAY + " minutes! They are at (" + ChatColor.RED
								+ (int) (Bukkit.getPlayer(uuid).getLocation().getX()) + ChatColor.GRAY + ", " + ChatColor.RED
								+ (int) (Bukkit.getPlayer(uuid).getLocation().getY()) + ChatColor.GRAY + ", " + ChatColor.RED
								+ (int) (Bukkit.getPlayer(uuid).getLocation().getZ()) + ChatColor.GRAY + ")!");
						spawnFirework(Bukkit.getPlayer(uuid).getLocation());
					}

					if (quarry.containsValue(uuid)) {
						Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getPlayer(uuid).getName() + ChatColor.GRAY
								+ " has been in control of the " + ChatColor.RED + "quarry" + ChatColor.GRAY + " for "
								+ ChatColor.RED + count.get(uuid) + ChatColor.GRAY + " minutes! They are at (" + ChatColor.RED
										+ (int) (Bukkit.getPlayer(uuid).getLocation().getX()) + ChatColor.GRAY + ", " + ChatColor.RED
										+ (int) (Bukkit.getPlayer(uuid).getLocation().getY()) + ChatColor.GRAY + ", " + ChatColor.RED
										+ (int) (Bukkit.getPlayer(uuid).getLocation().getZ()) + ChatColor.GRAY + ")!");
						spawnFirework(Bukkit.getPlayer(uuid).getLocation());
					}
				}
			}
		}
	}
}