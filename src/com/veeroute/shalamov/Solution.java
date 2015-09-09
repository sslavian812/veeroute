package com.veeroute.shalamov;

import java.util.List;

/**
 * Created by viacheslav on 09.09.15.
 */
public interface Solution {
    public Plan getPlan();
    public Plan constructPlan(List<Order> orders);
}
