package xyz.n7mn.dev.checkclient.action;

import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

import java.util.ArrayList;
import java.util.List;

public class ImpossibleAction {

    private final List<ImpossibleActionEnum> logs = new ArrayList<>();

    public List<ImpossibleActionEnum> dump(PlayerData data) {
        logs.clear();

        if (!data.getClientData().isReceivedMCBrand()) {
            logs.add(ImpossibleActionEnum.NOT_RECEIVED_MC_BRAND);
        }

        if (data.getClientData().getType() == ClientType.LUNAR && !data.getClientData().isReceivedLunarChannel()) {
            logs.add(ImpossibleActionEnum.NOT_RECEIVED_LUNAR_CHANNEL);
        } else if (data.getClientData().getType() != ClientType.LUNAR && data.getClientData().isReceivedLunarChannel()) {
            logs.add(ImpossibleActionEnum.RECEIVED_LUNAR_CHANNEL);
        }

        if (data.getClientData().isFeatherClient() && !data.getModData().isReceivedFeatherModData()) {
            logs.add(ImpossibleActionEnum.NOT_RECEIVED_FEATHER_MOD_DATA);
        } else if (!data.getClientData().isFeatherClient() && data.getModData().isReceivedFeatherModData()) {
            logs.add(ImpossibleActionEnum.RECEIVED_FEATHER_MOD_DATA);
        }

        if (data.getClientData().getType() == ClientType.MODIFIED_CLIENT) {
            logs.add(ImpossibleActionEnum.MODIFIED_CLIENT);
        }

        if (data.getClientData().isForge() && data.getClientData().isSendedHandShake() && !data.getClientData().isReceivedHandShake()) {
            logs.add(ImpossibleActionEnum.NOT_RECEIVED_FORGE_HANDSHAKE);
        }

        return logs;
    }

    public enum ImpossibleActionEnum {
        NOT_RECEIVED_MC_BRAND,
        NOT_RECEIVED_LUNAR_CHANNEL,
        RECEIVED_LUNAR_CHANNEL,
        NOT_RECEIVED_FEATHER_MOD_DATA,
        RECEIVED_FEATHER_MOD_DATA,
        NOT_RECEIVED_FORGE_HANDSHAKE,
        MODIFIED_CLIENT,
    }
}