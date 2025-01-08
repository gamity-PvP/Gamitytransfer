package fr.gamitypvp.gamitytransfer.network;

import net.minecraft.network.PacketBuffer;

public class RedirectPacket {
    private final String ip;
    private final int port;

    public RedirectPacket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public static void encode(RedirectPacket packet, PacketBuffer buffer) {
        buffer.writeUtf(packet.ip);
        buffer.writeInt(packet.port);
    }

    public static RedirectPacket decode(PacketBuffer buffer) {
        return new RedirectPacket(buffer.readUtf(32767), buffer.readInt());
    }
}
