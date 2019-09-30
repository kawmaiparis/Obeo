package ClientServer.ServerResponse;

import holidays_table.ObeoHoliday;

public class DeleteHolidayResponse extends ServerResponse {

    private ObeoHoliday holiday;

    public DeleteHolidayResponse(ObeoHoliday holiday) {
        this.holiday = holiday;
    }

    public ObeoHoliday getHoliday() {
        return holiday;
    }
}
