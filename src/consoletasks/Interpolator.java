package consoletasks;

public abstract class Interpolator implements Evaluatable {
    public abstract void clear();
    public abstract int numPoints();
    public abstract void addPoint(Point2D pt);
    public abstract Point2D getPoint(int i);
    public abstract void setPoint(int i, Point2D pt);
    public abstract void removeLastPoint();
    public abstract void sort();

    @Override
    public double evalf(double x) {
        double res = 0.0;
        int numData = numPoints();
        double numer, denom;

        for (int k = 0; k < numData; k++) {
            numer = 1.0;
            denom = 1.0;
            for (int j = 0; j < numData; j++) {
                if (j != k) {
                    numer = numer * (x - getPoint(j).getX());
                    denom = denom * (getPoint(k).getX() - getPoint(j).getX());
                }
            }
            res = res + getPoint(k).getY() * numer / denom;
        }
        return res;
    }
}