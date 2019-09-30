package ClientServer.ServerResponse;

import languages.UserLanguage;

public class CreateLanguageResponse extends ServerResponse {
    private UserLanguage userLanguage;

    public CreateLanguageResponse(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }
}
