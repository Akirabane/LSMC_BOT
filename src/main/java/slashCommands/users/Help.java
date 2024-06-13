package slashCommands.users;

import Utils.RandomColorGenerator;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

@CommandsDescription("Envoie un embed pour obtenir plus d'informations.")
public class Help extends ListenerAdapter {
    private static final Color randomColor = RandomColorGenerator.generateRandomColor();
    private static String channelChatRobotId;

    public Help() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            channelChatRobotId = properties.getProperty("CHANNEL_CHAT_ROBOT_ID");

            if (channelChatRobotId == null) {
                throw new IllegalArgumentException("L'ID du canal doit être défini dans config.properties");
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("help")) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Besoin d'aide ?")
                    .setDescription("La commande /help permet d'afficher des boutons pour obtenir plus d'informations.\n\n" +
                            "Cliquez sur les boutons ci-dessous pour en savoir plus.")
                    .setColor(randomColor);

            Button aboutButton = Button.primary("help_about_bot", "Qu'est-ce que LSMC_Bot ?");
            Button commandsButton = Button.secondary("help_commands_info", "Commandes");

            event.replyEmbeds(embedBuilder.build())
                    .addActionRow(aboutButton, commandsButton)
                    .queue();
        }
    }

    public static void handleButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getChannel().getId().equals(channelChatRobotId)) {
            return;
        }

        String componentId = event.getComponentId();
        EmbedBuilder responseEmbed = new EmbedBuilder();

        if (componentId.equals("help_about_bot")) {
            responseEmbed.setTitle("Qu'est-ce que LSMC_Bot ?")
                    .setDescription("LSMC_Bot est un bot Discord conçu pour faciliter la gestion et l'interaction dans le serveur LSMC.")
                    .setColor(randomColor);

            event.replyEmbeds(responseEmbed.build()).setEphemeral(true).queue();
        } else if (componentId.equals("help_commands_info")) {
            responseEmbed.setTitle("Commandes disponibles")
                    .setColor(randomColor);

            Reflections reflections = new Reflections("slashCommands");
            Set<Class<? extends ListenerAdapter>> classes = reflections.getSubTypesOf(ListenerAdapter.class);

            for (Class<? extends ListenerAdapter> clazz : classes) {
                String commandName = clazz.getSimpleName();
                String commandDescription = "Aucune description fournie.";

                if (clazz.isAnnotationPresent(CommandsDescription.class)) {
                    CommandsDescription annotation = clazz.getAnnotation(CommandsDescription.class);
                    commandDescription = annotation.value();
                }

                responseEmbed.appendDescription("/" + commandName.toLowerCase() + " - " + commandDescription + "\n");
            }

            event.replyEmbeds(responseEmbed.build()).setEphemeral(true).queue();
        }
    }
}
