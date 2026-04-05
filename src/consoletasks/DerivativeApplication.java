package consoletasks;

import java.io.*;

public class DerivativeApplication {
    public static void main(String[] args) throws IOException {
        Evaluatable[] functs = new Evaluatable[2];
        functs[0] = new AnalyticalFunction(0.5); // Функція 1: аналітична
        functs[1] = new FileListInterpolation(); // Функція 2: таблична

        try {
            ((FileListInterpolation)functs[1]).readFromFile("TblFunc.dat");
        } catch (IOException ex) {
            System.out.println("Помилка читання файлу! Переконайся, що TblFunc.dat створено.");
            System.exit(-1);
        }

        String fileName = "";
        for (Evaluatable f: functs) {
            System.out.println("Функція: " + f.getClass().getSimpleName());
            fileName = f.getClass().getSimpleName() + ".dat";
            PrintWriter out = new PrintWriter(new FileWriter(fileName));

            for (double x = 1.5; x <= 6.5; x += 0.05) {
                System.out.println("x: " + x + "\tf: " + f.evalf(x) + "\tf': " + NumMethods.der(x, 1.0e-4, f));
                out.printf("%16.6e%16.6e%16.6e\n", x, f.evalf(x), NumMethods.der(x, 1.0e-4, f));
            }
            System.out.println("\n");
            out.close();
        }
        System.out.println("Обчислення завершено! Файли створено в корені проєкту.");
    }
}