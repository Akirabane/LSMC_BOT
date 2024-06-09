package listeners;

import Utils.RandomColorGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class VerificationListener extends ListenerAdapter {

    private String channelId;
    private String roleNouveauVenuId;
    private String roleCitoyenId;

    public VerificationListener() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            this.channelId = properties.getProperty("CHANNEL_DOUANE_ID");
            this.roleNouveauVenuId = properties.getProperty("ROLE_NOUVEAU_VENU_ID");
            this.roleCitoyenId = properties.getProperty("ROLE_CITOYEN_ID");

            if (channelId == null || roleNouveauVenuId == null || roleCitoyenId == null) {
                throw new IllegalArgumentException("Les IDs des rôles et du canal doivent être définis dans config.properties");
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        TextChannel channel = event.getJDA().getTextChannelById(channelId);
        if (channel.getId().equals(channelId)) {
            channel.purgeMessages(channel.getIterableHistory().complete());
            sendVerificationEmbed(channel);
        }
    }

    private void sendVerificationEmbed(TextChannel channel) {
        Color randomColor = RandomColorGenerator.generateRandomColor();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Verification")
                .setDescription("Veuillez cliquer sur la réaction afin d'avoir votre rôle vérifié")
                .setColor(randomColor);

        channel.sendMessageEmbeds(embed.build()).queue(message -> {
            message.addReaction(Emoji.fromUnicode("✅")).queue();
        });
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getChannel().getId().equals(channelId) && !event.getUser().isBot()) {
            Member member = event.getMember();

            if(member.getRoles().contains(event.getGuild().getRoleById(roleCitoyenId))) {
                return;
            }

            if (member != null) {
                Role roleNouveauVenu = event.getGuild().getRoleById(roleNouveauVenuId);
                Role roleCitoyen = event.getGuild().getRoleById(roleCitoyenId);

                if (roleNouveauVenu != null && roleCitoyen != null) {
                    event.getGuild().removeRoleFromMember(member, roleNouveauVenu).queue();
                    event.getGuild().addRoleToMember(member, roleCitoyen).queue(
                            success -> System.out.println("Rôle citoyen attribué avec succès à " + member.getEffectiveName()),
                            error -> System.out.println("Erreur lors de l'attribution du rôle citoyen : " + error.getMessage())
                    );
                } else {
                    System.out.println("Rôle non trouvé : " + roleNouveauVenuId + " ou " + roleCitoyenId);
                }

                event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("Votre rôle a été vérifié.").queue();
                });
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.getChannel().getId().equals(channelId) && !event.getUser().isBot()) {
            Member member = event.getMember();
            if (member != null) {
                if(!member.getRoles().contains(event.getGuild().getRoleById(roleCitoyenId))) {
                    return;
                }
                Role roleNouveauVenu = event.getGuild().getRoleById(roleNouveauVenuId);
                Role roleCitoyen = event.getGuild().getRoleById(roleCitoyenId);

                if (roleNouveauVenu != null && roleCitoyen != null) {
                    event.getGuild().removeRoleFromMember(member, roleCitoyen).queue();
                    event.getGuild().addRoleToMember(member, roleNouveauVenu).queue(
                            success -> System.out.println("Rôle citoyen supprimé avec succès à " + member.getEffectiveName()),
                            error -> System.out.println("Erreur lors de l'attribution du rôle nouveau venu : " + error.getMessage())
                    );
                } else {
                    System.out.println("Rôle non trouvé : " + roleNouveauVenuId + " ou " + roleCitoyenId);
                }

                event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("Votre rôle vérifié a été supprimé.").queue();
                });
            }
        }
    }
}
