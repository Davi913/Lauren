package commands;

import application.Lauren;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class AjudaCommand extends Command {
    public AjudaCommand() {
        this.name = "ajuda";
        this.aliases = new String[]{"help"};
        this.help = "Comando de ajuda do bot.";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setImage("https://i.imgur.com/mQVFSrP.gif")
                .setAuthor("Comandos atacaaaaar \uD83E\uDDF8", "https://google.com", event.getJDA().getSelfUser().getAvatarUrl())
                .setDescription(
                        "Para mais informaçõe sobre um comando, digite `" + Lauren.config.prefix + "help <comando>` que eu lhe informarei mais sobre ele <a:feliz:712669414566395944>")
                .addField("**Ajuda** ❓ - _Este módulo tem comandos para te ajudar na utilização de bot._", "`ajuda` `info`", false)
                .addField("**Configurações** ⚙ - _Em configurações você define preferências de como agirei em seu servidor._", "`config`", false)
                .addField("**Mensagens Customizadas \uD83D\uDD79** - _Este módulo possui comandos para você enviar minhas mensagens customizadas._", "`createregister`", false)
                .addField("**Utilidade \uD83D\uDEE0** - _Este módulo possui coisas úteis pro eu dia a dia._", "`ping` `clear` `serverinfo` `avatar` `playerinfo`", false)
                .setFooter("Comando usado por " + event.getMember().getNickname(), event.getMember().getUser().getAvatarUrl())
                .setColor(event.getMember().getColor())
                .setTimestamp(Instant.now());
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
