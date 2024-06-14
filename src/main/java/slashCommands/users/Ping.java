package slashCommands.users;

import Entities.User;
import Services.UserService;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandsDescription("Renvoie le temps de réponse du bot.")
public class Ping extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        UserService userService = new UserService();
        User user = userService.getUserByUserId(event.getUser().getIdLong());
        if (user == null) {
            event.reply("Utilisateur non enregistré.").setEphemeral(true).queue();
            return;
        }

        List<String> permissions = userService.getPermissionsByRole(user.getRole());
        if (!permissions.contains("doPingCommand")) {
            event.reply("Vous n'avez pas la permission d'effectuer cette commande.").setEphemeral(true).queue();
            return;
        }

        if (event.getName().equalsIgnoreCase("ping")) {
            long startTime = System.currentTimeMillis();
            event.reply("Pong! Temps de réponse du bot : ...").queue(response -> {
                long endTime = System.currentTimeMillis();
                long ping = endTime - startTime;
                response.editOriginal("Pong! Temps de réponse du bot : " + ping + " ms").queue();
                response.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
            });
        }
    }
}
