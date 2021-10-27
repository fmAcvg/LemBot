package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.entities.Sanction;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.*;

public class QueryHelper {

    private static final String INSERT_USER_STATUS = "INSERT INTO user_status (id_discord, user_tag, status) VALUES (?,?,?);";
    private static final String INSERT_MEMBER_AMOUNT = "INSERT INTO number_member (total_member) VALUES (?);";
    public static final String TOP_EMOJIS = """
                        SELECT '\uD83D\uDC4D', SUM(id_discord) AS c  FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDC4D%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T
                        UNION ALL\s
                        SELECT '\uD83D\uDE05', SUM(id_discord) AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE05%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL  \s
                        SELECT '\uD83D\uDE0A', SUM(id_discord )AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE0A%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDC40', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDC40%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE02', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE02%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE09', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE09%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE1B', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE1B%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T \s
                        UNION ALL
                        SELECT content, SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE42%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE42', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE43%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83E\uDD13', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83E\uDD13%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T \s
                        UNION ALL
                        SELECT '\uD83D\uDE04', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE04%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        ORDER BY c DESC LIMIT 3;""";
    public static final String TOP_CHANNELS = "SELECT channel_name, COUNT(id_channel) FROM `channel` GROUP BY id_channel ORDER BY COUNT(id_channel) DESC LIMIT 3;";
    public static final String TOP_PINGED_USER = "SELECT content, COUNT(id_discord) FROM `user_message_content` WHERE content LIKE '%<@!%' OR '%<@I%' GROUP BY content HAVING COUNT(id_discord) > 1 ORDER BY COUNT(id_discord) DESC LIMIT 3";
    public static final String USER_LEAVE_LOG = "INSERT INTO user_leave (id_user_leave,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String USER_JOIN_LOG = "INSERT INTO user_join (id_user_join,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String USER_BAN_DATA = "INSERT INTO banned_user (id_banned_user,id_discord,user_tag, username, ban_author, ban_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_KICK_DATA = "INSERT INTO kicked_user (id_kicked_user,id_discord,user_tag, username, kick_author, kick_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_MUTE_DATA = "INSERT INTO muted_user (id_muted_user,id_discord,user_tag, username, mute_author, mute_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_WARN_DATA = "INSERT INTO warned_user (id_warned_user,id_discord,user_tag, username, warn_author, warn_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String LAST_BUMP = "SELECT TIME(TIMESTAMPADD(HOUR,1, bumped_at)) FROM user_bump_time ORDER BY bumped_at DESC LIMIT 1";
    public static final String NEXT_BUMP = "SELECT TIMESTAMPDIFF(MINUTE,CURRENT_TIMESTAMP, TIMESTAMPADD(HOUR,2, bumped_at)) FROM user_bump_time ORDER BY bumped_at DESC LIMIT 1";
    public static final String ACTIVE_MEMBER_LOG = "INSERT INTO active_user (active_member) VALUES (?);";
    public static final String ACTIVE_USER_RECORD = "SELECT MAX(active_member) FROM active_user;";

    public static void logMemberStatusChange(long userId, String userTag, OnlineStatus newStatus) {
        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement statement = conn.prepareStatement(INSERT_USER_STATUS)) {
            statement.setLong(1, userId);
            statement.setBlob(2, Helper.changeCharacterEncoding(statement, userTag));
            statement.setString(3, String.valueOf(newStatus));
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logActiveMemberCount(int memberCount) {
        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = conn.prepareStatement(ACTIVE_MEMBER_LOG)) {
            preparedStatement.setLong(1, memberCount);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logMemberAmount(Guild guild) {
        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement statement = conn.prepareStatement(INSERT_MEMBER_AMOUNT)) {
            statement.setInt(1, guild.getMemberCount());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    private static EmbedBuilder getTop(String query, String[] fieldNames, String[] fieldIcons) throws SQLException {
        try (Connection conn = DatabaseConnector.openConnection(); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription("");

            int top = 1;

            while (resultSet.next()) {
                if (top == 1) {
                    embed.addField(fieldNames[0], fieldIcons[0] + resultSet.getString(1), false);
                }
                if (top == 2) {
                    embed.addField(fieldNames[1], fieldIcons[1] + resultSet.getString(1), false);
                }
                if (top == 3) {
                    embed.addField(fieldNames[2], fieldIcons[2] + resultSet.getString(1), false);
                }
                top++;
            }
            return embed;
        }
    }

    public static EmbedBuilder getTopEmojis() throws SQLException {
        return getTop(TOP_EMOJIS, new String[]{"", "", ""}, new String[]{"TOP 1", "TOP 2", "TOP 3"});
    }

    public static EmbedBuilder getTopActiveChannels() throws SQLException {
        return getTop(TOP_CHANNELS, new String[]{"", "", ""}, new String[]{"TOP 1", "TOP 2", "TOP 3"});
    }

    public static EmbedBuilder getTopPingedUser() throws SQLException {
        return getTop(TOP_PINGED_USER, new String[]{":first_place:", ":second_place:", ":third_place:"}, new String[]{"", "", ""});
    }

    private static void logMemberStatus(String query, User member) {
        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setLong(1, member.getIdLong());
            preparedStatement.setString(2, member.getAsTag());
            preparedStatement.setString(3, member.getName());
            preparedStatement.setString(4, member.getEffectiveAvatarUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logUserLeave(User member) {
        logMemberStatus(USER_LEAVE_LOG, member);
    }

    public static void logUserJoin(User member) {
        logMemberStatus(USER_JOIN_LOG, member);
    }

    private static void insertSanction(String query, Sanction sanction) {
        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setLong(1, sanction.userId);
            preparedStatement.setString(2, sanction.userTag);
            preparedStatement.setString(3, sanction.userName);
            preparedStatement.setString(4, sanction.author);
            preparedStatement.setString(5, sanction.reason);
            preparedStatement.setString(6, sanction.channelName);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logUserBan(Sanction s) {
        insertSanction(USER_BAN_DATA, s);
    }

    public static void logUserKick(Sanction s) {
        insertSanction(USER_KICK_DATA, s);
    }

    public static void logUserMute(Sanction s) {
        insertSanction(USER_MUTE_DATA, s);
    }

    public static void logUserWarn(Sanction s) {
        insertSanction(USER_WARN_DATA, s);
    }

    public static Time getNextBumpTime() {
        Time latestBumpTime = null;

        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = conn.prepareStatement(LAST_BUMP)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                latestBumpTime = resultSet.getTime(1);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return latestBumpTime;
    }

    public static int getMinutesToNextBump() {
        int minutesBeforeBump = 0;

        try (Connection conn = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = conn.prepareStatement(NEXT_BUMP)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                minutesBeforeBump = resultSet.getInt(1);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return minutesBeforeBump;
    }

    public static int getActiveUserRecord() {
        try (Connection conn = DatabaseConnector.openConnection(); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(ACTIVE_USER_RECORD)){
            if (!resultSet.next()) {
                return 0;
            }

            int currentRecord = resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
