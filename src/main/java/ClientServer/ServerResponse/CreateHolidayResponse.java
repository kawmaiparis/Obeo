package ClientServer.ServerResponse;

import holidays_table.ObeoHoliday;

public class CreateHolidayResponse extends ServerResponse {

    private ObeoHoliday holidayId;

    public CreateHolidayResponse(ObeoHoliday holidayId) {
        assert (holidayId.getId() != -1);
        this.holidayId = holidayId;
    }

    public ObeoHoliday getHolidayId() {
        return holidayId;
    }
}
