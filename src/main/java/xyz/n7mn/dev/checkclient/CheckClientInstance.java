package xyz.n7mn.dev.checkclient;

import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.Bukkit;
import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.check.impl.brand.*;
import xyz.n7mn.dev.checkclient.check.impl.brand.feather.FeatherFabric;
import xyz.n7mn.dev.checkclient.check.impl.brand.feather.FeatherForge;
import xyz.n7mn.dev.checkclient.check.impl.brand.forge.Forge_V_1_12;
import xyz.n7mn.dev.checkclient.check.impl.brand.forge.Forge_V_1_13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum CheckClientInstance {

    INSTANCE;
    private CheckClient plugin = (CheckClient) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("CheckClient"));

    private List<CheckMCBrandAbstract> MCBrands = new ArrayList<>(
            Arrays.asList(new BlueBerry(), new Fabric(), new LunarClient(), new PvPLoungeClient(), new Vanilla()
                    , new FeatherForge(), new FeatherFabric(), new Forge_V_1_12(), new Forge_V_1_13()));

    public CheckClient getPlugin() {
        return plugin;
    }

    public void setPlugin(CheckClient plugin) {
        this.plugin = plugin;
    }

    public List<CheckMCBrandAbstract> getMCBrands() {
        return MCBrands;
    }

    public void setMCBrands(List<CheckMCBrandAbstract> MCBrands) {
        this.MCBrands = MCBrands;
    }
}