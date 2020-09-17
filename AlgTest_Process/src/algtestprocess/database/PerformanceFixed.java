/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algtestprocess.database;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nimro
 */
public class PerformanceFixed {
    
    String cardName;
    public Map<String, MeasurementFixed> measurements;
    
    
    public PerformanceFixed(String cardName) {
        this.cardName = cardName;
        measurements = new HashMap<>();
    }
    
    public void putMeasurement(MeasurementFixed measurement) {
        measurements.put(measurement.methodName, measurement);
    }
    
    public MeasurementFixed getMeasurement(String methodName) {
        return measurements.get(methodName);
    }
    
    public boolean hasMeasurement(String methodName) {
        return measurements.containsKey(methodName);
    }
    
}
