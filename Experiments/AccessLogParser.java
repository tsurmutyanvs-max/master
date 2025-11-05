package Experiments;

import java.util.Scanner;

public class AccessLogParser {
    public static <scanner> void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Вывожу сообщения перед запросом чисел
        System.out.println("Введи первое число: ");
        int a = scanner.nextInt();

        System.out.println("Введи второе число");
        int b = scanner.nextInt();

        //Вычисление и вывод результата
        System.out.println("Сложение - " + (a + b));
        System.out.println("Вычитание - " + (a - b));
        System.out.println("Деление - " + (a / b));
        System.out.println("Умножение - " + (a * b));
        System.out.println("Дробное - " + ((double) a / b));
    }
}




