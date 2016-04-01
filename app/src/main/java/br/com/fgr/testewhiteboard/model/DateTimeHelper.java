package br.com.fgr.testewhiteboard.model;

public class DateTimeHelper {

    private DateTimeHelper() {

    }

    public static String timeFormatted(int hour, int minute) {

        String hourFormatted = hour < 10 ? 0 + "" + hour : hour + "";
        String minuteFormatted = minute < 10 ? 0 + "" + minute : minute + "";

        return String.format("%s:%s", hourFormatted, minuteFormatted);


    }

    public static String dateFormatted(int day, int month, int year) {

        String dayFormatted = day < 10 ? 0 + "" + day : day + "";
        String monthFormatted = month < 10 ? 0 + "" + month : month + "";
        String yearFormatted = year < 10 ? 0 + "" + year : year + "";

        return String.format("%s/%s/%s", dayFormatted, monthFormatted, yearFormatted);


    }

}
