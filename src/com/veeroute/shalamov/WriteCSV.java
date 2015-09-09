package com.veeroute.shalamov;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by viacheslav on 08.09.15.
 */
public class WriteCSV {


    public void archive(int n, Plan plan) {
        archive(n, plan, "");
    }

    public void archive(int n, Plan plan, String comment) {
        int cost = plan.getCost();
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("MM-dd_hh-mm-ss", Locale.ENGLISH);
        String filename = n + "_" + cost + "__" + format.format(date) + "__" + comment + ".csv";
        write(filename, plan.routes);
    }

    public void write(String filename, List<Route> routes) {
        BufferedWriter bw = null;
        try {
            File file = new File("./" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            bw = new BufferedWriter(new FileWriter(filename));
            for (Route r : routes) {
                for (WayPoint w : r.wayPoints) {
                    bw.write(w.toString());
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
