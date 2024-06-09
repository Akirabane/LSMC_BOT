package slashCommands;

import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@CommandsDescription("Ajoute un employé à la base de données.")
public class AddEmployee extends ListenerAdapter {

    private final UserService userService;

    public AddEmployee() {
        this.userService = new UserService();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("AddEmployee")) {
            String targetUserName = event.getOption("user").getAsMember().getEffectiveName();
            long targetUserId = event.getOption("user").getAsMember().getIdLong();

            if (userService.getUserByUserId(event.getMember().getIdLong()) != null) {
                event.reply("Vous êtes déjà enregistré dans la base de données.").setEphemeral(true).queue();
            } else {
                userService.addUser(targetUserId, targetUserName);
                event.reply("Employé ajouté avec succès.").setEphemeral(true).queue();
            }
        }
    }
}
