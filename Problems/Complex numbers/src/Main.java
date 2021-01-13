class ComplexNumber {

    private final double re;
    private final double im;

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public int hashCode() {
        double result = 17;
        result = 31 * result + re;
        result = 31 * result + im;
        return (int) result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ComplexNumber)) {
            return false;
        }

        ComplexNumber complexNumber = (ComplexNumber) obj;
        return this.re == complexNumber.getRe() && this.im == complexNumber.getIm();
    }

}