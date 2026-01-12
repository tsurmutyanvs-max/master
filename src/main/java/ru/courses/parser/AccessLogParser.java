package src.main.java.ru.courses.parser;


import java.io.*;

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

        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}