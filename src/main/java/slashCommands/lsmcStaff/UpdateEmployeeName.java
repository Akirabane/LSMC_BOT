package slashCommands.lsmcStaff;

import Entities.User;
import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandsDescription("Modifie le nom d'un employé dans la base de données.")
public class UpdateEmployeeName extends ListenerAdapter {

    private final UserService userService;

    public UpdateEmployeeName() {
        this.userService = new UserService();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        UserService userService = new UserService();
        User user = userService.getUserByUserId(event.getUser().getIdLong());
        if (user == null) {
            event.reply("Utilisateur non enregistré.").setEphemeral(true).queue();
            return;
        }

        List<String> permissions = userService.getPermissionsByRole(user.getRole());
        if (!permissions.contains("doUpdateEmployeeNameCommand")) {
            event.reply("Vous n'avez pas la permission d'effectuer cette commande.").setEphemeral(true).queue();
            return;
        }

        if (event.getName().equalsIgnoreCase("updateemployeename")) {
            String targetUserName = event.getOption("user").getAsMember().getEffectiveName();
            long targetUserId = event.getOption("user").getAsMember().getIdLong();
            String newName = event.getOption("newname").getAsString();

            if (userService.getUserByUserId(targetUserId) != null) {
                userService.updateNameOfEmployee(targetUserId, newName);
                event.reply("Nom de l'employé mis à jour avec succès.").setEphemeral(true).queue();
            } else {
                event.reply(targetUserName + " n'est pas enregistré dans la base de données.").setEphemeral(true).queue();
            }
        }
    }
}
