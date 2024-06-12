package Utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashCommandsManager {

    public static void registerCommands(JDA jda) {
        jda.updateCommands().addCommands(
                createHelpCommand(),
                createNameRPCommand(),
                createPingCommand(),
                createCommitsCommand(),
                createAddEmployeeCommand(),
                createRemoveEmployeeCommand()
        ).queue();
    }

    private static CommandData createHelpCommand() {
        return Commands.slash("help", "Affiche un embed pour obtenir plus d'informations.");
    }

    private static CommandData createNameRPCommand() {
        return Commands.slash("namerp", "Envoie un DM à un utilisateur pour changer son pseudo.")
                .addOption(OptionType.USER, "user", "L'utilisateur à qui envoyer le DM", true);
    }

    private static CommandData createPingCommand() {
        return Commands.slash("ping", "Répond Pong!");
    }

    private static CommandData createCommitsCommand() {
        return Commands.slash("commits", "Affiche les 10 derniers commits du dépôt LSMC-BOT sur GitHub.")
                .addOption(OptionType.STRING, "branch", "La branche du dépôt à consulter", false);
    }

    private static CommandData createAddEmployeeCommand() {
        return Commands.slash("addemployee", "Ajoute un employé à la base de données.")
                .addOption(OptionType.USER, "user", "L'utilisateur à ajouter", true);
    }

    private static CommandData createRemoveEmployeeCommand() {
        return Commands.slash("removeemployee", "Retire un employé de la base de données.")
                .addOption(OptionType.USER, "user", "L'utilisateur à retirer", true);
    }
}

