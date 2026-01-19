package src.main.java.ru.courses.parser;


package ru.courses.parser;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private long totalTraffic = 0;
    private ZonedDateTime minTime = null;
    private ZonedDateTime maxTime = null;

    // Для хранения всех записей
    private final List<LogEntry> entries = new ArrayList<>();

    // === ЗАДАНИЕ #2: Часть 1 — Пиковая посещаемость в секунду ===
    private final Map<Long, Integer> visitsPerSecond = new HashMap<>();

    // === ЗАДАНИЕ #2: Часть 2 — Сайты-рефереры ===
    private final Set<String> referringSites = new HashSet<>();

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        if (minTime == null || entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }
        if (maxTime == null || entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }

        entries.add(entry);

        // Часть 1: Пиковая посещаемость (только не-боты)
        if (!entry.getUserAgent().isBot()) {
            long second = entry.getTime().toEpochSecond();
            visitsPerSecond.merge(second, 1, Integer::sum);
        }

        // Часть 2: Рефереры
        String referer = entry.getReferer();
        if (referer != null && !referer.isEmpty() && !referer.equals("-")) {
            String domain = extractDomain(referer);
            if (domain != null && !domain.isEmpty()) {
                referringSites.add(domain);
            }
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

    // === МЕТОД 1: Пиковая посещаемость в секунду ===
    public int getPeakVisitsPerSecond() {
        if (visitsPerSecond.isEmpty()) {
            return 0;
        }
        return Collections.max(visitsPerSecond.values());
    }

    // === МЕТОД 2: Сайты, со страниц которых есть ссылки ===
    public Set<String> getReferringSites() {
        return new HashSet<>(referringSites);
    }

    // === МЕТОД 3: Максимальная посещаемость одним пользователем ===
    public int getMaxVisitsPerUser() {
        if (entries.isEmpty()) {
            return 0;
        }

        Map<String, Long> visitsByIp = entries.stream()
                .filter(entry -> !entry.getUserAgent().isBot())
                .collect(Collectors.groupingBy(
                        LogEntry::getIpAddr,
                        Collectors.counting()
                ));

        if (visitsByIp.isEmpty()) {
            return 0;
        }

        return visitsByIp.values().stream()
                .mapToInt(Long::intValue)
                .max()
                .orElse(0);
    }

    // === Вспомогательный метод: извлечение домена из referer ===
    private String extractDomain(String referer) {
        try {
            String url = referer.trim();
            if (url.startsWith("https://")) {
                url = url.substring(8);
            } else if (url.startsWith("http://")) {
                url = url.substring(7);
            }
            int slashIndex = url.indexOf('/');
            if (slashIndex != -1) {
                url = url.substring(0, slashIndex);
            }
            return url.isEmpty() ? null : url;
        } catch (Exception e) {
            return null;
        }
    }
}