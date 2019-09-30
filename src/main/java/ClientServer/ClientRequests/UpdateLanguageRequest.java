package ClientServer.ClientRequests;

import languages.UserLanguage;

public class UpdateLanguageRequest extends ClientRequest {
    private UserLanguage userLanguage;

    public UpdateLanguageRequest(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }
}
