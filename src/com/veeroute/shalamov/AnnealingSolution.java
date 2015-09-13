package com.veeroute.shalamov;

import java.util.List;
import java.util.Random;

/**
 * Created by viacheslav on 09.09.15.
 */
public class AnnealingSolution extends AbstractSolution implements Solution {

    private Transformer transformer = new RandomReverseTransforner();

    private double initT = 0.10;
    private double minT = 0.01;
    private double alpha = 1.00001;
    private double balance = 1.0 / 1000.0;


    public void setParameters(double initT,
                              double minT,
                              double alpha,
                              double balance) {
        this.initT = initT;
        this.minT = minT;
        this.alpha = alpha;
        this.balance = balance;
    }


    public AnnealingSolution(List<Order> orders, Map map) {
        super(orders, map);
    }

    private List<Order> bestOrders;

    @Override
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
               // bestPlan = nextPlan;
                bestOrders = nextOrders;
                bestEnergy = nextEnergy;
            }
        }

        return constructPlan(bestOrders);
    }

    public List<Order> arrangeAnnealing() {
        getPlan();
        return bestOrders;
    }
}
