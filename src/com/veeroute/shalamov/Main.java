package com.veeroute.shalamov;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by viacheslav on 08.09.15.
 */
public class Main {
    public static void main(String[] args) {

        ReadCSV reader = new ReadCSV();
        List<Order> orders = reader.read("stops.csv");
        int n = orders.size();
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
        System.out.println(next.size());
        Solution solution = new AnnealingSolution(next, map);
        ((AnnealingSolution)solution).setParameters(0.1, 0.0001, 1.0001, 1.0/1000.0);
        Plan t = solution.constructPlan(next);
        System.out.println(t.getSize());
        int initialCost = t.getCost();

        long time = System.currentTimeMillis();
        Plan plan = solution.getPlan();
        time = System.currentTimeMillis() - time;
        time /= 1000;
        System.out.println("initial size: " + t.getSize());
        System.out.println("size: "+ plan.getSize());
        System.out.println("cost: " + plan.getCost());
        System.out.println("initial MST cost: " + initialCost);
        System.out.println("time: " + time);

        WriteCSV writer = new WriteCSV();
        writer.archive(n, plan, "MST+annealing_" + time + "s");



//        Date[] start = new Date[4];
//        Date[] end = new Date[4];
//        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
//        try {
//            start[0] = format.parse("09:00:00");
//            end[0] = format.parse("12:00:00");
//            start[1] = format.parse("12:00:00");
//            end[1] = format.parse("15:00:00");
//            start[2] = format.parse("15:00:00");
//            end[2] = format.parse("18:00:00");
//            start[3] = format.parse("18:00:00");
//            end[3] = format.parse("21:00:00");
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Solution solution = new MultiTimeSolution(orders, map, start, end);
//
//        long time = System.currentTimeMillis();
//        Plan plan = solution.getPlan();
//        time = System.currentTimeMillis() - time;
//        time /= 1000;
//        System.out.println("cost: " + plan.getCost());
//        System.out.println("time: " + time);
//
//        WriteCSV writer = new WriteCSV();
//        writer.archive(n, plan, "MST+annealing+courierPooling_" + time + "s");


    }
}