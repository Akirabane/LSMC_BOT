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

public class TicketListener  extends ListenerAdapter {

    private String roleMembreDuPersonnelID;
    private String channelTicketID;

    public TicketListener() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            this.roleMembreDuPersonnelID = properties.getProperty("ROLE_MEMBRE_DU_PERSONNEL_ID");
            this.channelTicketID = properties.getProperty("CHANNEL_TICKET_ID");

            if (roleMembreDuPersonnelID == null) {
                throw new IllegalArgumentException("L'IDs du rôle et du cannal doivent être définis dans config.properties");
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
        channel.purgeMessages(channel.getIterableHistory().complete());
            Button button = Button.primary("createTicket", "Créer un ticket");
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Créer un ticket")
                    .setDescription("Cliquez sur le bouton ci-dessous pour créer un ticket.")
                    .setColor(randomColor);

            channel.sendMessageEmbeds(embedBuilder.build())
                    .setActionRow(button)
                    .queue();
        }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("createTicket")) {
            Member member = event.getMember();
            if (member != null) {
                Guild guild = event.getGuild();
                Category category = event.getChannel().asTextChannel().getParentCategory();

                event.deferReply(true).queue();

                if(guild != null && category != null) {

                    List<TextChannel> ticketChannels = category.getTextChannels();
                    Optional<Integer> maxTicketNumber = ticketChannels.stream()
                            .map(channel -> channel.getName().replace("ticket-", ""))
                            .filter(name -> name.matches("\\d+"))
                            .map(Integer::parseInt)
                            .max(Comparator.naturalOrder());

                    int ticketNumber = maxTicketNumber.orElse(0) + 1;
                    String ticketName = "ticket-" + ticketNumber;

                    guild.createTextChannel(ticketName)
                            .setParent(category)
                            .queue(channel -> {
                                String membrePersonnel = "<@&" + this.roleMembreDuPersonnelID + ">";
                                channel.sendMessage("Un " + membrePersonnel + " va bientôt s'occuper de vous " + member.getAsMention() + ", merci de patienter.")
                                        .queue();
                                EmbedBuilder ticketEmbed = new EmbedBuilder()
                                        .setTitle("Ticket")
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

                                event.getHook().sendMessage("Votre ticket a été créé dans le canal " + channel.getAsMention() + ".")
                                        .setEphemeral(true)
                                        .queue();
                            });
                } else {
                    event.getHook().sendMessage("Une erreur est survenue lors de la création du ticket.").setEphemeral(true).queue();
                }
            }
        } else if (event.getComponentId().equals("closeTicket")) {
            event.deferReply(true).queue();
            Member member = event.getMember();
            if(member != null && member.getRoles().stream().anyMatch(role -> role.getId().equals(this.roleMembreDuPersonnelID))) {
                TextChannel channel = (TextChannel) event.getChannel();
                channel.delete().queue();
            } else {
                event.getHook().sendMessage("Vous n'avez pas la permission de clore ce ticket.").setEphemeral(true).queue();
            }
        }
    }
}
