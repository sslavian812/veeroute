package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 10.09.15.
 */
public class AnnealingScheduler {

    private Transformer transformer = new RandomReverseTransforner();

    Map map;
    List<Order> orders;
    int n;

    List<Courier> originalPool;
    List<Courier> bestPool;


    long startTime;
    long timeGap;

    private double initT = 0.05;
    private double minT = 0.0005;
    private double alpha = 1.00001;
    private double balance = 1.0 / 1000.0;


    long curEnergy;
    long bestEnergy;


    public AnnealingScheduler(Map map, List<Courier> pool) {
        this.map = map;
        this.originalPool = pool;
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        startTime = 0;
        try {
            startTime = format.parse("09:00:00").getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.timeGap = 12 * 60 * 60;
    }

    public AnnealingScheduler(Map map, List<Courier> pool, Date start, Date end) {
        this.map = map;
        this.originalPool = pool;
        this.startTime = start.getTime() / 1000;
        this.timeGap = (end.getTime() - start.getTime()) / 1000;
    }

    public void setParameters(double initT,
                              double minT,
                              double alpha,
                              double balance) {
        this.initT = initT;
        this.minT = minT;
        this.alpha = alpha;
        this.balance = balance;
    }

    private long getCost(List<Courier> couriers) {
        return new Plan(makeRoutes(couriers)).getCost();
    }


    public List<Courier> schedule(List<Order> orders, long additionalCost) {
        this.orders = orders;
        this.n = orders.size();
        List<Courier> curPool = constructPool(orders);

        curEnergy = getCost(curPool) + additionalCost;
        bestEnergy = getCost(curPool) + additionalCost;
        getBestPool();
        return bestPool;
    }


    private void getBestPool() {
        int i = 1;
        double curT = initT;


        while (curT > minT) {
            curT = initT / Math.pow(alpha, i);
            ++i;
            List<Order> nextOrders = transformer.f(orders);

            long nextEnergy = getCost(constructPool(nextOrders));
            double delta = nextEnergy - curEnergy;
            if (delta <= 0) {
                orders = nextOrders;
                curEnergy = nextEnergy;
            } else {
                double p = Math.exp(-delta / curT * balance + 3);
                   System.out.println("iteration: " + i + ". enegy: " + curEnergy + ". temperature: " + curT + ". probability: " + p + " delta: " + delta);
                if ((new Random().nextDouble()) < p) {
                    orders = nextOrders;
                    curEnergy = nextEnergy;
                    System.out.println("allowed!");
                }
            }
        }

    }

    private List<Route> makeRoutes(List<Courier> couriers) {
        return couriers.stream()
                .map(c -> new Route(map, c.wayPoints))
                .collect(Collectors.toList());
    }

    private List<Courier> constructPool(List<Order> orders) {
        Courier courier = null;
        List<Courier> curPool = originalPool.stream().map(c -> new Courier(c))
                .collect(Collectors.toList());

        for (Order o : orders) {
            if (courier != null && !courier.isAvailable(o, map.time(courier.curOrderId, o.id))) {
                courier = null;
            }

            if (courier == null) {
                courier = getCourier(o, curPool);
                if (courier == null) {
                    courier = new Courier(curPool.size(), 0, startTime);
                    curPool.add(courier);
                    courier.addOrder(o, 0);
                    continue;
                }
            }
            courier.addOrder(o, map.time(courier.curOrderId, o.id));
        }

        if (bestPool == null)
            bestPool = curPool;

        if (getCost(bestPool) > getCost(curPool))
            bestPool = curPool;

        return curPool;
    }

    private Courier getCourier(Order order, List<Courier> couriers) {
        if (couriers.isEmpty())
            return null;
        Courier nearestCourier = null;
        for (Courier c : couriers) {
            if (c.curTime + map.time(c.curOrderId, order.id) < startTime + timeGap) {
                if (nearestCourier == null
                        || map.time(nearestCourier.curOrderId, order.id) > map.time(c.curOrderId, order.id)) {
                    nearestCourier = c;
                }
            }
        }
        return nearestCourier;
    }
}
