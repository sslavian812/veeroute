package com.veeroute.shalamov;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viacheslav on 09.09.15.
 */
public class MSTSolution extends AbstractSolution implements Solution {

//    List<Order> orders;
//    Map map;
//    int n;

    public MSTSolution(List<Order> orders, Map map) {
//        this.orders = orders;
//        this.map = map;
//        n = orders.size();
        super(orders, map);
    }

    private List<Pair<Integer, Integer>> mst() {
        List<Pair<Integer, Integer>> t = new ArrayList<>(n);
        boolean[] used = new boolean[n];
        int[] min_e = new int[n];
        int[] sel_e = new int[n];

        Arrays.fill(used, false);
        Arrays.fill(min_e, Integer.MAX_VALUE);
        Arrays.fill(sel_e, -1);

        min_e[0] = 0;
        for (int i = 0; i < n; ++i) {
            int v = -1;
            for (int j = 0; j < n; ++j)
                if (!used[j] && (v == -1 || min_e[j] < min_e[v]))
                    v = j;

            used[v] = true;
            if (sel_e[v] != -1)
                t.add(new Pair<>(v, sel_e[v]));

            for (int to = 0; to < n; ++to) {
                if (map.time(v, to) < min_e[to]) {
                    min_e[to] = map.time(v, to);
                    sel_e[to] = v;
                }
            }
        }
        return t;
    }

    private boolean[] dfsUsed;
    private List<Integer> dfsPath;
    private boolean[][] dfsG;

    private List<Integer> mstPath(List<Pair<Integer, Integer>> tree) {
        dfsUsed = new boolean[n];
        dfsG = new boolean[n][n];
        dfsPath = new ArrayList<>(n);
        for (Pair<Integer, Integer> p : tree) {
            dfsG[p.getKey()][p.getValue()] = true;
            dfsG[p.getValue()][p.getKey()] = true;
        }
        dfsPath.add(0);
        dfs(0);
        return dfsPath;
    }

    private void dfs(int cur) {
        if (dfsUsed[cur])
            return;
        dfsUsed[cur] = true;

        for (int i = 0; i < n; ++i) {
            if (!dfsUsed[i] && dfsG[cur][i]) {
                dfsPath.add(i);
                dfs(i);
            }
        }
    }


    private List<Order> rearrangeOrders(List<Integer> permutation) {
        if (orders.size() != permutation.size())
            throw new IndexOutOfBoundsException("invalid permutation");

        List<Order> next = new ArrayList<>(orders.size());
        for (Order o : orders) {
            next.add(o);
        }
        for (int i = 0; i < permutation.size(); ++i) {
            int at = permutation.get(i);
            next.set(i, orders.get(at));
        }
        return next;
    }

    public List<Order> arrangeAsMST() {
        return rearrangeOrders(mstPath(mst()));
    }

    @Override
    public Plan getPlan() {
        return constructPlan(rearrangeOrders(mstPath(mst())));
    }
}
