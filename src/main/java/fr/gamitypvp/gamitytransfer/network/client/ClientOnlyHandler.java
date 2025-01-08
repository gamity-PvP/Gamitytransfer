package fr.gamitypvp.gamitytransfer.network.client;

import fr.gamitypvp.gamitytransfer.network.RedirectPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientOnlyHandler {
    public static void registerClientHandler(RedirectPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT,() -> {
            contextSupplier.get().enqueueWork(() -> {
                net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
                minecraft.player.connection.getConnection().disconnect(
                        new StringTextComponent("Redirecting to " + packet.getIp() + ":" + packet.getPort())
                );
                minecraft.setScreen(new net.minecraft.client.gui.screen.ConnectingScreen(
                        minecraft.screen,
                        minecraft,
                        new net.minecraft.client.multiplayer.ServerData("Server", packet.getIp() + ":" + packet.getPort(), false)
                ));
            });
            contextSupplier.get().setPacketHandled(true);
            return null;
        });
    }
}
