package listeners;

import Utils.RandomColorGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class demandeDeRoleListener extends ListenerAdapter {

    private String sourceChannelId;
    private String destinationChannelId;
    private String managerRoleId;
    
    public demandeDeRoleListener() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            this.sourceChannelId = properties.getProperty("CHANNEL_DEMANDE_ROLE_ID");
            this.destinationChannelId = properties.getProperty("CHANNEL_DEMANDE_ROLE_ADMIN_ID");
            this.managerRoleId = properties.getProperty("ROLE_DIRECTEUR_ID");

            if (sourceChannelId == null || destinationChannelId == null || managerRoleId == null) {
                throw new IllegalArgumentException("Les IDs des rôles et du canal doivent être définis dans config.properties");
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        TextChannel sourceChannel = event.getJDA().getTextChannelById(sourceChannelId);

        if (sourceChannel != null) {
            sourceChannel.purgeMessages(sourceChannel.getIterableHistory().complete());
            Color randomColor = RandomColorGenerator.generateRandomColor();

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Instructions pour la demande de rôle")
                    .setDescription("Veuillez fournir les informations suivantes dans votre demande :")
                    .addField("Nom et prénom RP", "Entrez votre nom et prénom en jeu.", false)
                    .addField("Rôle demandé", "Spécifiez le rôle que vous souhaitez obtenir.", false)
                    .addField("Personne justifiant le rôle", "Indiquez la personne qui peut justifier votre demande.", false)
                    .setColor(randomColor)
                    .setFooter("Merci de suivre les instructions ci-dessus pour faciliter le traitement de votre demande.");

            sourceChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(sourceChannelId) && !event.getAuthor().isBot()) {
            Message message = event.getMessage();
            Member member = event.getMember();
            User user = event.getAuthor();
            TextChannel destinationChannel = event.getGuild().getTextChannelById(destinationChannelId);

            if (destinationChannel != null && member != null) {
                message.delete().queue();

                destinationChannel.sendMessage("<@&" + managerRoleId + "> " + member.getAsMention() + " a fait une demande:\n" + message.getContentRaw()).queue();

                user.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("Votre demande a été transmise.").queue();
                });
            }
        }
    }
}
