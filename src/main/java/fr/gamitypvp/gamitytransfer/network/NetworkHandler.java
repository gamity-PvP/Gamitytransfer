package fr.gamitypvp.gamitytransfer.network;

import fr.gamitypvp.gamitytransfer.network.client.ClientOnlyHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("gamitytransfer", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        CHANNEL.registerMessage(
                0,
                RedirectPacket.class,
                RedirectPacket::encode,
                RedirectPacket::decode,
                (packet, contextSupplier) -> DistExecutor.safeRunWhenOn(
                        Dist.CLIENT,
                        ()-> ()-> ClientOnlyHandler.registerClientHandler(packet, contextSupplier)
                )
        );
    }
}
