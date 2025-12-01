import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int validFileCount = 0;

        while (true) {
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine();

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            // Проверка на есть или нет файла
            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Файл не существует.");
                } else {
                    System.out.println("Указанный путь является путём к папке, а не к файлу.");
                }
                continue;
            }

           
            System.out.println("Путь указан верно.");
            validFileCount++;
            System.out.println("Это файл номер " + validFileCount);
        }
    }
}