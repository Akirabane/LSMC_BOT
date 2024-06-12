package Utils;

import events.GuildMemberJoinListener;
import listeners.TicketListener;
import listeners.VerificationListener;
import listeners.DemandeDeRoleListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import slashCommands.*;

public class EventsManager extends ListenerAdapter {

        public EventsManager() {}
        public void registerAllListeners(JDABuilder builder) {
            //Events
            builder.addEventListeners(new GuildMemberJoinListener());

            //Listeners
            builder.addEventListeners(new VerificationListener());
            builder.addEventListeners(new TicketListener());
            builder.addEventListeners(new DemandeDeRoleListener());
            builder.addEventListeners(new CanalChat());

            //Utils
            builder.addEventListeners(new ButtonInteractionManager());

            //Slash Commands
            builder.addEventListeners(new Help());
            builder.addEventListeners(new NameRP());
            builder.addEventListeners(new Ping());
            builder.addEventListeners(new Commits());
            builder.addEventListeners(new AddEmployee());
            builder.addEventListeners(new RemoveEmployee());
            builder.addEventListeners(new ListEmployees());
            builder.addEventListeners(new UpdateEmployeeName());
            builder.addEventListeners(new UpdateEmployeeRank());
        }

}
