package listeners;

import Utils.RandomColorGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TicketListener extends ListenerAdapter {

    private String roleMembreDuPersonnelID;
    private String channelTicketID;
    private String generalAskingID;
    private String recrutementAskingID;

    public TicketListener() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            this.roleMembreDuPersonnelID = properties.getProperty("ROLE_MEMBRE_DU_PERSONNEL_ID");
            this.channelTicketID = properties.getProperty("CHANNEL_TICKET_ID");
            this.generalAskingID = properties.getProperty("CATEGORY_TICKETS_ID");
            this.recrutementAskingID = properties.getProperty("CATEGORY_RECRUTEMENTS_ID");

            if (roleMembreDuPersonnelID == null || channelTicketID == null) {
                throw new IllegalArgumentException("Les IDs du rôle et du canal doivent être définis dans config.properties");
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Color randomColor = RandomColorGenerator.generateRandomColor();
        TextChannel channel = event.getJDA().getTextChannelById(channelTicketID);
        if (channel != null) {
            channel.purgeMessages(channel.getIterableHistory().complete());

            Button generalAskingButton = Button.primary("generalAsking", "Demande générale");
            Button recrutementButton = Button.danger("recrutementAsking", "Rejoindre le LSMC");
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Système de ticket du LSMC.")
                    .setDescription("Veuillez retrouver ci-dessous les différents types de demandes via ticket disponibles.")
                    .setColor(randomColor);

            channel.sendMessageEmbeds(embedBuilder.build())
                    .setActionRow(generalAskingButton, recrutementButton)
                    .queue();
        } else {
            System.out.println("Le canal spécifié est introuvable.");
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member != null && guild != null) {
            switch (event.getComponentId()) {
                case "generalAsking":
                    Category generalCategory = guild.getCategoryById(generalAskingID);
                    createTicket(event, member, guild, generalCategory, "ticket-", "Ticket");
                    break;
                case "recrutementAsking":
                    Category recrutementCategory = guild.getCategoryById(recrutementAskingID);
                    createTicket(event, member, guild, recrutementCategory, "recrutement-", "Recrutement");
                    break;
                case "closeTicket":
                    closeTicket(event, member);
                    break;
                default:
                    event.deferReply(true).setEphemeral(true).queue();
                    event.getHook().sendMessage("Commande inconnue.").queue();
                    break;
            }
        } else {
            event.deferReply(true).setEphemeral(true).queue();
            event.getHook().sendMessage("Une erreur est survenue lors de la gestion de votre demande.").queue();
        }
    }

    private void createTicket(ButtonInteractionEvent event, Member member, Guild guild, Category category, String ticketPrefix, String ticketTitle) {
        List<TextChannel> ticketChannels = category.getTextChannels();
        Optional<Integer> maxTicketNumber = ticketChannels.stream()
                .map(channel -> channel.getName().replace(ticketPrefix, ""))
                .filter(name -> name.matches("\\d+"))
                .map(Integer::parseInt)
                .max(Comparator.naturalOrder());

        int ticketNumber = maxTicketNumber.orElse(0) + 1;
        String ticketName = ticketPrefix + ticketNumber;

        guild.createTextChannel(ticketName)
                .setParent(category)
                .queue(channel -> {
                    String membrePersonnel = "<@&" + this.roleMembreDuPersonnelID + ">";
                    channel.sendMessage("Un " + membrePersonnel + " va bientôt s'occuper de vous " + member.getAsMention() + ", merci de patienter.")
                            .queue();
                    EmbedBuilder ticketEmbed = new EmbedBuilder()
                            .setTitle(ticketTitle)
                            .setDescription(member.getAsMention() + ", votre ticket a été créé.")
                            .setColor(RandomColorGenerator.generateRandomColor());

                    Button closeButton = Button.danger("closeTicket", "Clore le ticket");

                    channel.sendMessageEmbeds(ticketEmbed.build())
                            .setActionRow(closeButton)
                            .queue();

                    channel.getManager().putMemberPermissionOverride(
                                    member.getIdLong(),
                                    EnumSet.of(Permission.VIEW_CHANNEL,
                                            Permission.MESSAGE_SEND,
                                            Permission.MESSAGE_HISTORY),
                                    null)
                            .queue();

                    event.deferReply(true).setEphemeral(true).queue();
                    event.getHook().sendMessage("Votre ticket a été créé dans le canal " + channel.getAsMention() + ".")
                            .queue();
                });
    }

    private void closeTicket(ButtonInteractionEvent event, Member member) {
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals(this.roleMembreDuPersonnelID))) {
            TextChannel channel = (TextChannel) event.getChannel();
            event.deferReply(true).setEphemeral(true).queue();
            channel.delete().queue();
        } else {
            event.deferReply(true).setEphemeral(true).queue();
            event.getHook().sendMessage("Vous n'avez pas la permission de clore ce ticket.").queue();
        }
    }
}
