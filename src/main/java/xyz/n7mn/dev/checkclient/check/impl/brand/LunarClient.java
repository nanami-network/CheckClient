package xyz.n7mn.dev.checkclient.check.impl.brand;

import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class LunarClient extends CheckMCBrandAbstract {

    @Override
    public ClientType run(PlayerData data, String brand) {
        return brand.startsWith("lunar") ? ClientType.LUNAR : ClientType.UNKNOWN;
    }
}