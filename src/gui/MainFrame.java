package gui;

import consoletasks.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    // Оголошуємо елементи інтерфейсу
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton calcButton;

    public MainFrame() {
        super("Розрахунок похідної (РГР)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. Створюємо модель таблиці з трьома колонками
        String[] columns = {"x", "Функція f(x)", "Похідна f'(x)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Додаємо таблицю в панель прокрутки (щоб можна було гортати вниз)
        JScrollPane scrollPane = new JScrollPane(table);

        // 2. Створюємо кнопку
        calcButton = new JButton("Обчислити");
        calcButton.setFont(new Font("Arial", Font.BOLD, 14));

        // 3. Додаємо дію для кнопки (що буде відбуватися при натисканні)
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateData();
            }
        });

        // 4. Додаємо елементи у наше вікно (таблицю - по центру, кнопку - вниз)
        add(scrollPane, BorderLayout.CENTER);
        add(calcButton, BorderLayout.SOUTH);
    }

    // Метод, який виконує математику і заповнює таблицю
    private void calculateData() {
        // Очищаємо таблицю перед новим розрахунком
        tableModel.setRowCount(0);

        // Беремо нашу аналітичну функцію (як в Завданні 1)
        Evaluatable func = new AnalyticalFunction(0.5);
        // Беремо наш новий калькулятор похідної (із Завдання 2)
        DerivativeCalculator calculator = new CentralDifference();

        // Рахуємо в циклі і додаємо результати як нові рядки в таблицю
        for (double x = 1.5; x <= 6.5; x += 0.05) {
            double fx = func.evalf(x);
            double dfx = calculator.calculate(x, 1.0e-4, func);

            // Форматуємо числа, щоб вони виглядали красиво (по 4 та 6 знаків після коми)
            Object[] row = {
                    String.format("%.4f", x),
                    String.format("%.6f", fx),
                    String.format("%.6f", dfx)
            };
            tableModel.addRow(row);
        }
    }
}