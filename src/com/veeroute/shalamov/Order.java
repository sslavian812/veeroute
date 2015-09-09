package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by viacheslav on 08.09.15.
 */
public class Order {
    public Integer id;
    public String description;
    public Date from;
    public Date to;
    //public Date work;
    public long work;
    public Double x;
    public Double y;


    public static long diffInSeconds(Date startDate, Date endDate) {
        long resultMills = endDate.getTime() - startDate.getTime();
        return resultMills / 1000;
    }

    public Order(String id, String description, String from, String to, String work, String x, String y) {
        this.id = Integer.parseInt(id);
        this.description = description;

        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

        try {
            this.from = format.parse(from);
            this.to = format.parse(to);
            this.work = (format.parse(work).getTime() - (format.parse("00:00:00")).getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
    }
}
