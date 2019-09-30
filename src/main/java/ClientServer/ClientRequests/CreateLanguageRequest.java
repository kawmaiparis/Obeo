package ClientServer.ClientRequests;

import languages.UserLanguage;

public class CreateLanguageRequest extends ClientRequest {
    private UserLanguage userLanguage;

    public CreateLanguageRequest(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }
}
