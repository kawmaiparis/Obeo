package holidays_table;

import database.RecordObject;
import java.sql.Date;

public class ObeoHoliday extends RecordObject {
    private Date start_date;
    private Date end_date;
    private long user_id;
    private String destination_city;
    private double longitude;
    private double latitude;
    private long selection_time;

    /*Constructer for making from the database*/
    public ObeoHoliday(long id, String destination_city, Date start_date, Date end_date,
                       long user_id, double longitude, double latitude, long selection_time) {
        super(id, "holidays", "holiday_id");
        this.start_date = start_date;
        this.end_date = end_date;
        this.user_id = user_id;
        this.destination_city = destination_city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.selection_time = selection_time;
    }

    /*Constructor for add to the database*/
    public ObeoHoliday(String destination_city, Date start_date, Date end_date, long user_id,
                       double longitude, double latitude, long selection_time) {
        this(-1, destination_city, start_date, end_date, user_id, longitude, latitude,  selection_time);
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObeoHoliday)
            return this.getId() == ((ObeoHoliday) obj).getId();
        return false;
    }

    public long getSelection_time() {
        return selection_time;
    }
}
