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

    List<Courier> pool;

    MultiTimeSolution(List<Order> orders, Map map, Date[] start, Date[] end) {
        this.orders = orders;
        this.map = map;
        this.n = orders.size();
        this.start = start;
        this.end = end;
        this.pool = new ArrayList<>();
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
            Transformer annealingTransformer = new AnnealingTransformer(
                    map, pool, start[bucketNumber], end[bucketNumber]);
            // todo set parameters
            bucket = annealingTransformer.f(bucket);
            constructPlan(bucket);
        }

        List<Route> routes = new ArrayList<>();
        for (Courier c : pool) {
            routes.add(new Route(map, c.wayPoints));
        }
        return new Plan(routes);
    }

    /**
     * Constructs routes from given orders (ordered as they are)
     * using nearest available couriers from pool and creates new couriers if necessary.
     *
     * @param orders
     * @return
     */
    @Override
    public Plan constructPlan(List<Order> orders) {
        return null;
    }
}
