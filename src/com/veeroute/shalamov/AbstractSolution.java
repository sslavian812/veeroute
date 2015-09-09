package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by viacheslav on 09.09.15.
 */
public class AbstractSolution {

    List<Order> orders;
    Map map;
    int n;

    public Plan constructPlan(List<Order> orders) {
        List<Route> routes = new ArrayList<>();
        Route route = new Route(map);
        int routeId = 0;
        int orderIndex = 0;

        int previousOrder = -1;

        DateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
        long startTime = 0;
        try {
            startTime = format.parse("09:00:00").getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curTime = startTime;
        long timeGap = 12 * 60 * 60;

        for (Order o : orders) {
            if (previousOrder == -1) {
                WayPoint wp = new WayPoint(o, routeId);
                wp.stop = orderIndex;
                orderIndex++;
                wp.arrival = new Date((curTime) * 1000);
                wp.start = wp.arrival;
                wp.finish = new Date((curTime + o.work) * 1000);
                curTime += o.work;
                previousOrder = o.id;
                route.wayPoints.add(wp);
            }
            long required = map.time(previousOrder, o.id);
            if (curTime + required > startTime + timeGap) {
                routes.add(route);
                route = new Route(map);
                routeId++;
                orderIndex = 0;
                curTime = startTime;
                previousOrder = o.id;

                WayPoint wp = new WayPoint(o, routeId);
                wp.stop = orderIndex;
                orderIndex++;
                wp.arrival = new Date((curTime) * 1000);
                wp.start = wp.arrival;
                wp.finish = new Date((curTime + o.work) * 1000);
                curTime += o.work;
                route.wayPoints.add(wp);

                continue;
            }

            WayPoint wp = new WayPoint(o, routeId);
            wp.stop = orderIndex;
            orderIndex++;
            wp.arrival = new Date((curTime + required) * 1000);
            wp.start = wp.arrival;
            wp.finish = new Date((curTime + required + o.work) * 1000);
            curTime += required + o.work;
            previousOrder = o.id;
            route.wayPoints.add(wp);
        }
        return new Plan(routes);
    }
}
