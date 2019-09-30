package languages;

import database.RecordObject;

public class UserLanguage extends RecordObject {

    private long user_id;
    private String language;
    private int level;

    public UserLanguage(long user_id, String language, int level) {
        super(user_id, "user_languages", "user_id");
        this.user_id = user_id;
        this.language = language;
        this.level = level;
    }





    public long getUser_id() {
        return user_id;
    }

    public String getLanguage() {
        return language;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean  sameLanguage(UserLanguage userLanguage) {
        return this.language.equals(userLanguage.language);
    }

    @Override
    public String toString() {
        return "UserLanguage{" +
                "user_id=" + user_id +
                ", language='" + language + '\'' +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserLanguage) {
            return ((UserLanguage) obj).language.equals(this.language);
        }
        return false;
    }
}
