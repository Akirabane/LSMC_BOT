package slashCommands.lsmcStaff;

import Entities.User;
import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandsDescription("Ajoute un employé à la base de données.")
public class AddEmployee extends ListenerAdapter {

    private final UserService userService;

    public AddEmployee() {
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
        if (!permissions.contains("doAddEmployeeCommand")) {
            event.reply("Vous n'avez pas la permission d'effectuer cette commande.").setEphemeral(true).queue();
            return;
        }

        if (event.getName().equalsIgnoreCase("AddEmployee")) {
            String targetUserName = event.getOption("user").getAsMember().getEffectiveName();
            long targetUserId = event.getOption("user").getAsMember().getIdLong();

            if (userService.getUserByUserId(targetUserId) != null) {
                event.reply(targetUserName + " est déjà enregistré dans la base de données.").setEphemeral(true).queue();
            } else {
                userService.addUser(targetUserId, targetUserName);
                event.reply("Employé ajouté avec succès.").setEphemeral(true).queue();
            }
        }
    }
}
