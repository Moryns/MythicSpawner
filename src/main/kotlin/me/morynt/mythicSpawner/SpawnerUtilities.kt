package me.morynt.mythicSpawner

import SpawnerData
import me.morynt.mythicSpawner.SpawnerManager.folder
import me.morynt.mythicSpawner.SpawnerManager.spawnerMap
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

fun fileFormat(id: String, mob: String, world: World,x: Double, y: Double, z: Double, minCount: Int, maxCount: Int, chance: Int, cooldown: Int  ) {
    val filePath = File(folder, "${id}.yml")
    filePath.createNewFile()
    val file = YamlConfiguration.loadConfiguration(filePath)
    file.set("${id}.mob", mob)
    file.set("${id}.location.world", world.name)
    file.set("${id}.location.x", x.toInt())
    file.set("${id}.location.y", y.toInt())
    file.set("${id}.location.z", z.toInt())
    file.set("${id}.location.yaw", 0)
    file.set("${id}.location.pitch", 0)
    file.set("${id}.count.minCount", minCount)
    file.set("${id}.count.maxCounter", maxCount)
    file.set("${id}.require_player.enabled", true)
    file.set("${id}.require_player.count", 1)
    file.set("${id}.require_player.radius", 10)
    file.set("${id}.cooldown", cooldown)
    file.set("${id}.chance", chance)
    file.save(filePath)
    configReload()
}

fun configReload() {
    spawnerMap.clear()
    SpawnerManager.init()
}
