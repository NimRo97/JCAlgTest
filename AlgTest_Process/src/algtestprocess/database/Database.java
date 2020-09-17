/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algtestprocess.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nimro
 */
public class Database {
    
    public static void runDemo() {
        
        List<PerformanceFixed> data = new ArrayList<>();
        
        String dir = "../Profiles/performance/fixed";
        final File folder = new File(dir);
        for (final File file : folder.listFiles()) {
            if (file.isFile()) {
                data.add(ParserCSV.parsePerformanceFixed(dir + "/" + file.getName()));
            }
        }
        
        List<String> lines = new ArrayList<>();
        lines.add("Algorithm;Count;Inter Card;Intra Card");
        for (String s : DatabaseUtils.foundAlgorithms) {
            int count = 0;
            float sumInCard = 0;
            List<Float> averages = new ArrayList<>();
            for (PerformanceFixed entry : data) {
                if (entry.hasMeasurement(s)) {
                    MeasurementFixed m = entry.getMeasurement(s);
                    if (m.hasError()) {
                        continue;
                    }
                    
                    count += 1;
                    sumInCard += (m.operationMax() - m.operationMin()) / m.operationAvg();
                    averages.add(m.operationAvg());
                }
            }
            
            float min = Float.POSITIVE_INFINITY;
            float max = 0;
            float sum = 0;
            for (float f : averages) {
                if (f > max) max = f;
                if (f < min) min = f;
                sum += f;
            }
            float betweenCards = (max - min) / (sum / count) * 100;
            float inCard = sumInCard / count * 100;
            
            if (count != 0) {
                lines.add(s + ";" + count + ";" + Math.round(betweenCards) + "%;" + Math.round(inCard) + "%");
            }
        }
        
        try {
            Path file = Paths.get("output.csv");
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
