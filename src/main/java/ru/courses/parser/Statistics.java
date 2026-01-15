package src.main.java.ru.courses.parser;




import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private long totalTraffic = 0;
    private ZonedDateTime minTime = null;
    private ZonedDateTime maxTime = null;

    // Для существующих страниц (код 200)
    private Set<String> uniquePages = new HashSet<>();

    // Для несуществующих страниц (код 404)
    private Set<String> nonExistentPages = new HashSet<>();

    // Для статистики ОС
    private Map<String, Integer> osCounter = new HashMap<>();

    // Для статистики браузеров
    private Map<String, Integer> browserCounter = new HashMap<>();

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        if (minTime == null || entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }
        if (maxTime == null || entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }

        // Существующие страницы (код 200)
        if (entry.getResponseCode() == 200) {
            uniquePages.add(entry.getPath());
        }

        // Несуществующие страницы (код 404)
        if (entry.getResponseCode() == 404) {
            nonExistentPages.add(entry.getPath());
        }

        // Статистика ОС
        String os = entry.getUserAgent().getOs();
        osCounter.merge(os, 1, Integer::sum);

        // Статистика браузеров
        String browser = entry.getUserAgent().getBrowser();
        browserCounter.merge(browser, 1, Integer::sum);
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || totalTraffic == 0) {
            return 0.0;
        }
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }

    // --- Part 1: Существующие страницы ---
    public Set<String> getUniquePages() {
        return new HashSet<>(uniquePages);
    }

    // --- Part 2: Несуществующие страницы ---
    public Set<String> getNonExistentPages() {
        return new HashSet<>(nonExistentPages);
    }

    // --- Part 3: Статистика ОС ---
    public Map<String, Double> getOsStatistics() {
        Map<String, Double> result = new HashMap<>();
        int total = osCounter.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return result;
        for (Map.Entry<String, Integer> entry : osCounter.entrySet()) {
            result.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return result;
    }

    // --- Part 4: Статистика браузеров ---
    public Map<String, Double> getBrowserStatistics() {
        Map<String, Double> result = new HashMap<>();
        int total = browserCounter.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return result;
        for (Map.Entry<String, Integer> entry : browserCounter.entrySet()) {
            result.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return result;
    }
}