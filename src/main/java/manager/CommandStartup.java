package manager;

import application.Lauren;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.ArrayList;
import java.util.List;

public class CommandStartup {
    public CommandStartup(JDA bot, String folder, String... classes) {
        List<Command> commands = new ArrayList<>();

        for (String className : classes) {
            try {
                commands.add((Command) Class.forName(folder + "." + className).newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException exception) {
                Logger.log("Unable to find class " + className + ".class").save();
            }
        }

        CommandClientBuilder clientBuilder = new CommandClientBuilder();
        clientBuilder.setOwnerId("702518526753243156");
        clientBuilder.setPrefix(Lauren.config.prefix);
        clientBuilder.setHelpWord("riphelpmessage");
        commands.forEach(clientBuilder::addCommand);
        clientBuilder.setActivity(Activity.watching("my project on github.com/Yuhtin/Lauren"));

        bot.addEventListener(clientBuilder.build());
        Logger.log("All commands has been registred").save();
    }
}
