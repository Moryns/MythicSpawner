import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.mobs.ActiveMob
import me.morynt.mythicSpawner.MythicSpawner
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import java.util.concurrent.CompletableFuture
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

data class SpawnerData(
    val mob: String,
    val world: String,
    val x: Int,
    val y: Int,
    val z: Int,
    val yaw: Int,
    val pitch: Int,
    val minMob: Int,
    val maxMob: Int,
    val enabled: Boolean,
    val count: Int,
    val radius: Int,
    val cooldown: Int,
    val chance: Int
) {
    var lastUsage: Long = System.currentTimeMillis()
    var mobSet: MutableSet<ActiveMob> = mutableSetOf()

    fun spawn(countMob: Int) {
        if (Random.nextInt(0, 101) <= chance) {
            val world = Bukkit.getWorld(world) ?: return
            for (l in 1..countMob) {
                val locationFuture = findPotentialSpawn(world)
                spawnMob(locationFuture)
            }
        }
        lastUsage = System.currentTimeMillis()
    }

    fun findPotentialSpawn(world: World): CompletableFuture<Location?> {
        return CompletableFuture.supplyAsync {
            for (j in 0..5) {
                val potentialLocation = Location(
                    world,
                    (x - Random.nextInt(-10, 11)).toDouble(),
                    (y - 5).toDouble(),
                    (z - Random.nextInt(-10, 11)).toDouble()
                )
                var n = 0
                while (world.getBlockAt(potentialLocation).type != Material.AIR && n < 10) {
                    potentialLocation.y += 1
                    n++
                }
                if (world.getBlockAt(potentialLocation).type == Material.AIR) {
                    return@supplyAsync potentialLocation
                }
            }
            null
        }
    }

    fun spawnMob(locationFuture: CompletableFuture<Location?>) {
        locationFuture.thenAccept { location ->
            if (location != null) {
                Bukkit.getScheduler().runTask(MythicSpawner.inst, Runnable runTask@{
                    val mythicMob = MythicBukkit.inst().mobManager.getMythicMob(mob).getOrNull()?: return@runTask
                    mobSet.add(mythicMob.spawn(BukkitAdapter.adapt(location), 1.0))
                })
            }
        }
    }
}
