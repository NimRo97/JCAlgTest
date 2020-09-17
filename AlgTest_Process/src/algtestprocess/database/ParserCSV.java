/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algtestprocess.database;

import static algtestprocess.database.DatabaseUtils.ANOTATED_ALGORITHMS_FILE;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author nimro
 */
public class ParserCSV {
    
    public static final String CARD_NAME_FIELD = "Card name";
    public static final String METHOD_NAME_FIELD = "method name:";
    public static final String CONFIG_FIELD = "measurement config:";
    public static final String BASELINE_FIELD = "baseline measurements (ms):";
    public static final String BASELINE_STATS_FIELD = "baseline stats (ms):";
    public static final String OPERATION_FIELD = "operation raw measurements (ms):";
    public static final String OPERATION_STATS_FIELD = "operation stats (ms/op):";
    public static final String INFO_FIELD = "operation info:";
    
    public static final int CONFIG_LINE_OFFSET = 0;
    public static final int BASELINE_LINE_OFFSET = 1;
    public static final int BASELINE_STATS_LINE_OFFSET = 2;
    public static final int OPERATION_LINE_OFFSET = 3;
    public static final int OPERATION_STATS_LINE_OFFSET = 4;
    public static final int INFO_LINE_OFFSET = 5;
    
    public static PerformanceFixed parsePerformanceFixed(String path) {
        
        List<String> lines;
        PerformanceFixed results = null;
        System.out.println(path + ": ");
        try (Stream<String> input = Files.lines(Paths.get(path))) {
            lines = input.collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println("ERROR: annotated_algorithms.list not found");
            return null;
        }
        
        for (int i = 0; i < lines.size(); i++) {
            String[] data = lines.get(i).split(";");
            if (data.length <= 1) {
                continue;
            }
            
            if (data[0].equals(CARD_NAME_FIELD)) {
                results = new PerformanceFixed(data[1].strip());
                continue;
            }
            
            if (data[0].equals(METHOD_NAME_FIELD)) {
                String methodName = data[1].strip();
                if (!DatabaseUtils.algorithmList.contains(methodName)) {
                    System.out.println("    " + methodName);
                }
                MeasurementFixed m = parseMeasurement(methodName, lines, i + 1);
                results.putMeasurement(m);
            }
        }
        
        return results;
    }
    
    public static MeasurementFixed parseMeasurement(String methodName,
                                                    List<String> lines,
                                                    int index) {
        /* Example of measurement in the CSV file:
        method name:; ALG_SHA MessageDigest_doFinal()
        measurement config:;appletPrepareINS;34;appletMeasureINS;41;config;00...
        baseline measurements (ms):;79.00;78.00;78.00;79.00;78.00;
        baseline stats (ms):;avg:;78.40;min:;78.00;max:;79.00;
        operation raw measurements (ms):;290.60;291.60;283.60;284.60;287.60;
        operation stats (ms/op):;avg op:;5.75;min op:;5.67;max op:;5.83;
        operation info:;data length;256;total iterations;250;total invocations;250;
        */
        
        // measurement config line
        String[] data = lines.get(index + CONFIG_LINE_OFFSET).split(";");
        if (!data[0].equals(CONFIG_FIELD)) {
            return new MeasurementFixed(methodName, data[0]);
        }
        String prepareIns = data[2];
        String measureIns = data[4];
        String config = data[6];
        
        // baseline measurements line
        data = lines.get(index + BASELINE_LINE_OFFSET).replaceAll(",", ".").split(";");
        if (!data[0].equals(BASELINE_FIELD)) {
            return new MeasurementFixed(methodName, data[0]);
        }
        float[] baseline = new float[5];
        for (int i = 0; i < 5; i++) {
            baseline[i] = Float.parseFloat(data[i + 1]);
        }
        
        // skip stats - computed on demand
        data = lines.get(index + BASELINE_STATS_LINE_OFFSET).split(";");
        if (!data[0].equals(BASELINE_STATS_FIELD)) {
            return new MeasurementFixed(methodName, data[0]);
        }
        
        // operation raw measurements line
        data = lines.get(index + OPERATION_LINE_OFFSET).replaceAll(",", ".").split(";");
        if (!data[0].equals(OPERATION_FIELD)) {
            return new MeasurementFixed(methodName, data[0]);
        }
        int len = data.length - 1;
        float[] operation = new float[len];
        for (int i = 0; i < len; i++) {
            operation[i] = Float.parseFloat(data[i + 1]);
        }
        
        // skip stats - computed on demand
        data = lines.get(index + OPERATION_STATS_LINE_OFFSET).split(";");
        if (!data[0].equals(OPERATION_STATS_FIELD)) {
            return new MeasurementFixed(methodName, data[0]);
        }
        
        // operation info line
        data = lines.get(index + INFO_LINE_OFFSET).split(";");
        if (!data[0].equals(INFO_FIELD)) {
            return new MeasurementFixed(methodName, data[0]);
        }
        int dataLength = Integer.parseInt(data[2]);
        int iterations = Integer.parseInt(data[4]);
        int invocations = Integer.parseInt(data[6]);
        
        return new MeasurementFixed(methodName, prepareIns, measureIns, config,
                                    baseline, operation,
                                    dataLength, iterations, invocations);
    }
}
