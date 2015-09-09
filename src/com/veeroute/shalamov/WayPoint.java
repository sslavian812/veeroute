package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by viacheslav on 08.09.15.
 */
public class WayPoint {
    Integer routeId;
    Integer stop;  // порядковый номер заказа в маршруте
    Integer orderId;
    String location;
    Date twFrom;
    Date twTo;
    Double x;
    Double y;
    Date arrival; // прибытие на заказ
    Date start;   // начало выполнения
    Date finish;  // окончание выполнения (start + order.work)

    public WayPoint()
    {}

    public WayPoint(Order order, int routeId)
    {
        this.routeId = routeId;
        orderId = order.id;
        location = order.description;
        twFrom = order.from;
        twTo = order.to;
        x = order.x;
        y = order.y;
    }

    @Override
    public String toString() {
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                routeId, stop, orderId, location,
                format.format(twFrom), format.format(twTo),
                x, y, format.format(arrival),
                format.format(start), format.format(finish));
    }
}
