package slashCommands;

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
        if (event.getName().equalsIgnoreCase("ListEmployees")) {
            List<User> users = userService.getAllUsers();
            StringBuilder usersList = new StringBuilder();

            for (User user : users) {
                usersList.append(user.getUsername() + " ->  grade : " + user.getGrade()).append("\n");
            }
            event.reply("Liste des employés :\n" + usersList).setEphemeral(true).queue();

        }
    }
}
