import events.EventsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.util.EnumSet;
import java.util.Properties;

public class Main {
    public static void main (String[] args) {

        //Chargement du TOKEN DU BOT
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
            return;
        }

        final String TOKEN = properties.getProperty("TOKEN");
        if(TOKEN == null) {
            System.out.println("Erreur lors du chargement du token.");
            return;
        }

        // Initialisation du bot JDA
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class));

            // Ajout de l'écouteur d'événements
            EventsManager eventsManager = new EventsManager();
            eventsManager.registerAllListeners(jdaBuilder);

            //Construction du JDA
            JDA jda = jdaBuilder.build();
            jda.awaitReady();
            System.out.println("[LSMC BOT] ONLINE.");
        } catch (InvalidTokenException e) {
            System.out.println("Le token fourni est invalide.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion du bot.");
            e.printStackTrace();
        }
    }
}
