package tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * 表示一个四则运算表达式
 * 判断两个四则运算表达式是否重复
 * 打印四则运算表达式
 */
public class Arithmetic implements BracketType {
    public ArrayList<Fraction> operands = new ArrayList<>();
    int[] operators = new int[3]; //1表示 +   2表示 -   3表示 *   4表示 /
    int bracketType;
    public Fraction result = new Fraction();


    /**
     * 将四则运算表达式转换成字符串形式
     */
    @Override
    public String toString() {
        String string = "";
        switch (bracketType) {
            case nullType_zero:
                string = getArithmetic(0, -1, -1, -1, -1);
                break;
            case firstType_one:
                string = getArithmetic(1, -1, -1, 1, -1);
                break;
            case secondType_one:
                string = getArithmetic(0, 1, -1, 2, -1);
                break;
            case thirdType_one:
                string = getArithmetic(0, 2, -1, 3, -1);
                break;
            case forthType_one:
                string = getArithmetic(1, -1, -1, 2, -1);
                break;
            case fifthType_one:
                string = getArithmetic(0, 1, -1, 3, -1);
                break;
            case firstType_two:
                string = getArithmetic(1, 2, -1, 1, 3);
                break;
            case secondType_two:
                string = getArithmetic(2, -1, -1, 1, 2);
                break;
            case thirdType_two:
                string = getArithmetic(1, 1, -1, 2, 2);
                break;
            case forthType_two:
                string = getArithmetic(0, 1, 1, 2, 3);
                break;
            case fifthType_two:
                string = getArithmetic(0, 1, 2, 3, 3);
                break;
            default:
                break;
        }
        return string;
    }

    /**
     * 在四则运算表达式的字符串形式特定位置添加括号
     *
     * @param i1   在开头存在几个左括号
     * @param i2_1 在生成第几个运算符时添加第一个左括号
     * @param i2_2 在生成第几个运算符时添加第二个左括号
     * @param i3_1 在生成第几个运算符时添加一个右括号
     * @param i3_2 在生成第几个运算符时添加二个右括号
     */
    private String getArithmetic(int i1, int i2_1, int i2_2, int i3_1, int i3_2) {
        String string = "";
        switch (i1) {
            case 0:
                break;
            case 1:
                string = "(";
                break;
            case 2:
                string = "((";
                break;
            default:
                break;
        }
        string += operands.get(0).toString();
        for (int i = 1; i < operands.size(); i++) {
            string += judgeOperatorType(operators[i - 1]);
            if (i == i2_1) {
                string += "(";
            }
            if (i == i2_2) {
                string += "(";
            }
            string += operands.get(i).toString();
            if (i == i3_1) {
                string += ")";
            }
            if (i == i3_2) {
                string += ")";
            }
        }
        string += "=";
        return string;
    }

    /**
     * 将运算符从int类型转为String类型
     */
    private String judgeOperatorType(int operator) {
        String str = null;
        switch (operator) {
            case 1:
                str = "+";
                break;
            case 2:
                str = "-";
                break;
            case 3:
                str = "*";
                break;
            case 4:
                str = "/";
                break;
            default:
                break;
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arithmetic that = (Arithmetic) o;
        return bracketType == that.bracketType && Objects.equals(operands, that.operands) && Arrays.equals(operators, that.operators);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(operands, bracketType);
        result = 31 * result + Arrays.hashCode(operators);
        return result;
    }
}
