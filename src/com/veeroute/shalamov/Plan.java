package com.veeroute.shalamov;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 09.09.15.
 */
public class Plan {

    public List<Route> routes;

    public Plan(List<Route> routes) {
        this.routes = routes;
    }


    public int getCost() {
        int acc = 0;
        for (Route r : routes) {
            acc += r.getCost();
        }
        return acc;
    }

    public int getSize() {
        int acc = 0;
        for (Route r : routes)
            acc += r.wayPoints.size();
        return acc;
    }

    public boolean lessThan(Plan other) {
        return this.getCost() < other.getCost();
    }

}
