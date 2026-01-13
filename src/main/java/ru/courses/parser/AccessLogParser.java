package src.main.java.ru.courses.parser;




import java.io.*;
import java.util.Map;
import java.util.Set;

public class AccessLogParser {

    public static void main(String[] args) {
        String path = "access.log";
        parseLogFile(path);
    }

    public static void parseLogFile(String path) {
        File file = new File(path);

        if (!file.exists()) {
            System.err.println("Файл не найден: " + path);
            return;
        }
        if (!file.isFile()) {
            System.err.println("Указанный путь — не файл: " + path);
            return;
        }

        Statistics stats = new Statistics();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    LogEntry entry = new LogEntry(line);
                    stats.addEntry(entry);
                } catch (IllegalArgumentException e) {
                    System.err.println("Пропущена строка: " + e.getMessage());
                }
            }

            System.out.printf("Анализ завершён:%n");
            System.out.printf("• Средний трафик за час: %.2f байт%n", stats.getTrafficRate());

            Set<String> pages = stats.getUniquePages();
            System.out.println("Уникальные страницы: " + pages);

            Map<String, Double> osStats = stats.getOsStatistics();
            osStats.forEach((os, ratio) -> System.out.printf("%s: %.2f%%\n", os, ratio * 100));

        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}