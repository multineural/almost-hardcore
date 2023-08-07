package net.multineural.minecraft.persist;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.multineural.minecraft.AlmostHardcoreMod;

import java.util.HashMap;
import java.util.UUID;

public class ServerPersistentState extends PersistentState {

    public static final String PROP_NAME_NUMLIVES = AlmostHardcoreMod.PROP_NAME_MOD_ID + "-lives";
    public static final String PROP_NAME_PLAYERS_NBT = AlmostHardcoreMod.PROP_NAME_MOD_ID + "-players";

    // stores the playerId with their number of lives left.
    // convert that to a pojo later if we need more fields
    HashMap<UUID, PlayerPojo> players = new HashMap<>();

    public HashMap<UUID, PlayerPojo> getPlayers() {
        return players;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {

        AlmostHardcoreMod.LOGGER.info(">>>> writeNbt method...");

        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            NbtCompound playerStateNbt = new NbtCompound();

            // ANYTIME YOU PUT NEW DATA IN THE PlayerState CLASS YOU NEED TO REFLECT THAT HERE!!!
            playerStateNbt.putInt(PROP_NAME_NUMLIVES, playerSate.getNumLives());

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.put(PROP_NAME_PLAYERS_NBT, playersNbtCompound);

        return nbt;
    }


    public int getNumLives(UUID playerId) {

        AlmostHardcoreMod.LOGGER.info(">>>> getNumLives method, playerId=" + playerId);

        PlayerPojo pojo = players.get(playerId);
        if(pojo == null) {
            AlmostHardcoreMod.LOGGER.info(">>>> PLAYER POJO IS NULL!!");
        }

        int retval = pojo.getNumLives();

        return retval;
    }


    public void setNumLives(UUID playerId, int numLives) {

        AlmostHardcoreMod.LOGGER.info(">>>> setNumLives method...");

        players.get(playerId).setNumLives(numLives);
        markDirty();
    }


    ////////////////////////// PUBLIC STATIC STUFF ////////////////////////

    public static ServerPersistentState createFromNbt(NbtCompound tag) {

        AlmostHardcoreMod.LOGGER.info(">>>> createFromNbt method...");
        ServerPersistentState serverPersistentState = new ServerPersistentState();
        NbtCompound playersNbt = tag.getCompound(PROP_NAME_PLAYERS_NBT);

        playersNbt.getKeys().forEach(key -> {
            PlayerPojo playerPojo = new PlayerPojo();
            UUID uuid = UUID.fromString(key);

            playerPojo.setNumLives(playersNbt.getCompound(key).getInt(PROP_NAME_NUMLIVES));
            AlmostHardcoreMod.LOGGER.info(">>>> updated number of lives for uuid: " + uuid.toString());
            serverPersistentState.players.put(uuid, playerPojo);
        });

        return serverPersistentState;
    }


    public static ServerPersistentState getOrCreate(MinecraftServer server) {

        AlmostHardcoreMod.LOGGER.info(">>>> getOrCreate method...");

        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager =
                server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        ServerPersistentState serverState = persistentStateManager.getOrCreate(
                ServerPersistentState::createFromNbt,
                ServerPersistentState::new,
                AlmostHardcoreMod.PROP_NAME_MOD_ID);

        return serverState;
    }

    public static PlayerPojo getPlayerPojo(LivingEntity player) {

        AlmostHardcoreMod.LOGGER.info(">>>> getPlayerPojo method...");

        ServerPersistentState serverPersistentState = getOrCreate(player.getWorld().getServer());

        // Either get the player by the uuid, or we don't have data for him yet, make a new player state
        PlayerPojo playerPojo = serverPersistentState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerPojo());

        return playerPojo;
    }


}
