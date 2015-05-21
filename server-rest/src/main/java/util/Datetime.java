package util;

import java.util.Date;


/**
 * Created by arno on 21/05/15.
 *
 * Class used to specify the current time when changing the mean state or the intervention creation
 *
 *
 */
public class Datetime {

    public Date getCurrentDate()
    {
        Date date = new Date();

        // Milliseconds removal
        date.setTime(date.getTime() - (date.getTime() % 1000));

        return date;
    }
}
