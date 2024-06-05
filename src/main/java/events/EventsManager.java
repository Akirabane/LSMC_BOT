package events;

import listeners.VerificationListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventsManager extends ListenerAdapter {

        public EventsManager() {}
        public void registerAllListeners(JDABuilder builder) {
            builder.addEventListeners(new GuildMemberJoinListener());
            builder.addEventListeners(new VerificationListener());
        }

}
