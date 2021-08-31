package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class HelpList extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String helpCommand = "help";

        if (Helper.isValidCommand(userMessageContent, helpCommand) || Helper.isValidCommand(userMessageContent, "hilfe")) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("HILFE \n");
                embedBuilder.setColor(0x002d47);
                embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                embedBuilder.setDescription("""
                        Hallo, ich heiße **LemBot** und bin ein Bot für GoodQuestion:)\s

                                        -------------------- **BEFEHLSLISTE** --------------------\s
                         \s
                         Prefix: **?**\s
                        \s
                        `?check <password>`: gibt zurück, ob Passwort unsicher ist\s
                        `?hmm`: Anzahl deiner geschickten Nachrichten auf GQ\s
                        `?hmb`: Anzahl deiner Bumps auf GQ\s
                        `?topu`: Liste der Top 3 User mit den meisten Nachrichten
                        `?topb`: Liste der Top 3 Server-Bumper\040
                        `?tope`: Liste der 3 am häufigsten benutzten Emojis\040\040
                        `?topp`: Liste der 3 am häufigsten gepingten User
                        `?topc`: Liste der 3 aktivisten Channels
                        `?aur`: Record der maximalen, aktiven User
                        `?srole`: Liste aller Rollen auf GQ\040
                        `?shelp`: Zeigt die Staff-Befehlsliste
                        `?source`: Informationen zum Code von Lembot
                        `?hcb`: Zeigt, wie Quellcode eingefärbt wird\s 
                        `?+bumper`: weist dem User <@&815922232106156033> zu 
                        `?-bumper`: entfernt dem User <@&815922232106156033>\s 
                        """);

                event.getChannel().sendMessage(embedBuilder.build()).queue();

            }

        }

    }

}
