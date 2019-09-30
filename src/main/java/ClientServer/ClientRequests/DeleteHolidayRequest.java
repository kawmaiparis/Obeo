package ClientServer.ClientRequests;

import holidays_table.ObeoHoliday;

public class DeleteHolidayRequest extends ClientRequest {
    private ObeoHoliday obeoHoliday;

    public DeleteHolidayRequest(ObeoHoliday obeoHoliday) {
        this.obeoHoliday = obeoHoliday;
    }

    public ObeoHoliday getObeoHoliday() {
        return obeoHoliday;
    }
}
