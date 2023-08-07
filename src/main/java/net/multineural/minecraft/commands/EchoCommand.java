package net.multineural.minecraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class EchoCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("echo")
                .requires(source -> source.hasPermissionLevel(0)) // Require op level 2 (operator)
                .then(CommandManager.argument("value", IntegerArgumentType.integer())
                        .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "value"))));

        dispatcher.register(command);
    }

    private static int execute(ServerCommandSource source, int value) {
        source.sendFeedback(() -> Text.of("Echo: " + value), false);
        return 1;
    }

}
