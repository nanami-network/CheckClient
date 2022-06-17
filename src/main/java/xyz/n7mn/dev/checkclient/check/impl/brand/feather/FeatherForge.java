package xyz.n7mn.dev.checkclient.check.impl.brand.feather;

import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class FeatherForge extends CheckMCBrandAbstract {
    @Override
    public ClientType run(PlayerData data, String brand) {
        return brand.equalsIgnoreCase("Feather Forge") ? ClientType.FEATHER_FORGE : ClientType.UNKNOWN;
    }
}
