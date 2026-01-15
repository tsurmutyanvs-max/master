package src.main.java.ru.courses.parser;



import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private long totalTraffic = 0;
    private ZonedDateTime minTime = null;
    private ZonedDateTime maxTime = null;

    // Для хранения уникальных страниц (код 200)
    private Set<String> uniquePages = new HashSet<>();

    // Для подсчёта частоты ОС
    private Map<String, Integer> osCounter = new HashMap<>();

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        if (minTime == null || entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }
        if (maxTime == null || entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }

        // Part 1: добавляем страницу, если код ответа 200
        if (entry.getResponseCode() == 200) {
            uniquePages.add(entry.getPath());
        }

        // Part 2: считаем ОС
        String os = entry.getUserAgent().getOs();
        osCounter.merge(os, 1, Integer::sum); // если ключ есть — прибавляем 1, если нет — создаём с 1
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || totalTraffic == 0) {
            return 0.0;
        }
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }


    public Set<String> getUniquePages() {
        return new HashSet<>(uniquePages); // возвращаем копию, чтобы нельзя было изменить внутреннее состояние
    }


    public Map<String, Double> getOsStatistics() {
        Map<String, Double> result = new HashMap<>();
        int total = osCounter.values().stream().mapToInt(Integer::intValue).sum();

        if (total == 0) {
            return result; // пустой Map, если нет данных
        }

        for (Map.Entry<String, Integer> entry : osCounter.entrySet()) {
            String os = entry.getKey();
            int count = entry.getValue();
            double ratio = (double) count / total;
            result.put(os, ratio);
        }

        return result;
    }
}