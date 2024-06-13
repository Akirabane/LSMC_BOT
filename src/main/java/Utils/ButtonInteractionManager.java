package Utils;

import listeners.TicketListener;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import slashCommands.users.Help;

public class ButtonInteractionManager extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        if (componentId.startsWith("help_")) {
            Help.handleButtonInteraction(event);
        } else if (componentId.startsWith("ticket_")) {
            TicketListener.handleButtonInteraction(event);
        } else {
            event.deferReply(true).setEphemeral(true).queue();
            event.getHook().sendMessage("Ce bouton ne fonctionne pas.").queue();
        }
    }
}
