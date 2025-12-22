package src.main.java.ru.courses.parser;



import java.io.*;

public class AccessLogParser {

    public static void main(String[] args) {
        String path = "access.log";
        parseLogFile(path);
    }

    public static void parseLogFile(String path) {
        File file = new File(path);

        // Проверка файла
        if (!file.exists()) {
            System.err.println(" Файл не найден: " + path);
            return;
        }
        if (!file.isFile()) {
            System.err.println(" Указанный путь — не файл: " + path);
            return;
        }

        int totalLines = 0;
        int maxLength = 0;
        int minLength = Integer.MAX_VALUE;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                int length = line.length();

                // Проверка длины строки (критерий 5)
                if (length > 1024) {
                    throw new LineTooLongException(
                            String.format("Строка #%d слишком длинная: %d символов (макс. 1024)",
                                    totalLines, length)
                    );
                }

                // Обновление макс/мин
                if (length > maxLength) maxLength = length;
                if (length < minLength) minLength = length;
            }

            // Вывод результатов (критерии 2,3,4)
            System.out.println(" Анализ файла '" + path + "' завершён:");
            System.out.println("• Общее количество строк: " + totalLines);
            System.out.println("• Длина самой длинной строки: " + maxLength);
            System.out.println("• Длина самой короткой строки: " + minLength);

        } catch (LineTooLongException e) {
            // Критерий 5 и 6: собственный класс исключения наследуется от RuntimeException
            System.err.println(" Прервано: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(" Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}

