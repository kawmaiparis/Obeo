package ClientServer.ServerResponse;

import holidays_table.ObeoHoliday;
import interests.UserInterest;
import java.util.List;
import languages.UserLanguage;
import user.ObeoUser;

public class LoginResponse extends ServerResponse {
    private ObeoUser obeoUser;
    private List<ObeoHoliday> holidayList;
    private List<UserInterest> userInterestList;
    private List<UserLanguage> userLanguageList;

    public LoginResponse(ObeoUser obeoUser, List<ObeoHoliday> holidayList,
        List<UserInterest> userInterestList, List<UserLanguage> userLanguageList) {
        this.obeoUser = obeoUser;
        this.holidayList = holidayList;
        this.userInterestList = userInterestList;
        this.userLanguageList = userLanguageList;
    }

    public ObeoUser getObeoUser() {
        return obeoUser;
    }

    public List<ObeoHoliday> getHolidayList() {
        return holidayList;
    }

    public List<UserInterest> getUserInterestList() {
        return userInterestList;
    }

    public List<UserLanguage> getUserLanguageList() {
        return userLanguageList;
    }
}
