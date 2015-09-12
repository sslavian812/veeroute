package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 10.09.15.
 */
public class AnnealingTransformer implements Transformer {

    private Transformer transformer = new RandomReverseTransforner();

    Map map;
    List<Order> orders;
    int n;

    List<Courier> pool;
    List<Courier> originalPool;

    long startTime;
    long timeGap;

    private double initT = 0.10;
    private double minT = 0.001;
    private double alpha = 1.0001;
    private double balance = 1.0 / 1000.0;


    public AnnealingTransformer(Map map, List<Courier> pool) {
        this.map = map;
        this.originalPool = pool;
        this.pool = new ArrayList<>(originalPool);
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        startTime = 0;
        try {
            startTime = format.parse("09:00:00").getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.timeGap = 12 * 60 * 60;
    }

    public AnnealingTransformer(Map map, List<Courier> pool, Date start, Date end) {
        this.map = map;
        this.originalPool = pool;
        this.pool = new ArrayList<>(originalPool);
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


    @Override
    public List<Order> f(List<Order> orders) {
        this.orders = orders;
        this.n = orders.size();
        getPlan();
        return bestOrders;
    }


    private List<Order> bestOrders;
    private List<Courier> bestPool;

    public Plan getPlan() {
        int i = 1;
        double curT = initT;
        Plan lastPlan = constructPlan(orders);
        bestOrders = orders;
        int lastEnergy = lastPlan.getCost();
        int bestEnergy = lastPlan.getCost();

        while (curT > minT) {
            curT = initT / Math.pow(alpha, i);
            ++i;
            List<Order> nextOrders = transformer.f(orders);
            Plan nextPlan = constructPlan(nextOrders);
            int nextEnergy = nextPlan.getCost();
            double delta = nextEnergy - lastEnergy;
            if (delta <= 0) {
                orders = nextOrders;
                lastEnergy = nextEnergy;
            } else {
                double p = Math.exp(-delta / curT * balance);
                System.out.println("iteration: " + i + ". enegy: " + lastEnergy + ". temperature: " + curT + ". probability: " + p + " delta: " + delta);
                if ((new Random().nextDouble()) < p) {
                    orders = nextOrders;
                    lastEnergy = nextEnergy;
                    System.out.println("allowed!");
                }
            }
            if (nextEnergy < bestEnergy) {
                bestOrders = nextOrders;
                bestEnergy = nextEnergy;
            }
        }

        originalPool = bestPool;

        List<Route> bestRoutes = bestPool.stream()
                .map(c -> new Route(map, c.wayPoints))
                .collect(Collectors.toList());
        return new Plan(bestRoutes);
    }


    public Plan constructPlan(List<Order> orders) {
        Courier courier = null;
        pool = new ArrayList<>(originalPool);

        for (Order o : orders) {
            if (courier != null && !courier.isAvailable(o, map.time(courier.curOrderId, o.id))) {
                courier = null;
            }

            if (courier == null) {
                courier = getCourier(o);
                if (courier == null) {
                    courier = new Courier(pool.size(), 0, startTime);
                    pool.add(courier);
                    courier.addOrder(o, 0);
                    continue;
                }
            }
            courier.addOrder(o, map.time(courier.curOrderId, o.id));
        }

        List<Route> routes = new ArrayList<>();
        for (Courier c : pool) {
            routes.add(new Route(map, c.wayPoints));
        }

        if(bestPool == null)
            bestPool = new ArrayList<>(pool);

        List<Route> bestRoutes = bestPool.stream()
                .map(c -> new Route(map, c.wayPoints))
                .collect(Collectors.toList());

        if((new Plan(bestRoutes)).getCost() > (new Plan(routes)).getCost())
            bestPool = pool;

        return new Plan(routes);
    }

    private Courier getCourier(Order order) {
        if (pool.isEmpty())
            return null;
        Courier nearestCourier = null;
        for (Courier c : pool) {
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
