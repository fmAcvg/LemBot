package discord.bot.gq.db.config;

import discord.bot.gq.db.ConnectionToDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigSelection {
    private String roleId = null;
    private String channelId = null;

    public void selectRoleId() {
        try {
            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String selectRoleId = "SELECT config_value FROM config WHERE config_name = 'id_ping_role'";
            Statement statement = db.connection.createStatement();
            ResultSet rS = statement.executeQuery(selectRoleId);

            if (rS.next()) {
                this.roleId = rS.getString(1);

            }
        } catch (SQLException e) {
            e.getMessage();

        }

    }

    public void selectChannelId() {
        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String selectChannelId = "SELECT config_value FROM config WHERE config_name = 'id_ping_channel'";
            Statement statement = db.connection.createStatement();
            ResultSet rS = statement.executeQuery(selectChannelId);

            if (rS.next()) {
                this.channelId = rS.getString(1);

            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }

    public String getRoleId() {
        return roleId;
    }

    public String getChannelId() {
        return channelId;
    }

}