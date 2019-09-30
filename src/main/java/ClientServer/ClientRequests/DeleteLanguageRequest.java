package ClientServer.ClientRequests;

import languages.UserLanguage;

public class DeleteLanguageRequest extends ClientRequest{

    private UserLanguage userLanguage;

    public DeleteLanguageRequest(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }
}
