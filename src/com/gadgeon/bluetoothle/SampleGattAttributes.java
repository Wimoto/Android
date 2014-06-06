package com.gadgeon.bluetoothle;

import java.util.HashMap;

public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String TemperatureCurrentValue = "e0035609-ec48-4ed0-9f3b-5419c00a94fd";    
    public static String LightCurrentValue = "e003560f-ec48-4ed0-9f3b-5419c00a94fd";    
    public static String HumidityCurrentValue = "e0035615-ec48-4ed0-9f3b-5419c00a94fd";

    static {
        attributes.put("e0035609-ec48-4ed0-9f3b-5419c00a94fd", "Current Temperature Value");
        attributes.put("e003560f-ec48-4ed0-9f3b-5419c00a94fd", "Current Light Value");
        attributes.put("e0035615-ec48-4ed0-9f3b-5419c00a94fd", "Current Humidity Value");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
