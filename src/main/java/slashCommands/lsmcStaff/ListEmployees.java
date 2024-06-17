package slashCommands.lsmcStaff;

import Entities.User;
import Services.UserService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListEmployees extends ListenerAdapter {
    private final UserService userService;

    public ListEmployees() {
        this.userService = new UserService();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        UserService userService = new UserService();
        User userCheck = userService.getUserByUserId(event.getUser().getIdLong());
        if (userCheck == null) {
            event.reply("Utilisateur non enregistré.").setEphemeral(true).queue();
            return;
        }

        List<String> permissions = userService.getPermissionsByRole(userCheck.getRole());
        if (!permissions.contains("doListEmployeesCommand")) {
            event.reply("Vous n'avez pas la permission d'effectuer cette commande.").setEphemeral(true).queue();
            return;
        }

        if (event.getName().equalsIgnoreCase("ListEmployees")) {
            List<User> users = userService.getAllUsers();
            StringBuilder usersList = new StringBuilder();

            for (User user : users) {
                usersList.append(user.getUsername() + " ->  grade_rp : " + user.getGradeRp()).append("\n");
            }
            event.reply("Liste des employés :\n" + usersList).setEphemeral(true).queue();

        }
    }
}
