package xyz.n7mn.dev.checkclient.check.impl.brand.forge;

import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class Forge_V_1_13 extends CheckMCBrandAbstract {
    @Override
    public ClientType run(PlayerData data, String brand) {
        return brand.equalsIgnoreCase("forge") ? ClientType.FORGE_1_13_ABOVE : ClientType.UNKNOWN;
    }
}