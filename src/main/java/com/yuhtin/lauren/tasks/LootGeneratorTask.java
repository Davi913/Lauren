package com.yuhtin.lauren.tasks;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.yuhtin.lauren.Lauren;
import com.yuhtin.lauren.core.logger.Logger;
import com.yuhtin.lauren.core.player.Player;
import com.yuhtin.lauren.core.player.controller.PlayerController;
import com.yuhtin.lauren.models.enums.LogType;
import com.yuhtin.lauren.utils.helper.TaskHelper;
import com.yuhtin.lauren.utils.helper.Utilities;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LootGeneratorTask {

    @Getter
    private final EventWaiter eventWaiter = new EventWaiter();
    private static final LootGeneratorTask INSTANCE = new LootGeneratorTask();

    public static LootGeneratorTask getInstance() {
        return INSTANCE;
    }

    public void startRunnable() {
        Logger.log("Registered LootGeneratorTask");

        final List<Long> allowedChannels = Arrays.asList(
                765024746655318016L,
                763757066103554069L,
                721037764321345578L,
                700673056414367825L
        );

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Loot Radiante", null, "https://cdn.discordapp.com/emojis/724759930653114399.png?v=1");
        embed.setFooter("© ^Aincrad™ servidor de jogos", Lauren.getInstance().getGuild().getIconUrl());

        embed.setThumbnail("https://www.pcguia.pt/wp-content/uploads/2019/11/lootbox.jpg");
        embed.setColor(Color.ORANGE);

        String arrow = "   <:seta:771540348157689886>";
        embed.setDescription(
                "Você encontrou uma **lootbox**, seja o primeiro a reajir\n" +
                        "no ícone abaixo para garantir prêmios aleatórios\n" +
                        "\n" +
                        " \uD83C\uDFC6 Prêmios:\n" +
                        arrow + " Experiência **(Garantido)**\n" +
                        arrow + " Pontos de Patente\n" +
                        arrow + " Dinheiro\n" +
                        arrow + " Cargo Sortudo"
        );

        TaskHelper.runTaskTimerAsync(new TimerTask() {
            @Override
            public void run() {
                Logger.log("Running LootGeneratorTask");

                if (new Random().nextInt(100) > 7) return;


                int value = new Random().nextInt(allowedChannels.size());
                long channelID = allowedChannels.get(value);

                TextChannel channel = Lauren.getInstance().getGuild().getTextChannelById(channelID);

                if (channel == null) {
                    Logger.log("Can't select a random channel to drop a loot", LogType.ERROR);
                    return;
                }

                Logger.log("Dropped loot on channel " + channel.getName());

                Message message = channel.sendMessage(embed.build()).complete();
                message.addReaction(":radiante:771541052590915585").queue();

                eventWaiter.waitForEvent(MessageReactionAddEvent.class, event -> !event.getMember().getUser().isBot(),
                        event -> {
                            message.clearReactions().queue();

                            String nickname = event.getMember().getNickname();
                            if (nickname == null) nickname = event.getMember().getEffectiveName();

                            channel.sendMessage("<:felizpakas:742373250037710918> " +
                                    "Parabéns **" + nickname + "**, você capturou uma lootbox, você pode abrir ela mais tarde " +
                                    "usando `$openloot`")
                                    .queue();

                            Player player = PlayerController.INSTANCE.get(event.getUserIdLong());
                            player.setLootBoxes(player.getLootBoxes() + 1);

                            Logger.log("The player " + Utilities.INSTANCE.getFullName(event.getUser()) + " getted the drop");
                        }, 10, TimeUnit.SECONDS,

                        () -> {
                            message.editMessage("<a:tchau:751941650728747140> " +
                                    "Infelizmente acabou o tempo e ninguém coletou o loot.")
                                    .queue();
                            message.clearReactions().queue();
                        });
            }
        }, 10, 30, TimeUnit.MINUTES);
    }
}
