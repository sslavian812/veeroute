package com.veeroute.shalamov;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 08.09.15.
 */
public class Route {
    public List<WayPoint> wayPoints;
    private Map map;

    public static final int PER_HOUR = 250;
    public static final int PER_COURIER = 1000;


    public Route(Map map) {
        this(map, new ArrayList<WayPoint>());
    }

    public Route(Map map, List<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
        this.map = map;
    }

    public int getCost() {
        double acc = (double) PER_COURIER;
        int lastOrder = -1;
        for (WayPoint w : wayPoints) {
            if (lastOrder == -1) {
                lastOrder = w.orderId;
                continue;
            }
            acc += ((double) map.time(lastOrder, w.orderId) / 60 / 60) * PER_HOUR;
            lastOrder = w.orderId;
        }
        return (int) acc;
    }
}
