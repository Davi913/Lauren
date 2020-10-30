package com.yuhtin.lauren.commands.help;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.yuhtin.lauren.Lauren;
import com.yuhtin.lauren.models.annotations.CommandHandler;
import com.yuhtin.lauren.service.PterodactylConnection;
import com.yuhtin.lauren.utils.helper.MathUtils;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@CommandHandler(
        name = "info",
        type = CommandHandler.CommandType.HELP,
        description = "Veja um pouco mais sobre mim",
        alias = {"info", "binfo"})
public class InfoCommand extends Command {

    @SneakyThrows
    @Override
    protected void execute(CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        User user = event.getJDA().getUserById(272879983326658570L);
        String authorBot = user == null ? bot.getName() + "#" + bot.getDiscriminator() : user.getName() + "#" + user.getDiscriminator();
        OffsetDateTime timeCreated = bot.getTimeCreated();

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor("Informações sobre a bot mais linda do mundo", null, bot.getAvatarUrl())

                .addField("📆 Criado em", "`" + timeCreated.getDayOfMonth() + " de " + timeCreated.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) + ", "
                        + timeCreated.getYear() + " às " + timeCreated.getHour() + ":" + timeCreated.getMinute() + "`", true)
                .addField("<a:feliz_2:726220815749611603> Versão atual", "`v" + Lauren.getInstance().getVersion() + "`", true)
                .addField("🙍‍♂️ Dono", "`" + authorBot + "`", true)

                .addField("<a:infinito:703187274912759899> Uptime",
                        "`" + MathUtils.format(System.currentTimeMillis() - Lauren.getInstance().getStartTime()) + "`",
                        true)

                .addField("💥 Servidores", "`Sou exclusiva deste servidor :d`", true)
                .addField("🏓 Ping da API", "`" + event.getJDA().getGatewayPing() + "ms`", true)

                .addField("\uD83D\uDD8A Prefixo", "Padrão: `$`", true)
                .addField("<:java:723609384428503071> Versão Java", "`v" + System.getProperty("java.version") + "`", true)
                .addField("<:discord:723587554422816889> Versão JDA", "`v4.2.0_186`", true)

                .addField("⚙️ Núcleos", "`" + Runtime.getRuntime().availableProcessors() + " cores`", true)
                .addField("\uD83D\uDEE2 Banco de Dados", "`" + Lauren.getInstance().getConfig().databaseType + "`", true)
                .addField("\uD83C\uDF9E RAM", "`"
                        + PterodactylConnection.get().getServer().getServerUsage().getMemoryUsage() +
                        "M/"
                        + PterodactylConnection.get().getServer().getLimits().getMemory() + "M`", true)

                .setFooter("Mais informações em $ping", event.getAuthor().getAvatarUrl())
                .setColor(event.getMember().getColor())
                .setThumbnail(bot.getAvatarUrl())
                .setTimestamp(Instant.now());

        event.getChannel().sendMessage(builder.build()).queue();
    }
}
