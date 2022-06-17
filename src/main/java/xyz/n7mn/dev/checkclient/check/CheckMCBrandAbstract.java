package xyz.n7mn.dev.checkclient.check;

import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.data.impl.ClientData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public abstract class CheckMCBrandAbstract {
    public abstract ClientType run(PlayerData data, String brand);
}