package solver;

public class Complex {

    private final double real;
    private final double imagery;

    public Complex(double real, double imagery) {
        this.real = real;
        this.imagery = imagery;
    }

    public double getReal() {
        return real;
    }

    public double getImagery() {
        return imagery;
    }

    public Complex add(Complex another) {
        return new Complex(this.real + another.getReal(),
                this.imagery + another.getImagery());
    }

    public Complex multiply(Complex another) {
        return new Complex(this.real * another.getReal() + (-1) *
                this.imagery * another.getImagery(),
                this.real * another.getImagery() +
                        another.getReal() * this.imagery);
    }

    public Complex scalarMultiply(double scalar) {
        return new Complex(this.real * scalar, this.imagery * scalar);
    }

    public Complex negate() {
        return this.scalarMultiply(-1);
    }

    public Complex conjugate() {
        return new Complex(this.real, -this.imagery);
    }

    public Complex divide(Complex another) {
        double divisor = another.multiply(another.conjugate()).getReal();
        Complex divisible = this.multiply(another.conjugate());
        return new Complex(divisible.getReal() / divisor,
                divisible.getImagery() / divisor);
    }

    public boolean isZero() {
        return real == 0 && imagery == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (real != 0) {
            sb.append(real);
        }

        if (imagery != 0) {
            if (imagery > 0 && sb.length() != 0) {
                sb.append("+");
            }
            sb.append(imagery).append("i");
        }

        if (sb.length() == 0) {
            sb.append(0.0);
        }

        return sb.toString();
    }

}
