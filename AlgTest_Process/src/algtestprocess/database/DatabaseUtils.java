/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algtestprocess.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author nimro
 */
public class DatabaseUtils {
    
    public static final int MEASURES_COUNT = 5;
    public static final int TARGET_OPERATION_COUNT = 50;
    public static final String ANNOTATED_ALGORITHMS_FILE = "annotated_algorithms.list";
    public static final String NEW_ALGORITHMS_FILE = "all_algorithms.list";
    
    public static Set<String> algorithms;
    public static Set<String> newAlgorithms;
    public static Set<String> foundAlgorithms;
    
    private static final DatabaseUtils databaseUtils = new DatabaseUtils();
    
    private DatabaseUtils() {
        
        List<String> input;
        algorithms = new HashSet<>();
        newAlgorithms = new HashSet<>();
        foundAlgorithms = new HashSet<>();
        
        try (Stream<String> lines = Files.lines(Paths.get(ANNOTATED_ALGORITHMS_FILE))) {
            input = lines.collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println("ERROR: annotated_algorithms.list not found");
            return;
        }
        
        for (String line : input) {
            algorithms.add(line.strip().split(";")[0]);
        }
        algorithms.remove("");
        
        try (Stream<String> lines = Files.lines(Paths.get(NEW_ALGORITHMS_FILE))) {
            input = lines.collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println("ERROR: annotated_algorithms.list not found");
            return;
        }
        
        for (String line : input) {
            newAlgorithms.add(line.strip());
        }
        newAlgorithms.remove("");
    }
    
    public static DatabaseUtils getInstance() {
        return databaseUtils; 
    }
    
    
    public static float arraySum(float[] array) {
        float sum = 0;
        for (float i : array) {
            sum += i;
        }
        return sum;
    }
    
    public static float arrayAvg(float[] array) {
        return arraySum(array) / array.length;
    }
    
    public static float arrayMin(float[] array) {
        float min = array[0];
        for (float i : array) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }
    
    public static float arrayMax(float[] array) {
        float max = array[0];
        for (float i : array) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }
}
