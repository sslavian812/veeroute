package com.veeroute.shalamov;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by viacheslav on 10.09.15.
 */
public class MultiTimeSolution implements Solution {
    Date[] start;
    Date[] end;

    List<Order> orders;
    Map map;
    int n;

    List<Courier> couriers;

    MultiTimeSolution(List<Order> orders, Map map, Date[] start, Date[] end) {
        this.orders = orders;
        this.map = map;
        this.n = orders.size();
        this.start = start;
        this.end = end;
        this.couriers = new ArrayList<>();
    }

    @Override
    public Plan getPlan() {
        for (int i = 0; i < start.length; ++i) {
            final int bucketNumber = i;
            List<Order> bucket = orders.stream()
                    .filter(o -> o.from.equals(start[bucketNumber]) && o.to.equals(end[bucketNumber]))
                    .collect(Collectors.toList());
            Transformer mstTransformer = new MSTTransformer(map);
            bucket = mstTransformer.f(bucket);


            AnnealingScheduler annealingTransformer = new AnnealingScheduler(
                    map, couriers, start[bucketNumber], end[bucketNumber]);
            // todo set parameters

            List<Route> routes = new ArrayList<>();
            for (Courier c : couriers) {
                routes.add(new Route(map, c.wayPoints));
            }

            couriers = annealingTransformer.schedule(bucket,(new Plan(routes)).getCost());
        }

        List<Route> routes = new ArrayList<>();
        for (Courier c : couriers) {
            routes.add(new Route(map, c.wayPoints));
        }
        return new Plan(routes);
    }

    /**
     * Constructs routes from given orders (ordered as they are)
     * using nearest available couriers from couriers and creates new couriers if necessary.
     *
     * @param orders
     * @return
     */
    @Override
    @Deprecated
    public Plan constructPlan(List<Order> orders) {
        return null;
    }
}
