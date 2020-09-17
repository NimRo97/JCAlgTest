/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algtestprocess.database;

import java.io.File;
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
        
    }
}
