package com.veeroute.shalamov;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by viacheslav on 10.09.15.
 */
public class Courier {
    int id;
    int freeSlot;

    long curTime;
    int curOrderId;
    List<WayPoint> wayPoints;

    public Courier(int id, int freeSlot, long curTime) {
        this.id = id;
        this.freeSlot = freeSlot;
        this.curTime = curTime;
        curOrderId = -1;
        wayPoints = new ArrayList<>();
    }

    public Courier(Courier other) {
        this.id = other.id;
        this.freeSlot = other.freeSlot;
        this.curTime = other.curTime;
        this.curOrderId = other.curOrderId;
        wayPoints = new ArrayList<>();
        for (WayPoint wp : other.wayPoints)
            this.wayPoints.add(wp);
    }

    public boolean isAvailable(Order order, long dest) {
        return (curTime + dest <= order.to.getTime() / 1000);
    }

    public void addOrder(Order order, long dest) {
        WayPoint wp = new WayPoint(order, id);
        wp.stop = freeSlot;
        freeSlot++;

        wp.arrival = new Date((curTime + dest) * 1000);

        wp.start = new Date(Math.max((curTime + dest) * 1000, order.from.getTime()));
        wp.finish = new Date(wp.start.getTime() + order.work * 1000);
        curTime = wp.finish.getTime() / 1000;

        curOrderId = order.id;
        wayPoints.add(wp);
        freeSlot++;
    }
}
