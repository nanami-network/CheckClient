package xyz.n7mn.dev.checkclient.type;

public enum ClientType {
    FORGE_1_13_ABOVE("Forge"),
    FORGE_1_13_BELOW("Forge"),
    VANILLA("Vanilla"),
    LUNAR("Lunar Client"),
    FEATHER_FORGE("Feather Forge"),
    FEATHER_FABRIC("Feather Fabric"),
    FABRIC("Fabric"),
    PVP_LOUNGE("PVP Lounge Client"),
    LABY_MOD("Laby Mod"),
    UNKNOWN("Unknown");

    private String name;

    ClientType(String name) {
        this.name = name;
    }

    public String getClientName() {
        return name;
    }
}
