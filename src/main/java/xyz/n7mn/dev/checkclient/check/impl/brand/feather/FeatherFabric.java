package xyz.n7mn.dev.checkclient.check.impl.brand.feather;

import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class FeatherFabric extends CheckMCBrandAbstract {
    @Override
    public ClientType run(PlayerData data, String brand) {
        return brand.equalsIgnoreCase("Feather Fabric") ? ClientType.FEATHER_FABRIC : ClientType.UNKNOWN;
    }
}