package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by viacheslav on 09.09.15.
 */
public class AnnealingSolution implements Solution {

    List<Order> orders;
    Map map;
    int n;


    public AnnealingSolution(List<Order> orders, Map map) {
        this.orders = orders;
        this.map = map;
        n = orders.size();
    }

    @Override
    public Plan getPlan() {
        double initT = 50;
        double minT = 0.7;
        int i = 1;
        Collections.shuffle(orders, new Random());
        double curT = initT;
        Plan lastPlan = constructPlan(orders);
        Plan bestPlan = constructPlan(orders);
        int lastEnergy = lastPlan.getCost();
        int bestEnergy = bestPlan.getCost();

        while (curT > minT) {
            curT = initT / Math.pow(1.00002, i);
            ++i;
            List<Order> nextStep = f(orders);
            Plan nextPlan = constructPlan(nextStep);
            int nextEnergy = nextPlan.getCost();
            double delta = nextEnergy - lastEnergy;
            if (delta <= 0) {
//                lastPlan = nextPlan;
                orders = nextStep;
                lastEnergy = nextEnergy;
            } else {
//                delta = delta /lastEnergy* 100;
                double p = Math.exp(-delta / curT / 1000);
                System.out.println("iteration: " + i + ". enegy: " + lastEnergy + ". temperature: " + curT + ". probability: " + p + " delta: " + delta);
                if ((new Random().nextDouble()) < p) {
//                    lastPlan = nextPlan;
                    orders = nextStep;
                    lastEnergy = nextEnergy;
                    System.out.println("allowed!");
                }
            }
            if (nextEnergy < bestEnergy) {
                bestPlan = nextPlan;
                bestEnergy = nextEnergy;
            }
        }

        return bestPlan;
    }

    private List<Order> f(List<Order> orders) {
        List<Order> copy = new ArrayList<>(orders.size());

        for (int i = 0; i < orders.size(); ++i) {
            copy.add(orders.get(i));
        }

        Collections.shuffle(copy);

//        int max = 2;
//        Random r = new Random();
//        int subsetSize = (int)(r.nextDouble() * max) + 2;
//        int startIndex = (int) (r.nextDouble() * (orders.size() - (max+2)));
//        for (int i = 0; i < startIndex; ++i) {
//            copy.add(orders.get(i));
//        }
//
//        for (int i = startIndex + subsetSize - 1; i >= startIndex; --i) {
//            copy.add(orders.get(i));
//        }
//
//        for (int i = startIndex + subsetSize; i < orders.size(); ++i) {
//            copy.add(orders.get(i));
//        }
        return copy;
    }

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
