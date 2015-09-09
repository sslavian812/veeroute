package com.veeroute.shalamov;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by viacheslav on 08.09.15.
 */
public class Map {

    private double[][] dist;
    private int[][] time;

    public Map(String filename, int size) {
        dist = new double[size+1][size+1];
        time = new int[size+1][size+1];

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(filename));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] ss = line.split(cvsSplitBy);
                int i = Integer.parseInt(ss[0]);
                int j = Integer.parseInt(ss[1]);
                double d =  Double.parseDouble(ss[2]);
                int t = Integer.parseInt(ss[3]);
                dist[i][j] = dist[j][i] = d;
                time[i][j] = time[j][i] = t;
            }

        } catch (FileNotFoundException e) {
            System.out.println("file: " + filename + " not found!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public double dist(int i, int j) {
        return dist[i][j];
    }

    public int time(int i, int j) {
        return time[i][j];
    }
}
