package events;

import Utils.CanalChat;
import listeners.TicketListener;
import listeners.VerificationListener;
import listeners.demandeDeRoleListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import slashCommands.NameRP;

public class EventsManager extends ListenerAdapter {

        public EventsManager() {}
        public void registerAllListeners(JDABuilder builder) {
            builder.addEventListeners(new GuildMemberJoinListener());
            builder.addEventListeners(new VerificationListener());
            builder.addEventListeners(new TicketListener());
            builder.addEventListeners(new demandeDeRoleListener());
            builder.addEventListeners(new NameRP());
            builder.addEventListeners(new slashCommands.Ping());
            builder.addEventListeners(new slashCommands.Commits());
            builder.addEventListeners(new CanalChat());
            builder.addEventListeners(new slashCommands.addEmployee());
        }

}
