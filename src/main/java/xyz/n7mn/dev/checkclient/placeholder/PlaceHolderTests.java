package xyz.n7mn.dev.checkclient.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceHolderTests extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "client_types";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Kouta1212";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        //todos
        return null;
    }
}
