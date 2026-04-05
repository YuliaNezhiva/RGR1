package gui;

import consoletasks.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {
    private List<Point2D> points = new ArrayList<>();
    private List<Point2D> derPoints = new ArrayList<>();

    public void setFileData(Evaluatable func) {
        points.clear();
        derPoints.clear();
        DerivativeCalculator calc = new CentralDifference();
        for (double x = 1.5; x <= 6.5; x += 0.1) {
            points.add(new Point2D(x, func.evalf(x)));
            derPoints.add(new Point2D(x, calc.calculate(x, 1.0e-4, func)));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Малюємо осі
        g2.drawLine(50, h / 2, w - 50, h / 2); // X
        g2.drawLine(w / 2, 50, w / 2, h - 50); // Y

        if (points.isEmpty()) return;

        // Малюємо графік функції (синім)
        g2.setColor(Color.BLUE);
        drawGraph(g2, points, w, h);

        // Малюємо графік похідної (червоним)
        g2.setColor(Color.RED);
        drawGraph(g2, derPoints, w, h);

        g2.setColor(Color.BLACK);
        g2.drawString("Синій: f(x), Червоний: f'(x)", 60, 60);
    }

    private void drawGraph(Graphics2D g2, List<Point2D> data, int w, int h) {
        for (int i = 0; i < data.size() - 1; i++) {
            int x1 = (int) (w / 2 + data.get(i).getX() * 50);
            int y1 = (int) (h / 2 - data.get(i).getY() * 50);
            int x2 = (int) (w / 2 + data.get(i + 1).getX() * 50);
            int y2 = (int) (h / 2 - data.get(i + 1).getY() * 50);
            g2.drawLine(x1, y1, x2, y2);
        }
    }
}