package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.command.CommandManager;

import java.util.*;

import static tech.goodquestion.lembot.lib.Helper.PREFIX;

public class HelpList implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("HILFE \n");
        embedBuilder.setColor(0x002d47);
        embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");

        String queriedHelpList = "default";

        if (args.length > 0) {
            queriedHelpList = args[0].toLowerCase(Locale.ROOT);
        }

        if (queriedHelpList.equals("-")) {
            EmbedBuilder e = new EmbedBuilder().setTitle("Verfügbare Help-Listen").setDescription(String.join("\n", CommandManager.getInstance().getHelpLists()));
            message.getChannel().sendMessage(e.build()).queue();
            return;
        }

        if (!CommandManager.getInstance().getHelpLists().contains(queriedHelpList)) {
            channel.sendMessage("Diese Commands existieren nicht! " + sender.getAsMention()).queue();
            return;
        }

        StringBuilder descriptionBuilder = new StringBuilder("Hallo, ich heiße **LemBot** und bin ein Bot für GoodQuestion :)");
        descriptionBuilder.append("\n\n----------------- **BEFEHLSLISTE** -----------------\n");
        descriptionBuilder.append("\nPrefix: **" + PREFIX + "**\n\n");

        List<IBotCommand> commandsOnHelpList = new ArrayList<>();

        for (IBotCommand command : CommandManager.getInstance().getCommands().values()) {
            if (!Objects.equals(command.getHelpList(), queriedHelpList)) continue;
            commandsOnHelpList.add(command);
        }

        commandsOnHelpList.sort(Comparator.comparing(IBotCommand::getName));
        commandsOnHelpList.forEach(c -> descriptionBuilder.append(c.getDescription()).append("\n"));

        embedBuilder.setDescription(descriptionBuilder.toString());
        message.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "`?help -`: Zeigt alle verfügbaren Command-Listen an";
    }

    @Override
    public String getHelpList() {
        return "default";
    }

}
