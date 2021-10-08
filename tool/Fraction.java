package tool;

import java.util.Objects;

/**
 * 表示分数
 */
public class Fraction {
    public int naturalNumber;   // 自然数
    int numerator = 0;   // 分子
    public int denominator = 1; // 分母

    // public String toString1() {
    //     String string = null;
    //     if (numerator == 0) {
    //         if (naturalNumber == 0) {
    //             string = "0";
    //         } else {
    //             string = naturalNumber + "";
    //         }
    //     } else if (numerator != 0) {
    //         if (naturalNumber == 0) {
    //             string = numerator + "/" + denominator;
    //         } else {
    //             string = naturalNumber + "(" +
    //                     numerator + "/" +
    //                     denominator + ")";
    //         }
    //     }
    //     return string;
    // }

    @Override
    public String toString() {
        String string;

        // 是自然数还是假分数
        if (numerator == 0) {
            if (naturalNumber == 0) {
                string = "0";
            } else {
                string = naturalNumber + "";
            }
        } else {
            string = naturalNumber + "(" +
                    numerator + "/" +
                    denominator + ")";
        }
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return naturalNumber == fraction.naturalNumber && numerator == fraction.numerator && denominator == fraction.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(naturalNumber, numerator, denominator);
    }
}
