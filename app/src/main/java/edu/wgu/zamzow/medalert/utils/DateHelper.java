package edu.wgu.zamzow.medalert.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Locale;

public class DateHelper {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat readable = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    private static final SimpleDateFormat reverseFormatter = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy", Locale.US);

    public static Date getDate(String iniDate) {
        try {
            return new Date(formatter.parse(iniDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String showAltDate(Date iniDate) {
        return formatter.format(iniDate);
    }

    public static Date getDateFromDB(String iniDate) {
        try {
            return new Date(reverseFormatter.parse(iniDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String toDBDate(Date iniDate) {
        return reverseFormatter.format(iniDate);
    }

    public static String showDate(Date iniDate) {
        return readable.format(iniDate);
    }
}
