package slashCommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashCommandsManager {

    public static void registerCommands(JDA jda) {
        jda.updateCommands().addCommands(
                createNameRPCommand(),
                createPingCommand()
        ).queue(); // Appel de la méthode queue() pour exécuter l'action
    }

    private static CommandData createNameRPCommand() {
        return Commands.slash("namerp", "Envoie un DM à un utilisateur pour changer son pseudo.")
                .addOption(OptionType.USER, "user", "L'utilisateur à qui envoyer le DM", true);
    }

    private static CommandData createPingCommand() {
        return Commands.slash("ping", "Répond Pong!");
    }
}

