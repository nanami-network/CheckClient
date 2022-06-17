package xyz.n7mn.dev.checkclient.data.impl;

import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class ClientData {

    private PlayerData data;

    private boolean sendedHandShake, receivedHandShake, isReceivedMCBrand, isReceivedLunarChannel;

    public ClientData(PlayerData data) {
        this.data = data;
    }

    private ClientType type = ClientType.UNKNOWN;

    private ClientType MCBrandResult = ClientType.UNKNOWN;

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public boolean isFeatherClient() {
        return type == ClientType.FEATHER_FABRIC || type == ClientType.FEATHER_FORGE;
    }

    public boolean isForge() {
        return type == ClientType.FORGE_1_13_BELOW || type == ClientType.FEATHER_FABRIC;
    }

    public boolean isSendedHandShake() {
        return sendedHandShake;
    }

    public void setSendedHandShake(boolean sendedHandShake) {
        this.sendedHandShake = sendedHandShake;
    }

    public boolean isReceivedHandShake() {
        return receivedHandShake;
    }

    public void setReceivedHandShake(boolean receivedHandShake) {
        this.receivedHandShake = receivedHandShake;
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

    public ClientType getMCBrandResult() {
        return MCBrandResult;
    }

    public void setMCBrandResult(ClientType MCBrandResult) {
        this.MCBrandResult = MCBrandResult;
    }
}
