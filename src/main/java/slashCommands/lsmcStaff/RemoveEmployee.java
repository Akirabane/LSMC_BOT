package slashCommands.lsmcStaff;

import Entities.User;
import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandsDescription("Retire un employé de la base de données.")
public class RemoveEmployee extends ListenerAdapter {

    private final UserService userService;

    public RemoveEmployee() {
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
        if (!permissions.contains("doRemoveEmployeeCommand")) {
            event.reply("Vous n'avez pas la permission d'effectuer cette commande.").setEphemeral(true).queue();
            return;
        }

        if (event.getName().equalsIgnoreCase("RemoveEmployee")) {
            Member targetUser = event.getOption("user").getAsMember();

            if (userService.getUserByUserId(targetUser.getIdLong()) == null) {
                event.reply(targetUser.getAsMention() + " n'est pas employé au sein du LSMC.").setEphemeral(true).queue();
            } else {
                userService.deleteUser(targetUser.getIdLong());
                event.reply("Employé retiré avec succès.").setEphemeral(true).queue();
            }
        }
    }
}

