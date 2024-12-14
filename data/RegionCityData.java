package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionCityData {
    private static final Map<String, List<String>> regionCityMap = new HashMap<>();

    static {
        regionCityMap.put("VI", List.of(
                "Kalibo, Aklan",
                "Iloilo City, Iloilo",
                "Roxas City, Capiz"));
        // Add more regions and cities as needed
    }

    public static List<String> getRegions() {
        return List.copyOf(regionCityMap.keySet());
    }

    public static List<String> getCities(String region) {
        return regionCityMap.getOrDefault(region, List.of());
    }
}
