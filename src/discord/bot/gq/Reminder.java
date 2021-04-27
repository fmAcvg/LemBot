package discord.bot.gq;

import discord.bot.gq.db.config.ConfigSelection;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Reminder extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + ConfigSelection.roleId + " :smile: ",
                "Es ist wieder Zeit zu bumpen " + ConfigSelection.roleId + " :smile:",
                "Bumpe den Server jetzt! " + ConfigSelection.roleId + " :smile:"};


        if (ConfigSelection.roleId == null || ConfigSelection.channelId == null) {
            return;
        }

        Random random = new Random();

        List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        User embedAuthor = event.getAuthor();
        

        if (isSuccessBump(disBoardEmbed, embedAuthor)) {

            final Runnable ping = () -> {
                int randomNumber = random.nextInt(pingContent.length);
                Objects.requireNonNull(event.getJDA().getTextChannelById(ConfigSelection.channelId)).sendMessage(pingContent[randomNumber]).queue();
            };

            scheduler.schedule(ping, 2, TimeUnit.HOURS);

        }
    }

    public static boolean isSuccessBump(List<MessageEmbed> messages, User embedAuthor) {

        long disBoardID = 302050872383242240L;
        String successBumpImageUrl = "https://disboard.org/images/bot-command-image-bump.png";

        if (embedAuthor.getIdLong() != disBoardID) {
            return false;
        }

        if (messages.get(0).getDescription() == null) {
            return false;
        }

        if (messages.get(0).getImage() == null) {
            return false;
        }
        return Objects.equals(Objects.requireNonNull(messages.get(0).getImage()).getUrl(),successBumpImageUrl);
    }

}
