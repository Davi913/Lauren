package com.yuhtin.lauren.commands.impl.music;

import com.yuhtin.lauren.commands.Command;
import com.yuhtin.lauren.commands.CommandInfo;
import com.yuhtin.lauren.util.MusicUtil;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

@CommandInfo(
        name = "skip.force",
        type = CommandInfo.CommandType.MUSIC,
        description = "Forçar o pulo de uma música"
)
public class ForceSkipCommand implements Command {

    @Override
    public void execute(CommandInteraction event, InteractionHook hook) {
        if (event.getGuild() == null
                || event.getMember() == null
                || MusicUtil.isIdle(event.getGuild(), hook)
                || !UserUtil.isDJ(event.getMember(), hook)) return;

        TrackManager.getByGuild(event.getGuild()).skipTrack();
        hook.sendMessage("\u23e9 Pulei a música pra você <3").queue();
    }
}
