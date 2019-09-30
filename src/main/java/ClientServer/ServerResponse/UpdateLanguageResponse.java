package ClientServer.ServerResponse;

import languages.UserLanguage;

public class UpdateLanguageResponse extends ServerResponse {
    private UserLanguage userLanguage;

    public UpdateLanguageResponse(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }
}
