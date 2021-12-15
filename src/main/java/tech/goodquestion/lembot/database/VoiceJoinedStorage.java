package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.VoiceChannel;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

public class VoiceJoinedStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        String insertQuery = "INSERT INTO voice_join (id_discord, user_tag, username, voice_channel_name) VALUES (?,?,?,?);";

        VoiceChannel voiceChannel = new VoiceChannel();

        voiceChannel.userId = event.getMember().getIdLong();
        voiceChannel.userTag = event.getMember().getUser().getAsTag();
        voiceChannel.userName = event.getMember().getUser().getName();
        voiceChannel.name = event.getChannelJoined().getName();

        String userMentioned = event.getMember().getAsMention();

        EmbedBuilder joinEmbed = new EmbedBuilder();

        String embedDescription = userMentioned + " ist **" + voiceChannel.name + "** um " + Helper.getCurrentTime() + " Uhr **gejoint**.";

        Helper.createEmbed(joinEmbed, "Voice **Joined** ", embedDescription, EmbedColorHelper.VOICE_JOINED);
        Config.getInstance().getChannel().getVoiceChatChannel().sendMessage(joinEmbed.build()).queue();

        Helper.insertVoiceChannelData(insertQuery, voiceChannel);
    }
}
