package ClientServer.ServerResponse;

import languages.UserLanguage;

public class DeleteLanguageResponse extends ServerResponse{
    private UserLanguage userLanguage;

    public DeleteLanguageResponse(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }
}
