package gui;

import consoletasks.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class MainFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private FileListInterpolation interpolation = new FileListInterpolation();

    public MainFrame() {
        super("Розрахунок похідної (РГР)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- СТВОРЕННЯ МЕНЮ ---
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Відкрити .dat файл");
        JMenuItem exitItem = new JMenuItem("Вихід");

        // Дія для кнопки "Відкрити"
        openItem.addActionListener(e -> openFile());
        // Дія для кнопки "Вихід"
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.addSeparator(); // Лінія-розділювач
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // --- ТАБЛИЦЯ ---
        String[] columns = {"x", "f(x)", "f'(x)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Кнопка знизу для розрахунку (залишаємо для зручності)
        JButton calcButton = new JButton("Обчислити за замовчуванням (Analytical)");
        calcButton.addActionListener(e -> calculateAnalytical());
        add(calcButton, BorderLayout.SOUTH);
    }

    // Метод для вибору файлу через вікно
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser(new File(".")); // Відкриваємо в папці проєкту
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                interpolation.readFromFile(selectedFile.getAbsolutePath());
                calculateTableData(interpolation);
                JOptionPane.showMessageDialog(this, "Файл успішно зчитано!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Помилка при читанні файлу!", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Розрахунок для аналітичної функції
    private void calculateAnalytical() {
        calculateTableData(new AnalyticalFunction(0.5));
    }

    // Загальний метод для заповнення таблиці
    private void calculateTableData(Evaluatable func) {
        tableModel.setRowCount(0);
        DerivativeCalculator calculator = new CentralDifference();

        // Якщо це таблична функція, беремо її точки, якщо аналітична - крокуємо циклом
        if (func instanceof FileListInterpolation) {
            FileListInterpolation fli = (FileListInterpolation) func;
            for (int i = 0; i < fli.numPoints(); i++) {
                double x = fli.getPoint(i).getX();
                double fx = fli.evalf(x);
                double dfx = calculator.calculate(x, 1.0e-4, fli);
                tableModel.addRow(new Object[]{
                        String.format("%.4f", x), String.format("%.6f", fx), String.format("%.6f", dfx)
                });
            }
        } else {
            for (double x = 1.5; x <= 6.5; x += 0.05) {
                tableModel.addRow(new Object[]{
                        String.format("%.4f", x),
                        String.format("%.6f", func.evalf(x)),
                        String.format("%.6f", calculator.calculate(x, 1.0e-4, func))
                });
            }
        }
    }
}