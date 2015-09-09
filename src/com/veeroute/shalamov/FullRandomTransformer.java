package com.veeroute.shalamov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by viacheslav on 09.09.15.
 */
public class FullRandomTransformer implements Transformer{

    @Override
    public List<Order> f(List<Order> orders) {
        List<Order> copy = new ArrayList<>(orders.size());
        for (int i = 0; i < orders.size(); ++i) {
            copy.add(orders.get(i));
        }
        Collections.shuffle(copy);
        return copy;
    }
}
