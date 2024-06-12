package slashCommands;

import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@CommandsDescription("Modifie le nom d'un employé dans la base de données.")
public class UpdateEmployeeName extends ListenerAdapter {

    private final UserService userService;

    public UpdateEmployeeName() {
        this.userService = new UserService();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("UpdateEmployeeName")) {
            String targetUserName = event.getOption("user").getAsMember().getEffectiveName();
            long targetUserId = event.getOption("user").getAsMember().getIdLong();
            String newName = event.getOption("newname").getAsString();

            if (userService.getUserByUserId(targetUserId) == null) {
                event.reply(targetUserName + " n'est pas enregistré dans la base de données.").setEphemeral(true).queue();
            } else {
                userService.updateNameOfUser(targetUserId, newName);
                event.reply("Rang de l'employé mis à jour avec succès.").setEphemeral(true).queue();
            }
        }
    }
}
