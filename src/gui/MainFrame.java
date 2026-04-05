package gui;

import consoletasks.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class MainFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private GraphPanel graphPanel = new GraphPanel();
    private FileListInterpolation interpolation = new FileListInterpolation();

    public MainFrame() {
        super("Розрахунок похідної (РГР) - Юлія");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Вкладки
        JTabbedPane tabbedPane = new JTabbedPane();

        // Вкладка з таблицею
        String[] columns = {"x", "f(x)", "f'(x)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        tabbedPane.addTab("Таблиця", new JScrollPane(table));

        // Вкладка з графіком
        tabbedPane.addTab("Графік", graphPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Меню
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Відкрити .dat файл");
        openItem.addActionListener(e -> openFile());
        fileMenu.add(openItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JButton calcBtn = new JButton("Аналітична функція");
        calcBtn.addActionListener(e -> {
            Evaluatable f = new AnalyticalFunction(0.5);
            calculateTableData(f);
            graphPanel.setFileData(f);
        });
        add(calcBtn, BorderLayout.SOUTH);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser(new File("."));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                interpolation.readFromFile(chooser.getSelectedFile().getAbsolutePath());
                calculateTableData(interpolation);
                graphPanel.setFileData(interpolation);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Помилка файлу");
            }
        }
    }

    private void calculateTableData(Evaluatable func) {
        tableModel.setRowCount(0);
        DerivativeCalculator calculator = new CentralDifference();
        for (double x = 1.5; x <= 6.5; x += 0.1) {
            tableModel.addRow(new Object[]{
                    String.format("%.2f", x),
                    String.format("%.4f", func.evalf(x)),
                    String.format("%.4f", calculator.calculate(x, 1.0e-4, func))
            });
        }
    }
}