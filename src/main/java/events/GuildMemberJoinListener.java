package events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GuildMemberJoinListener extends ListenerAdapter {

    private String getRoleIdFromConfig() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }

        return properties.getProperty("ROLE_NOUVEAU_VENU_ID");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        String roleId = getRoleIdFromConfig();

        if (roleId == null || roleId.isEmpty()) {
            System.out.println("L'ID du rôle est manquant dans le fichier de configuration.");
            return;
        }

        Role role = guild.getRoleById(roleId);
        if (role != null) {
            guild.addRoleToMember(event.getMember(), role).queue(
                    success -> System.out.println("Rôle attribué avec succès à " + event.getMember().getEffectiveName()),
                    error -> System.out.println("Erreur lors de l'attribution du rôle : " + error.getMessage())
            );
        } else {
            System.out.println("Rôle non trouvé pour l'ID : " + roleId);
        }
    }
}
