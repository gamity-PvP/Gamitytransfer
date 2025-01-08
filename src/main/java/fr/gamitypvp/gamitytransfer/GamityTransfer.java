package fr.gamitypvp.gamitytransfer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.gamitypvp.gamitytransfer.network.NetworkHandler;
import fr.gamitypvp.gamitytransfer.network.RedirectPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod("gamitytransfer")
public class GamityTransfer {
    public GamityTransfer() {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkHandler.registerMessages();
    }
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.registerMessages();
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("transfer")
                        .requires(source -> source.hasPermission(2)) // Commande réservée aux OPs
                        .then(Commands.argument("ip", StringArgumentType.word())
                                .then(Commands.argument("port", IntegerArgumentType.integer(1, 65535))
                                        .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getPlayerOrException();
                                            String ip = StringArgumentType.getString(context, "ip");
                                            int port = IntegerArgumentType.getInteger(context, "port");
                                            NetworkHandler.CHANNEL.sendTo(
                                                    new RedirectPacket(ip, port),
                                                    player.connection.connection,
                                                    NetworkDirection.PLAY_TO_CLIENT
                                            );
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
