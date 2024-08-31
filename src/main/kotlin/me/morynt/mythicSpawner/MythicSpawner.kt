package me.morynt.mythicSpawner

import me.morynt.mythicSpawner.commands.LightSpawners
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class MythicSpawner : JavaPlugin() {
    companion object {
        lateinit var inst: MythicSpawner private set
    }


    override fun onEnable() {
        inst = this
        dataFolder.mkdirs()
        SpawnerManager.init()
        LightSpawners().register()
        logger.info("<---------------------- ! Plugin Has Started ! ---------------------->")
        Scheduler()
        saveResource("spawnerconfig", false)
        saveResource("spawnerconfig/sheepBomb.yml", false)
    }

    override fun onDisable() {
    }

    fun Scheduler() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            SpawnerManager.spawnerMap.keys.forEach { spawnerList ->
                SpawnerManager.spawnerMap.get(spawnerList)?.let { spawnerData ->
                    spawnerData.mobSet.removeIf { it.isDead }
                    if (System.currentTimeMillis() - spawnerData.lastUsage >= spawnerData.cooldown) {
                        if (spawnerData.mobSet.isNotEmpty()) return@let
                        if (spawnerData.enabled == true) {
                            Bukkit.getOnlinePlayers().forEach { player ->
                                var distance: Double
                                try {
                                    distance = player.location.distance(
                                        Location(
                                            Bukkit.getWorld(spawnerData.world),
                                            spawnerData.x.toDouble(),
                                            spawnerData.y.toDouble(),
                                            spawnerData.z.toDouble()
                                        )
                                    )
                                } catch (e: IllegalArgumentException) {
                                    return@Runnable
                                }
                                if (distance <= spawnerData.radius) {
                                    spawnerData.spawn(Random.nextInt(spawnerData.minMob, spawnerData.maxMob + 1))
                                }
                            }
                        } else {
                            spawnerData.spawn(Random.nextInt(spawnerData.minMob, spawnerData.maxMob + 1))
                        }
                    }

                }
            }
        }, 0L, 20 * 1L)
    }
}
