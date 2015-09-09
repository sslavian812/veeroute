package com.veeroute.shalamov;

import java.util.List;

/**
 * Created by viacheslav on 08.09.15.
 */
public class Main {
    public static void main(String[] args) {

        ReadCSV reader = new ReadCSV();
        List<Order> orders = reader.read("stops.csv");
        int n = Integer.parseInt(args[0]);
        Map map = new Map("matrix.csv", n);


//        Solution solution = new SimpleRandomSolution(orders, map);
//        Plan plan = solution.getPlan();
//        System.out.println("cost: " + plan.getCost());
//        WriteCSV writer = new WriteCSV();
//        writer.archive(n, plan, "simple_random");


//        Collections.shuffle(orders, new Random());
//        Solution solution = new AnnealingSolution(orders, map);
//        long time = System.currentTimeMillis();
//        Plan plan = solution.getPlan();
//        time = System.currentTimeMillis() - time;
//        time /= 1000;
//        System.out.println("cost: " + plan.getCost());
//        System.out.println("time: " + time);
//
//        WriteCSV writer = new WriteCSV();
//        writer.archive(n, plan, "annealing_"+time+"s");


//        Solution solution = new MSTSolution(orders, map);
//        long time = System.currentTimeMillis();
//        Plan plan = solution.getPlan();
//        time = System.currentTimeMillis() - time;
//        time /= 1000;
//        System.out.println("cost: " + plan.getCost());
//        System.out.println("time: " + time);
//
//        WriteCSV writer = new WriteCSV();
//        writer.archive(n, plan, "MST_"+time+"s");


        Transformer transformer = new MSTTransformer(map);
        List<Order> next = transformer.f(orders);

        Solution solution = new AnnealingSolution(next, map);
        Plan t = solution.constructPlan(next);
        System.out.println("initial MST cost: " + t.getCost());

        long time = System.currentTimeMillis();
        Plan plan = solution.getPlan();
        time = System.currentTimeMillis() - time;
        time /= 1000;
        System.out.println("cost: " + plan.getCost());
        System.out.println("time: " + time);

        WriteCSV writer = new WriteCSV();
        writer.archive(n, plan, "MST+annealing_" + time + "s");

    }
}