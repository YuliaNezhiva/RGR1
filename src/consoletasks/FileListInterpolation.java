package consoletasks;

import java.io.*;
import java.util.*;

public class FileListInterpolation extends ListInterpolation {

    public FileListInterpolation() {
        super();
    }

    public void readFromFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s = in.readLine(); // читання рядка із заголовками стовпців
        clear();
        while ((s = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s);
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            addPoint(new Point2D(x, y));
        }
        in.close();
    }

    public void writeToFile(String fileName) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        out.printf("%9s%25s\n", "x", "y");
        for (int i = 0; i < numPoints(); i++) {
            out.println(getPoint(i).getX() + "\t" + getPoint(i).getY());
        }
        out.close();
    }
}