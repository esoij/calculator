package esoij.calculator;

import esoij.logging.LogUtils;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Scanner;

public class Calculator {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Scanner SCANNER = new Scanner(System.in);
    public Equation equation;

    public Calculator() {
        while (true) {
            LOGGER.info("Input equation. Input \"n\" to quit.");
            String equation = SCANNER.nextLine();
            if (Objects.equals(equation, "n")) break;
            this.equation = new Equation(equation);
            LOGGER.info("equation: {}", this.equation);
            LOGGER.info("answer:   {}", this.equation.solve());
        }

        //keep this at the end
        LOGGER.info("Press any key to quit the application.");
        SCANNER.nextLine();
    }
}
