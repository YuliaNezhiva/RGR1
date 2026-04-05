package consoletasks;

public interface DerivativeCalculator {
    double calculate(double x, double tol, Evaluatable f);
}