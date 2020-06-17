package commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import models.annotations.CommandHandler;
import utils.helper.Utilities;

@CommandHandler(name = "volume", type = CommandHandler.CommandType.MUSIC, description = "Definir um volume para meus batidões (não coloca menos de 30 se não deixa de ser batidão ;-;)")
public class VolumeCommand extends Command {

    public VolumeCommand() {
        this.name = "volume";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!Utilities.isDJ(event.getMember())) {
            event.getChannel().sendMessage("Ahhh, que pena \uD83D\uDC94 você não pode realizar essa operação").queue();
            return;
        }

        if (event.getArgs().isEmpty()) {
            event.getChannel().sendMessage("\uD83D\uDCA2 Eita calma ai, insira um valor de `1 a 100` para ser o volume").queue();
            return;
        }

        int i;
        try {
            i = Integer.parseInt(event.getArgs());
            if (i < 1 || i > 100) i = 65;
        }catch (Exception exception) {
            event.getChannel().sendMessage("\uD83D\uDCA2 Eita calma ai, insira um valor de `1 a 100` para ser o volume").queue();
            return;
        }

        MusicCommand.trackManager.player.setVolume(i);
        event.getChannel().sendMessage("♻️ Opaaaa, você setou o volume dos meus batidões para `" + i + "%`").queue();
    }
}
