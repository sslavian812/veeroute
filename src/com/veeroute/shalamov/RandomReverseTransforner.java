package com.veeroute.shalamov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by viacheslav on 09.09.15.
 */
public class RandomReverseTransforner implements Transformer {
    @Override
    public List<Order> f(List<Order> orders) {
        List<Order> copy = new ArrayList<>(orders.size());

        int max = 2;
        Random r = new Random();
        int subsetSize = (int)(r.nextDouble() * max) + 2;
        int startIndex = (int) (r.nextDouble() * (orders.size() - (max+2)));
        for (int i = 0; i < startIndex; ++i) {
            copy.add(orders.get(i));
        }

        for (int i = startIndex + subsetSize - 1; i >= startIndex; --i) {
            copy.add(orders.get(i));
        }

        for (int i = startIndex + subsetSize; i < orders.size(); ++i) {
            copy.add(orders.get(i));
        }
        return copy;
    }
}
