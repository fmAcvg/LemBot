package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class BoostStorage extends ListenerAdapter {

    @Override
    public void onGuildUpdateBoostCount(@Nonnull final GuildUpdateBoostCountEvent event) {

        final int newValue = event.getNewValue();
        final int oldValue = event.getOldValue();


    }
    @Override
    public void onGuildMemberUpdateBoostTime(@Nonnull final GuildMemberUpdateBoostTimeEvent event) {

        final long boosterId = event.getMember().getIdLong();
        final String boosterAsTag = event.getMember().getEffectiveName();
        final String boosterNickname = event.getMember().getNickname();


    }
}
