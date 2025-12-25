package src.main.java.ru.courses.parser;




import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessLogParser {

    public static void main(String[] args) {
        String path = "access.log";
        parseLogFile(path);
    }

    public static void parseLogFile(String path) {
        File file = new File(path);

        if (!file.exists()) {
            System.err.println(" Файл не найден: " + path);
            return;
        }
        if (!file.isFile()) {
            System.err.println(" Указанный путь — не файл: " + path);
            return;
        }

        int totalRequests = 0;
        int yandexBotCount = 0;
        int googleBotCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalRequests++;

                // Извлекаем User-Agent (предполагаем, что он в кавычках)
                String[] parts = line.split("\"");
                if (parts.length >= 7) {
                    String userAgent = parts[6].trim();

                    if (userAgent.contains("YandexBot")) {
                        yandexBotCount++;
                    } else if (userAgent.contains("Googlebot")) {
                        googleBotCount++;
                    }
                }
            }

            // Вывод статистики
            double yandexBotRatio = totalRequests > 0 ? (double) yandexBotCount / totalRequests : 0;
            double googleBotRatio = totalRequests > 0 ? (double) googleBotCount / totalRequests : 0;

            System.out.printf(" Анализ завершён:%n");
            System.out.printf(" Общее количество запросов: %d%n", totalRequests);
            System.out.printf(" Запросы от YandexBot: %d (%.2f%%)%n", yandexBotCount, yandexBotRatio * 100);
            System.out.printf(" Запросы от GoogleBot: %d (%.2f%%)%n", googleBotCount, googleBotRatio * 100);

        } catch (IOException e) {
            System.err.println(" Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}