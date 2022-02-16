package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BumpCounter extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        final User embedAuthor = event.getAuthor();
        final Message message = event.getMessage();
        final String pingedUser = "<@(\\d+)>";

        if (Helper.isNotSuccessfulBump(disBoardEmbed, embedAuthor)) return;

        final String embedContent = message.getEmbeds().get(0).getDescription();
        final Pattern pattern = Pattern.compile(pingedUser);
        final Matcher matcher = pattern.matcher(Objects.requireNonNull(embedContent));

        if (!matcher.find()) return;

        final int firstBump = 1;
        final String idPingedUser = matcher.group(1);
        final String pingedUserName = event.getJDA().retrieveUserById(idPingedUser).complete().getName();

        final Connection connection = DatabaseConnector.openConnection();
        final String userExists = "SELECT id_discord FROM user_bump WHERE id_discord = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(userExists)) {
            preparedStatement.setString(1, idPingedUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final String currentNumberBump = "UPDATE user_bump SET number_bumps = (number_bumps +1) WHERE id_discord = ?";
                PreparedStatement prepareStatementOne = connection.prepareStatement(currentNumberBump);
                prepareStatementOne.setString(1, idPingedUser);
                prepareStatementOne.executeUpdate();

                final String bumpTime = "INSERT INTO user_bump_time (id_user_bump_time, id_discord) VALUES (NULL,?)";
                PreparedStatement insert = connection.prepareStatement(bumpTime);
                insert.setString(1, idPingedUser);
                insert.executeUpdate();
            } else {
                final String bumpData = "INSERT INTO user_bump (id_discord, username, number_bumps) VALUES (?,?,?);";

                PreparedStatement prepareStatementThree = connection.prepareStatement(bumpData);
                prepareStatementThree.setString(1, idPingedUser);
                prepareStatementThree.setString(2, pingedUserName);
                prepareStatementThree.setInt(3, firstBump);
                prepareStatementThree.executeUpdate();

                final String bumpTime = "INSERT INTO user_bump_time (id_user_bump_time, id_discord) VALUES (NULL,?)";
                PreparedStatement insert = connection.prepareStatement(bumpTime);
                insert.setString(1, idPingedUser);
                insert.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }
}


