package esoij.calculator;

import esoij.logging.LogUtils;
import org.slf4j.Logger;

public class Equation {
    public static final Logger LOGGER = LogUtils.getLogger();
    public final String equationString;
    int pos = -1;
    int ch;

    public Equation(String equationString) {
        this.equationString = equationString;
    }

    public void nextCharacter() {
        this.ch = (++this.pos < this.equationString.length()) ? this.equationString.charAt(this.pos) : -1;
    }

    public boolean eat(int charToEat) {
        while (this.ch == ' ') this.nextCharacter();
        if (this.ch == charToEat) {
            this.nextCharacter();
            return true;
        }
        return false;
    }

    public Number solve() {
        this.nextCharacter();
        Number x = this.parseExpression();
        if (this.pos < this.equationString.length()) LOGGER.error("Unexpected character: '{}', ignoring all subsequent characters.", (char)this.ch);
        return x;
    }

    public Number parseExpression() {
        Number x = this.parseTerm();
        while(true) {
            if (this.eat('+')) x = x.add(this.parseTerm());
            else if (this.eat('-')) x = x.subtract(this.parseTerm());
            else return x;
        }
    }

    public Number parseTerm() {
        Number x = this.parseFactor();
        while(true) {
            if (this.eat('*')) x = x.mul(this.parseFactor());
            else if (this.eat('/')) x = x.divide(this.parseFactor());
            else return x;
        }
    }

    public Number parseFactor() {
        if (this.eat('+')) return parseFactor();
        if (this.eat('-')) return parseFactor().negate();

        Number x;
        int startPos = this.pos;
        if (this.eat('(')) {
            x = parseExpression();
            if (!eat(')')) throw new RuntimeException("Missing ')'");
        } else if ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') {
            while ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') nextCharacter();
            x = new Number(this.equationString.substring(startPos, this.pos));
        } else if (this.ch >= 'a' && this.ch <= 'z') {
            while (this.ch >= 'a' && this.ch <= 'z') nextCharacter();
            String function = this.equationString.substring(startPos, this.pos);
            if (this.eat('(')) {
                x = this.parseExpression();
                if (!this.eat(')')) throw new RuntimeException("Missing ')' after argument to " + function);
            } else x = this.parseFactor();
            if (function.equals("sqrt")) x = x.sqrt();
            else {
                LOGGER.error("Unexpected function: {}, returning -1.", function);
                x = Numbers.NEGATIVE_ONE;
            }
        } else {
            LOGGER.error("Unexpected character: {}, returning -1.", (char) this.ch);
            x = Numbers.NEGATIVE_ONE;
        }

        return x;
    }

    @Override
    public String toString() {
        return this.equationString;
    }
}
