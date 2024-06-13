package slashCommands.discordStaff;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@CommandsDescription("Envoie un message privé à un utilisateur pour lui demander de changer son pseudo Discord en utilisant son nom et prénom RP.")
public class NameRP extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("nameRP")) {
            User targetUser = event.getOption("user").getAsUser();

            targetUser.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Bonjour ! Veuillez changer votre pseudo Discord en utilisant votre nom et prénom RP.").queue(
                        success -> {
                            event.reply("Demande envoyée à " + targetUser.getAsTag() + " avec succès.").setEphemeral(true).queue();
                        },
                        error -> {
                            if (error instanceof ErrorResponseException) {
                                ErrorResponseException errorResponse = (ErrorResponseException) error;
                                if (errorResponse.getErrorCode() == 50007) {
                                    event.reply(targetUser.getAsMention() + " a bloqué ses MP ou le bot. Impossible d'envoyer un message privé.").queue();
                                } else {
                                    event.reply("Une erreur est survenue lors de l'envoi du message. Code d'erreur: " + errorResponse.getErrorCode()).setEphemeral(true).queue();
                                }
                            } else {
                                event.reply("Une erreur inattendue est survenue lors de l'envoi du message.").setEphemeral(true).queue();
                            }
                        }
                );
            });
        }
    }
}
