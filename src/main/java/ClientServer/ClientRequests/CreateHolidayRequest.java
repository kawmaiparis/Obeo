package ClientServer.ClientRequests;

import holidays_table.ObeoHoliday;

public class CreateHolidayRequest extends ClientRequest {

    private ObeoHoliday holiday;

    public CreateHolidayRequest(ObeoHoliday holiday) {
        assert (holiday.getId() == -1);
        this.holiday = holiday;
    }

    public ObeoHoliday getHoliday() {
        return holiday;
    }
}
