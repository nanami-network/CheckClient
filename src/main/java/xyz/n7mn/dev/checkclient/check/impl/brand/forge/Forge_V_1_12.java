package xyz.n7mn.dev.checkclient.check.impl.brand.forge;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.custompayload.WrappedPacketOutCustomPayload;
import xyz.n7mn.dev.checkclient.check.CheckMCBrandAbstract;
import xyz.n7mn.dev.checkclient.data.PlayerData;
import xyz.n7mn.dev.checkclient.type.ClientType;

public class Forge_V_1_12 extends CheckMCBrandAbstract {

    @Override
    public ClientType run(PlayerData data, String brand) {
        if (brand.equalsIgnoreCase("fml:forge")) {

            if (!data.getClientData().isSendedHandShake()) {
                PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), new WrappedPacketOutCustomPayload("FML|HS", new byte[]{-2, 0}));
                PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), new WrappedPacketOutCustomPayload("FML|HS", new byte[]{0, 2, 0, 0, 0, 0}));
                PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), new WrappedPacketOutCustomPayload("FML|HS", new byte[]{2, 0, 0, 0, 0}));

                data.getClientData().setSendedHandShake(true);
            }

            return ClientType.FORGE_1_13_BELOW;
        } else {
            return ClientType.UNKNOWN;
        }
    }
}
