/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algtestprocess.database;

import static algtestprocess.database.DatabaseUtils.*;
import java.util.Arrays;

/**
 * Class to hold information about one fixed performance measurement
 * 
 * @author NimRo97
 */
public class MeasurementFixed {
    
    public String methodName;
    
    // Configuraion settings
    public String prepareIns;
    public String measureIns;
    public String config;
    
    public float[] baseline;
    public float[] operation;
    
    public int dataLength;
    public int iterations;
    public int invocations;
    
    public String error = null;
    
    public MeasurementFixed(String methodName, String error) {
        this.methodName = methodName;
        this.error = error;
    }
    
    public MeasurementFixed(String methodName,
                            String prepareIns, String measureIns, String config,
                            float[] baseline, float[] operation,
                            int dataLength, int iterations, int invocations) {
        
        if (baseline == null || operation == null) {
            throw new IllegalArgumentException("Null array in argument.");
        }
        if (baseline.length != MEASURES_COUNT) {
            throw new IllegalArgumentException("Baseline length must be " +
                                               MEASURES_COUNT + ".");
        }
        
        this.methodName = methodName;
        
        this.prepareIns = prepareIns;
        this.measureIns = measureIns;
        this.config = config;
        
        this.baseline = baseline;
        this.operation = operation;
        
        this.dataLength = dataLength;
        this.iterations = iterations;
        this.invocations = invocations;
    }
    
    public float baselineAvg() {
        return arrayAvg(baseline);
    }
    
    public float baselineMin() {
        return arrayMin(baseline);
    }
    
    public float baselineMax() {
        return arrayMax(baseline);
    }
    
    public float operationAvg() {
        return arrayAvg(operation) / TARGET_OPERATION_COUNT;
    }
    
    public float operationMin() {
        return arrayMin(operation) / TARGET_OPERATION_COUNT;
    }
    
    public float operationMax() {
        return arrayMax(operation) / TARGET_OPERATION_COUNT;
    }
    
    public boolean hasError() {
        return error != null;
    }

    @Override
    public String toString() {
        if (hasError()) {
            return methodName + ": " + error;
        }
        return methodName + ":\n" + 
                "appletPrepareINS: " + prepareIns + "\n" +
                "appletMeasureINS: " + measureIns + "\n" +
                "config: " + config + "\n" +
                "baseline: " + Arrays.toString(baseline) + "\n" +
                "operation: " + Arrays.toString(operation) + "\n" +
                "data length: " + dataLength + "\n" +
                "total iterations: " + iterations + "\n" +
                "total invocations: " + invocations + "\n";
    }
    
    
}
