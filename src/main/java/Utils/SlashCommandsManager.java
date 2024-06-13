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
                createRemoveEmployeeCommand(),
                createListEmployeesCommand(),
                createUpdateNameOfEmployeeCommand(),
                createUpdateRankOfEmployeeCommand()
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

    private static CommandData createListEmployeesCommand() {
        return Commands.slash("listemployees", "Affiche la liste des employés enregistrés dans la base de données.");
    }

    private static CommandData createUpdateNameOfEmployeeCommand() {
        return Commands.slash("updateemployeename", "Met à jour le nom d'un employé")
                .addOption(OptionType.USER, "user", "L'utilisateur à mettre à jour", true)
                .addOption(OptionType.STRING, "newname", "Le nouveau nom de l'utilisateur", true);
    }

    private static CommandData createUpdateRankOfEmployeeCommand() {
        return Commands.slash("updateemployeerank", "Met à jour le rang d'un employé")
                .addOption(OptionType.USER, "user", "L'utilisateur à mettre à jour", true)
                .addOption(OptionType.STRING, "newrank", "Le nouveau rang de l'utilisateur", true);
    }
}

