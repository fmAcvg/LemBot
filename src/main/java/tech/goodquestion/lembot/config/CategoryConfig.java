package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Category;

public final class CategoryConfig {

    private long voiceFun;

    public Category getVoiceFunCategory() {
        return Config.getInstance().getGuild().getCategoryById(voiceFun);
    }
}
