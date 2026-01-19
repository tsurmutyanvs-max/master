package src.main.java.ru.courses.parser;


import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private long totalTraffic = 0;
    private ZonedDateTime minTime = null;
    private ZonedDateTime maxTime = null;

    // Для хранения всех записей
    private List<LogEntry> entries = new ArrayList<>();

    // Для подсчёта ошибок
    private int errorCount = 0;

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        if (minTime == null || entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }
        if (maxTime == null || entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }

        entries.add(entry);

        // Считаем ошибки
        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorCount++;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || totalTraffic == 0) {
            return 0.0;
        }
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) totalTraffic / hours;
    }

    // --- Part 1: Среднее количество посещений за час (не-боты) ---
    public double getAverageVisitsPerHour() {
        if (minTime == null || maxTime == null || entries.isEmpty()) {
            return 0.0;
        }
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        long nonBotVisits = entries.stream()
                .filter(entry -> !entry.getUserAgent().isBot())
                .count();
        return (double) nonBotVisits / hours;
    }

    // --- Part 2: Среднее количество ошибочных запросов за час ---
    public double getAverageErrorRequestsPerHour() {
        if (minTime == null || maxTime == null || entries.isEmpty()) {
            return 0.0;
        }
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0) hours = 1;
        return (double) errorCount / hours;
    }

    // --- Part 3: Средняя посещаемость одним пользователем (не-боты) ---
    public double getAverageVisitsPerUser() {
        if (entries.isEmpty()) {
            return 0.0;
        }
        long nonBotVisits = entries.stream()
                .filter(entry -> !entry.getUserAgent().isBot())
                .count();
        long uniqueUsers = entries.stream()
                .filter(entry -> !entry.getUserAgent().isBot())
                .map(LogEntry::getIpAddr)
                .distinct()
                .count();
        if (uniqueUsers == 0) {
            return 0.0;
        }
        return (double) nonBotVisits / uniqueUsers;
    }

    public Set<String> getUniquePages() {
        return null;
    }

    public Map<String, Double> getOsStatistics() {
        return null;
    }
}