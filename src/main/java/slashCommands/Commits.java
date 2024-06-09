package slashCommands;

import Utils.RandomColorGenerator;
import annotations.CommandsDescription;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@CommandsDescription("Récupère les derniers commits d'une branche donnée.")
public class Commits extends ListenerAdapter {

    private String githubRepo;
    private OkHttpClient httpClient;

    public Commits() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();

            this.githubRepo = properties.getProperty("GITHUB_LINK");
            this.httpClient = new OkHttpClient();

            if (githubRepo == null) {
                throw new IllegalArgumentException("L'ID du repo git doit être définis dans config.properties");
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du fichier de configuration.");
            e.printStackTrace();
        }
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Color randomColor = RandomColorGenerator.generateRandomColor();
        if (event.getName().equals("commits")) {
            String branch = event.getOption("branch") != null ? event.getOption("branch").getAsString().toLowerCase() : "main";
            try {

                if(!branchExists(branch)) {
                    event.reply("La branche " + branch + " n'existe pas.").setEphemeral(true).queue();
                    return;
                }

                String commitsJson = fetchCommits(branch);
                JSONArray commitsArray = new JSONArray(commitsJson);

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Derniers commits de la branche " + branch)
                        .setColor(randomColor);

                for (int i = Math.min(10, commitsArray.length()) - 1; i >= 0; i--) {
                    JSONObject commit = commitsArray.getJSONObject(i);
                    String message = commit.getJSONObject("commit").getString("message");
                    String author = commit.getJSONObject("commit").getJSONObject("author").getString("name");
                    String sha = commit.getString("sha");
                    String date = commit.getJSONObject("commit").getJSONObject("author").getString("date");

                    OffsetDateTime dateTime = OffsetDateTime.parse(date);
                    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

                    embedBuilder.addField(sha.substring(0, 7) + " - " + author + " - " + formattedDate, message, false);
                }


                event.replyEmbeds(embedBuilder.build()).queue(
                        response -> response.deleteOriginal().queueAfter(30, TimeUnit.SECONDS,
                                success -> System.out.println("Message supprimé avec succès."),
                                failure -> System.out.println("Erreur lors de la suppression du message, message déjà supprimé.")
                        )
                );
            } catch (IOException e) {
                event.reply("Erreur lors de la récupération des commits : " + e.getMessage()).setEphemeral(true).queue();
            }
        }
    }

    private String fetchCommits(String branch) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/" + githubRepo + "/commits?sha=" + branch)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }

    private boolean branchExists(String branch) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/" + githubRepo + "/branches")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseData = response.body().string();
            JSONArray branchesArray = new JSONArray(responseData);

            for (int i = 0; i < branchesArray.length(); i++) {
                JSONObject branchObject = branchesArray.getJSONObject(i);
                if (branchObject.getString("name").equals(branch)) {
                    return true;
                }
            }
        }
        return false;
    }

}
