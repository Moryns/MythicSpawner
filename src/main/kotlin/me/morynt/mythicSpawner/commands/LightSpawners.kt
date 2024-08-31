package me.morynt.mythicSpawner.commands

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import me.morynt.mythicSpawner.configReload
import me.morynt.mythicSpawner.fileFormat
import org.bukkit.entity.Player

class LightSpawners : CommandTree("lightspawners") {
    override fun register() {
        withPermission("lightspawners.command")
            .then(
                LiteralArgument("create")
                    .withPermission("lightspawners.command.create")
                    .then(
                        StringArgument("id")
                            .then(
                                StringArgument("mob")
                                    .then(
                                        IntegerArgument("cooldown")
                                            .then(
                                                IntegerArgument("chance")
                                                    .then(
                                                        IntegerArgument("min-count")
                                                            .then(
                                                                IntegerArgument("max-count")
                                                                    .executesPlayer(PlayerCommandExecutor { player, args ->
                                                                        fileFormat(
                                                                            args.get("id") as String,
                                                                            args.get("mob") as String,
                                                                            player.world,
                                                                            player.location.x,
                                                                            player.location.y,
                                                                            player.location.z,
                                                                            args.get("min-count") as Int,
                                                                            args.get("max-count") as Int,
                                                                            args.get("chance") as Int,
                                                                            args.get("cooldown") as Int
                                                                        )
                                                                        player.sendMessage(" §2> §aВы успешно создали новый спавнер.")
                                                                    })
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            ).then(
                LiteralArgument("reload")
                    .withPermission("lightspawners.command")
                    .executes(CommandExecutor { sender, args ->
                        if (sender is Player) {
                            sender.sendMessage(" §2> §aВы успешно перезагрузили конфиг плагина.")
                        } else {
                            println("> Конфиг был успешно перезагружен")
                        }
                        configReload()
                    })
            )
        super.register()
    }
}