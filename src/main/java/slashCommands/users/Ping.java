package slashCommands.users;

import annotations.CommandsDescription;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

@CommandsDescription("Renvoie le temps de réponse du bot.")
public class Ping extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
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
