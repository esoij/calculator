package esoij.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

public class Number {
    public BigDecimal value;
    public boolean isNaN = false;
    public boolean isNegative = false;
    public boolean isInfinite = false;

    public Number() {
    }

    public Number(BigDecimal d) {
        this.value = d;
    }

    public Number(BigInteger d) {
        this(new BigDecimal(String.valueOf(d)));
    }

    public Number(java.lang.Number d) {
        this(new BigDecimal(String.valueOf(d)));
    }

    public Number(String s) {
        switch (s) {
            case "NaN" -> this.isNaN = true;
            case "inf" -> this.isInfinite = true;
            case "-inf" -> this.isInfinite = this.isNegative = true;
            case null -> {
            }
            default -> this.value = new BigDecimal(s);
        }
    }

    public Number negate() {
        return new Number(this.value.negate());
    }

    public Number abs() {
        return new Number(this.value.abs());
    }

    public Number sqrt() {
        Number result;
        try {
            result = new Number(this.value.sqrt(MathContext.UNLIMITED));
        } catch (ArithmeticException e) {
            result = new Number(this.value.sqrt(MathContext.DECIMAL128));
        }
        return result;
    }

    public boolean greaterThan(java.lang.Number other) {
        return this.value.compareTo(new BigDecimal(String.valueOf(other))) > 0;
    }

    public boolean greaterThanOrEqualTo(java.lang.Number other) {
        return this.value.compareTo(new BigDecimal(String.valueOf(other))) >= 0;
    }

    public boolean lessThan(Number other) {
        return this.value.compareTo(other.value) < 0;
    }

    public boolean lessThan(java.lang.Number other) {
        return lessThan(new Number(other));
    }

    public Number and(Number other) {
        return new Number(this.value.toBigInteger().and(other.value.toBigInteger()));
    }

    public Number and(java.lang.Number other) {
        return and(new Number(other));
    }

    public Number or(Number other) {
        return new Number(this.value.toBigInteger().or(other.value.toBigInteger()));
    }

    public Number shiftRight(int other) {
        return new Number(this.value.toBigInteger().shiftRight(other));
    }

    public Number mul(Number other) {
        if (other.isInfinite) {
            if (other.isNegative) return Numbers.NEGATIVE_INFINITY;
            return Numbers.INFINITY;
        }
        return new Number(this.value.multiply(other.value));
    }

    public Number mul(java.lang.Number other) {
        return this.mul(new Number(other));
    }

    public Number divide(Number other) {
        Number result;
        try {
            result = new Number(this.value.divide(other.value));
        } catch (ArithmeticException e) {
            result = new Number(this.value.divide(other.value, MathContext.DECIMAL128));
        }
        return result;
    }

    public Number divide(java.lang.Number other) {
        return divide(new Number(other));
    }

    public Number add(Number other) {
        return new Number(this.value.add(other.value));
    }

    public Number add(java.lang.Number other) {
        return add(new Number(other));
    }

    public Number subtract(Number other) {
        return new Number(this.value.subtract(other.value));
    }

    public Number subtract(java.lang.Number other) {
        return new Number(this.value.subtract(new BigDecimal(String.valueOf(other))));
    }

    @Override
    public String toString() {
        if (this.value == null) {
            String value = "";
            if (this.isNegative) value += '-';
            if (this.isInfinite) value += "inf";
            if (this.isNaN)      value += "NaN";
            if (!this.isInfinite && !this.isNaN) value += "null";
            return value;
        }
        return this.value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof java.lang.Number)
            return new Number(String.valueOf(o)) instanceof Number other && (this == other ||
                    Objects.equals(this.value, other.value)
                            && this.isNaN == other.isNaN
                            && this.isNegative == other.isNegative
                            && this.isInfinite == other.isInfinite);
        return o instanceof Number other && (this == other ||
                Objects.equals(this.value, other.value)
                        && this.isNaN == other.isNaN
                        && this.isNegative == other.isNegative
                        && this.isInfinite == other.isInfinite);
    }
}
