package com.veeroute.shalamov;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslav on 08.09.15.
 */
public class ReadCSV {

    public List<Order> orders;

    public ReadCSV() {
        orders = new ArrayList<>();
    }

    public List<Order> read(String csvFile) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] ss = line.split(cvsSplitBy);
                orders.add(new Order(ss[0], ss[1], ss[2], ss[3], ss[4], ss[5], ss[6]));
            }

        } catch (FileNotFoundException e) {
            System.out.println("file: " + csvFile + " not found!");
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

        System.out.println("read file " + csvFile + " successful");
        System.out.println(orders.size() + " orders found.");
        return orders;
    }
}
