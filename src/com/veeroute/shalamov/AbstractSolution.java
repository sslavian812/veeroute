package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 09.09.15.
 */
public class AbstractSolution implements Solution {

    List<Order> orders;
    Map map;
    int n;

    public AbstractSolution(List<Order> orders, Map map) {
        this.orders = orders;
        this.map = map;
        n = orders.size();
    }

    @Override
    public Plan constructPlan(List<Order> orders) {
        Courier courier = null;
        ArrayList<Courier> couriers = new ArrayList<>();
        long startTime = 0;
        DateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
        try {
            startTime = format.parse("09:00:00").getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cn = 0;
        for (Order o : orders) {
            if (courier == null || !courier.isAvailable(o, map.time(courier.curOrderId, o.id))) {
                courier = new Courier(cn, 0, startTime);
                couriers.add(courier);
                ++cn;
                courier.addOrder(o, 0);
                continue;
            }

            courier.addOrder(o, map.time(courier.curOrderId, o.id));
        }

        List<Route> routes = new ArrayList<>();
        for (Courier c : couriers) {
            routes.add(new Route(map, c.wayPoints));
        }
        return new Plan(routes);
    }

//    @Override
//    public Plan constructPlan(List<Order> orders) {
//        List<Route> routes = new ArrayList<>();
//        Route route = new Route(map);
//        int routeId = 0;
//        int orderIndex = 0;
//
//        int previousOrder = -1;
//
//        DateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
//        long startTime = 0;
//        try {
//            startTime = format.parse("09:00:00").getTime() / 1000;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long curTime = startTime;
//        long timeGap = 12 * 60 * 60;
//
//        for (Order o : orders) {
//            if (previousOrder == -1) {
//                WayPoint wp = new WayPoint(o, routeId);
//                wp.stop = orderIndex;
//                orderIndex++;
//                wp.arrival = new Date((curTime) * 1000);
//                wp.start = wp.arrival;
//                wp.finish = new Date((curTime + o.work) * 1000);
//                curTime += o.work;
//                previousOrder = o.id;
//                route.wayPoints.add(wp);
//            }
//            long required = map.time(previousOrder, o.id);
//            if (curTime + required > startTime + timeGap) {
//                routes.add(route);
//                route = new Route(map);
//                routeId++;
//                orderIndex = 0;
//                curTime = startTime;
//                previousOrder = o.id;
//
//                WayPoint wp = new WayPoint(o, routeId);
//                wp.stop = orderIndex;
//                orderIndex++;
//                wp.arrival = new Date((curTime) * 1000);
//                wp.start = wp.arrival;
//                wp.finish = new Date((curTime + o.work) * 1000);
//                curTime += o.work;
//                route.wayPoints.add(wp);
//
//                continue;
//            }
//
//            WayPoint wp = new WayPoint(o, routeId);
//            wp.stop = orderIndex;
//            orderIndex++;
//            wp.arrival = new Date((curTime + required) * 1000);
//            wp.start = wp.arrival;
//            wp.finish = new Date((curTime + required + o.work) * 1000);
//            curTime += required + o.work;
//            previousOrder = o.id;
//            route.wayPoints.add(wp);
//        }
//        return new Plan(routes);
//    }


    @Override
    public Plan getPlan() {
        return null;
    }
}
