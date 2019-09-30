package ClientServer.ClientRequests;

import holidays_table.ObeoHoliday;

public class LookingForLocalsRequest extends ClientRequest {

    private ObeoHoliday holiday;

    public LookingForLocalsRequest(ObeoHoliday holiday) {
        this.holiday = holiday;
    }

    public ObeoHoliday getHoliday() {
        return holiday;
    }
}
