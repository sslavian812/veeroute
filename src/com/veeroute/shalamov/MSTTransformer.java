package com.veeroute.shalamov;

import java.util.List;

/**
 * Created by viacheslav on 09.09.15.
 */
public class MSTTransformer implements Transformer {

    Map map;

    MSTTransformer(Map map) {
        this.map = map;
    }

    @Override
    public List<Order> f(List<Order> orders) {
        MSTSolution s = new MSTSolution(orders, map);
        return s.arrangeAsMST();
    }
}
