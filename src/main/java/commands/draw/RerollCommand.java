package commands.draw;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import draw.controller.DrawController;
import models.annotations.CommandHandler;
import net.dv8tion.jda.api.Permission;
import utils.helper.Utilities;

@CommandHandler(name = "reroll", type = CommandHandler.CommandType.SUPORT, description = "Sortear um ganhador novamente")
public class RerollCommand extends Command {

    public RerollCommand() {
        this.name = "reroll";
        this.aliases = new String[]{"sortearnovamente", "redraw"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!Utilities.isPermission(event.getMember(), event.getChannel(), Permission.ADMINISTRATOR)) return;

        if (DrawController.get() == null || !DrawController.get().finished) {
            event.getMessage().delete().queue();
            return;
        }

        event.getChannel().sendMessage("♻️ Eu realmente não sei o que aconteceu, mas, como mandaram, sorteando um novo vencedor").queue();
        DrawController.get().finish();
    }
}
