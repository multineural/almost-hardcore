package net.multineural.minecraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.multineural.minecraft.PlayerEffect;

public class TotemCommand {

    // player runs /totem ## and it does the undying effect while decrementing the lives left
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("totem")
                .requires(source -> source.hasPermissionLevel(0)) // Require op level 2 (operator)
                .then(CommandManager.argument("livesToBurn", IntegerArgumentType.integer())
                        .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "livesToBurn"))));

        dispatcher.register(command);
    }

    private static int execute(ServerCommandSource source, int livesToBurn) {
        PlayerEffect.doUndyingEffect(source.getPlayer(), livesToBurn);
        return 1;
    }

}
