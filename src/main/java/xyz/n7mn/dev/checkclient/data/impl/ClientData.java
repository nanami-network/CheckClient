package xyz.n7mn.dev.checkclient.data.impl;

import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class ClientData {

    private PlayerData data;

    private boolean sendedHandShake, isReceivedMCBrand, isReceivedLunarChannel;

    public ClientData(PlayerData data) {
        this.data = data;
    }

    private ClientType type = ClientType.UNKNOWN;

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public boolean isSendedHandShake() {
        return sendedHandShake;
    }

    public void setSendedHandShake(boolean sendedHandShake) {
        this.sendedHandShake = sendedHandShake;
    }

    public boolean isReceivedMCBrand() {
        return isReceivedMCBrand;
    }

    public void setReceivedMCBrand(boolean receivedMCBrand) {
        isReceivedMCBrand = receivedMCBrand;
    }

    public boolean isReceivedLunarChannel() {
        return isReceivedLunarChannel;
    }

    public void setReceivedLunarChannel(boolean receivedLunarChannel) {
        isReceivedLunarChannel = receivedLunarChannel;
    }
}
