package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberLeftStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        CommandHelper.logUserLeave(event.getUser());
        CommandHelper.logMemberAmount(event.getGuild());


    }
}