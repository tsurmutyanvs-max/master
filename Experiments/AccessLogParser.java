package Experiments;

import java.util.Scanner;

public class AccessLogParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();


        System.out.println(a + b);      // сумма
        System.out.println(a - b);      // разность
        System.out.println(a * b);      // произведение
        System.out.println((double) a / b); // частное как double

        scanner.close();
    }
}