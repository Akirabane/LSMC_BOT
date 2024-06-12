package slashCommands;

import Entities.User;
import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandsDescription("Modifie le rang d'un employé dans la base de données.")
public class UpdateEmployeeRank extends ListenerAdapter {

    private final UserService userService;

    public UpdateEmployeeRank() {
        this.userService = new UserService();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("UpdateEmployeeRank")) {
            String targetUserName = event.getOption("user").getAsMember().getEffectiveName();
            long targetUserId = event.getOption("user").getAsMember().getIdLong();
            String newRank = event.getOption("newrank").getAsString();

            if (userService.getUserByUserId(targetUserId) == null) {
                event.reply(targetUserName + " n'est pas enregistré dans la base de données.").setEphemeral(true).queue();
            } else {
                List<String> rangs = List.of("Ambulancier", "Brancardier", "Infirmier", "Stagaire en Médecine", "Externe", "Interne", "Docteur", "Professeur", "Chef de Service", "Directeur");
                if (rangs.contains(newRank)) {
                    userService.updateRankOfUser(targetUserId, newRank);
                    event.reply("Rang de l'employé mis à jour avec succès.").setEphemeral(true).queue();
                } else {
                    event.reply("Le rang spécifié n'est pas valide.").setEphemeral(true).queue();
                }
            }
        }
    }
}
