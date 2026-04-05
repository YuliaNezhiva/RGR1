package consoletasks;

public class AnalyticalFunction implements Evaluatable {
    private double a;

    public AnalyticalFunction(double a) {
        this.a = a;
    }

    public AnalyticalFunction() {
        this(1.0); // Значення параметра a за замовчуванням
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    @Override
    public double evalf(double x) {
        return Math.exp(-a * x * x) * Math.sin(x);
    }
}