package me.morynt.mythicSpawner

import SpawnerData
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object SpawnerManager {
    val folder = File(MythicSpawner.inst.dataFolder, "spawnerconfig")
    var spawnerMap = HashMap<String, SpawnerData>()

    fun init() {
        if (!folder.exists()) {
            folder.mkdirs()
        }
        if (folder.isDirectory) {
            folder.listFiles { _, name -> name.endsWith(".yml") }?.forEach { file ->
                val ymlFile = YamlConfiguration.loadConfiguration(file)
                val spawnID = ymlFile.getKeys(false).first()

                spawnerMap.put(spawnID, SpawnerData(
                    ymlFile.getString("${spawnID}.mob")?: "NONE",
                    ymlFile.getString("${spawnID}.location.world")?: "world",
                    ymlFile.getInt("${spawnID}.location.x"),
                    ymlFile.getInt("${spawnID}.location.y"),
                    ymlFile.getInt("${spawnID}.location.z"),
                    ymlFile.getInt("${spawnID}.location.yaw"),
                    ymlFile.getInt("${spawnID}.location.pitch"),
                    ymlFile.getInt("${spawnID}.count.minCount"),
                    ymlFile.getInt("${spawnID}.count.maxCounter"),
                    ymlFile.getBoolean("${spawnID}.require_player.enabled"),
                    ymlFile.getInt("${spawnID}.require_player.count"),
                    ymlFile.getInt("${spawnID}.require_player.radius"),
                    ymlFile.getInt("${spawnID}.cooldown") * 1000,
                    ymlFile.getInt("${spawnID}.chance")
                ))
            }
        }
    }
}
