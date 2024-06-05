package slashCommands;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class NameRP extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("nameRP")) {
            User targetUser = event.getOption("user").getAsUser();

            targetUser.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Bonjour ! Veuillez changer votre pseudo Discord en utilisant votre nom et prénom RP.").queue();
                event.reply("Demande envoyée à " + targetUser.getAsTag() + " avec succès.").setEphemeral(true).queue();
            }, throwable -> {
                event.reply("Impossible d'envoyer un message privé à cet utilisateur.").setEphemeral(true).queue();
            });
        }
    }
}
