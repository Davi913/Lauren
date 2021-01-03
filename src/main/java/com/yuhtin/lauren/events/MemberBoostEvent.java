package com.yuhtin.lauren.events;

import com.yuhtin.lauren.core.player.Player;
import com.yuhtin.lauren.core.player.controller.PlayerController;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberBoostEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberUpdateBoostTime(@NotNull GuildMemberUpdateBoostTimeEvent event) {
        Player player = PlayerController.INSTANCE.get(event.getMember().getIdLong());
        Role role = event.getMember()
                .getRoles()
                .stream()
                .filter(memberRole -> memberRole.getIdLong() == 750365511430307931L)
                .findAny()
                .orElse(null);

        if (role == null) player.removePermission("role.booster");
        else {

            Role primeRole = LaurenStartup.getInstance()
                    .getGuild()
                    .getRoleById(722116789055782912L);

            if (primeRole != null) LaurenStartup.getInstance()
                    .getGuild()
                    .addRoleToMember(event.getMember(), primeRole)
                    .queue();

            player.addPermission("role.booster");
            player.addPermission("role.prime");
        }
    }
}
