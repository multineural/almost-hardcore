package net.multineural.minecraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.multineural.minecraft.persist.ServerPersistentState;

public class LivesCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("lives")
                .executes(context -> execute(context.getSource()));

        dispatcher.register(command);
    }

    // player runs /lives and it returns the number of totems/lives left
    private static int execute(ServerCommandSource source) {

        PlayerEntity player = source.getPlayer();
        String playerId = player.getUuid().toString();

        ServerPersistentState serverPersistentState =
                ServerPersistentState.getOrCreate(source.getPlayer().getServer());
        int lives = serverPersistentState.getNumLives(player.getUuid());

        String text = "lives";
        if (lives == 1) {
            text = "life";
        }
        player.sendMessage(Text.of(String.format("You have %s %s left ", lives, text)));

        return 1; // this is the callback status, not the numLives        return 1;
    }

}
