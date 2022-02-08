package com.yuhtin.lauren.commands.impl.admin;

import com.google.inject.Inject;
import com.yuhtin.lauren.commands.Command;
import com.yuhtin.lauren.commands.CommandData;
import com.yuhtin.lauren.core.player.controller.PlayerController;
import com.yuhtin.lauren.utils.helper.UserUtil;
import lombok.val;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

import java.util.concurrent.TimeUnit;

@CommandData(
        name = "permission",
        type = CommandData.CommandType.ADMIN,
        description = "Alterar uma permissão em um jogador.",
        args = {
                "<option>-Use add para adicionar ou remove para remover.",
                "<@player>-Jogador que deseja fazer a alteração.",
                "<permission>-Permissão que deseja adicionar ou remover."
        }
)
public class AddPermissionCommand implements Command {

    @Inject private PlayerController playerController;

    @Override
    public void execute(CommandInteraction event, InteractionHook hook) throws Exception {
        if (!UserUtil.hasPermission(event.getMember(), hook, Permission.ADMINISTRATOR))
            return;

        val option = event.getOption("option").getAsString();
        val player = event.getOption("player").getAsUser();
        val permission = event.getOption("permission").getAsString();

        val data = playerController.get(player.getIdLong());
        if (option.equalsIgnoreCase("add")) {
            data.addPermission(permission);

            hook.setEphemeral(true).sendMessage(
                    "<:felizpakas:742373250037710918> " +
                    "Você adicionou a permissão **" + permission + "** para o jogador " + player.getAsTag()
            ).queue();
            return;
        } else if (option.equalsIgnoreCase("remove")) {
            data.removePermission(permission);

            hook.setEphemeral(true).sendMessage(
                    "<:felizpakas:742373250037710918> " +
                            "Você removeu a permissão **" + permission + "** do jogador " + player.getAsTag()
            ).queue();
            return;
        }

        hook.setEphemeral(true).sendMessage(":x: Você precisa usar **add** ou **remove** como parâmetro option.").queue();
    }
}
