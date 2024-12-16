package data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegionCityData {
    private static final Map<String, List<String>> regionCityMap = new LinkedHashMap<>();

    static {
        regionCityMap.put("I", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("II", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("III", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("IV-A", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("IV-B", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("V", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("VI", List.of(
                "Kalibo, Aklan",
                "Iloilo City, Iloilo",
                "Roxas City, Capiz"));
        regionCityMap.put("VII", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("VIII", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("IX", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("X", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("XI", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("XII", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("XIII", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("NCR", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("CAR", List.of(
                "c1, p1",
                "c2, p2"));
        regionCityMap.put("BARMM", List.of(
                "c1, p1",
                "c2, p2"));
    }

    public static List<String> getRegions() {
        return List.copyOf(regionCityMap.keySet());
    }

    public static List<String> getCities(String region) {
        return regionCityMap.getOrDefault(region, List.of());
    }
}
