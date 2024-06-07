package Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CanalChat extends ListenerAdapter {
    private String channelChatRobotId;

    public CanalChat() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            this.channelChatRobotId = properties.getProperty("CHANNEL_CHAT_ROBOT_ID");

            if (channelChatRobotId == null) {
                throw new IllegalArgumentException("L'ID du canal doit être défini dans config.properties");
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        Color randomColor = RandomColorGenerator.generateRandomColor();
        TextChannel channel = event.getJDA().getTextChannelById(channelChatRobotId);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Message du BOT LSMC.")
                .setDescription("Je suis heureux de pouvoir répondre à vos questions et de vous aider. Utilisez la commande /help pour voir la liste des commandes disponibles.")
                .setColor(randomColor);

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
