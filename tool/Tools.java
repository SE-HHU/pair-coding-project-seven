package tool;

import service.Database;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;

/**
 * 工具类
 * 生成四则运算表达式，并打印
 */
public class Tools {
    /**
     * 生成四则运算表达式，并打印
     */
    public static void spawnArithmetic(int questionNumber, int naturalNumberMax,
                                       int denominatorMax, boolean bracket) {
        //创建文件输出流
        String arithmeticFileName = "./Exercises.txt";
        String resultFileName = "./Answers.txt";
        File arithmeticFile = new File(arithmeticFileName);
        File resultFile = new File(resultFileName);
        BufferedWriter arithmeticBufferedWriter;
        BufferedWriter resultBufferedWriter;
        try {
            arithmeticFile.createNewFile();
            resultFile.createNewFile();
            arithmeticBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(arithmeticFile)));
            resultBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(resultFile)));
        } catch (IOException e) {
            System.out.println("请检查文件！");
            return;
        }

        for (int i = 0; i < questionNumber; i++) { // 生成一道题目
            Arithmetic arithmetic = new Arithmetic();
            int operatorNumber = (int) (Math.random() * 3 + 1); //随机生成运算符数量

            //根据运算符数量以及是否存在括号生成括号类型
            if (operatorNumber == 1 || !bracket) {
                arithmetic.bracketType = BracketType.nullType_zero;
            } else if (operatorNumber == 2) {
                arithmetic.bracketType = (int) (Math.random() * 2 + 1);
            } else if (operatorNumber == 3) {
                arithmetic.bracketType = (int) (Math.random() * 10 + 1);
            }

            //生成含有指定数量运算符的四则运算表达式(不包含最后一个操作数)
            for (int j = 0; j < operatorNumber; j++) {
                spawnOperand(naturalNumberMax, denominatorMax, arithmetic); //生成操作数
                arithmetic.operators[j] = (int) (Math.random() * 4 + 1); //生成运算符
            }
            spawnOperand(naturalNumberMax, denominatorMax, arithmetic); //生成最后一个操作数

            arithmetic.result = Tools.getResult(arithmetic.toString()); //得到答案

            // 如果重复就跳过，否则打印
            if (Database.hashSet.contains(arithmetic)) {
                continue;
            }
            Database.hashSet.add(arithmetic);
            int index = Database.hashSet.size();
            String arithmeticString = index + ". " + arithmetic;
            System.out.println(arithmeticString);
            String resultString = index + ". " + arithmetic.result;
            try {
                arithmeticBufferedWriter.write(arithmeticString);
                arithmeticBufferedWriter.newLine();
                arithmeticBufferedWriter.flush();
                resultBufferedWriter.write(resultString);
                resultBufferedWriter.newLine();
                resultBufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 关闭字符输出流
        try {
            arithmeticBufferedWriter.close();
            resultBufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算四则运算表达式的值
     */
    public static Fraction getResult(String arithmeticStr) {
        Fraction result;
        String postfix = infixToPostfix(arithmeticStr); // 利用栈，中缀表达式转到后缀表达式
        result = calculatePostfix(postfix); // 计算后缀表达式的值并赋值给result
        return result;
    }

    /**
     * 中缀表达式转到后缀表达式
      */
    private static String infixToPostfix(String infixExpression) {
        Stack<String> stack = new Stack<>();
        String postfixExpression = ""; //存储后缀表达式
        char temp; // 存储中缀表达式的每个字符
        boolean isFraction = false; //是否遇见分数
        boolean isBracket = false;
        for (int i = 0; i < infixExpression.length(); i++) {
            temp = infixExpression.charAt(i);
            switch (temp) {
                case '+':
                case '-':
                    //当栈不为空以及栈顶元素不是左括号时,直接出栈,因此此时只有可能是*/+-四种运算符(根据规则4),否则入栈
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        postfixExpression += stack.pop() + " ";
                    }
                    //入栈
                    stack.push(temp + "");
                    break;
                case '/':
                    if (isFraction) { //遇到分数的分子分母
                        postfixExpression += temp;
                        break;
                    }
                case '*':
                    //遇到运算符 * 或 /
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
                        postfixExpression += stack.pop() + " ";
                    }
                    stack.push(temp + "");
                    break;
                case '(':
                    //遇到左括号
                    if (i != 0 && infixExpression.charAt(i - 1) >= '0' &&
                            infixExpression.charAt(i - 1) <= '9') {
                        //遇到分数的分子分母
                        isFraction = true;
                        postfixExpression += temp;
                    } else {
                        stack.push(temp + ""); //没有遇到分数的分子分母
                    }
                    break;
                case ')':
                    //遇到右括号
                    if (isFraction) {
                        postfixExpression += temp + " ";
                        isFraction = false;
                    } else {
                        String out = stack.pop();
                        if ("(".equals(out)) {
                            isBracket = true;
                        }
                        while (out != null && !isBracket) {
                            postfixExpression += out + " ";
                            out = stack.pop();
                            if ("(".equals(out)) {
                                isBracket = true;
                            }
                        }
                        isBracket = false;
                    }
                    break;
                default:
                    if (temp == '=') {
                        break;
                    }
                    //操作数
                    while (temp >= '0' && temp <= '9') {
                        postfixExpression += temp;
                        i++;
                        temp = infixExpression.charAt(i);
                    }
                    if (temp != '(' && !isFraction) {
                        postfixExpression += " "; // 分隔符
                    }
                    i--;
                    break;
            }
        }
        while (!stack.isEmpty()) {
            postfixExpression += stack.pop() + " ";
        }
        postfixExpression += "=";
        return postfixExpression;
    }

    /**
     * 计算后缀表达式的值
      */
    private static Fraction calculatePostfix(String postfixExpression) {
        Fraction result = new Fraction(); //存储结果
        String tempInt; //遇见数字临时存储
        Stack<Fraction> stack = new Stack<>();
        char temp; //存储后缀表达式的每个字符
        int isFraction = 0; // 1表示正在对分数的分子部分进行操作
        // 2表示正在对分数的分母部分进行操作
        Fraction temp1; // 进行运算的临时数字1
        Fraction temp2; // 进行运算的临时数字1
        for (int i = 0; i < postfixExpression.length(); i++) {
            temp = postfixExpression.charAt(i);
            switch (temp) {
                case '+':
                    temp2 = stack.pop();
                    temp1 = stack.pop();
                    stack.push(Tools.fractionAdd(temp1, temp2));
                    break;
                case '-':
                    temp2 = stack.pop();
                    temp1 = stack.pop();
                    stack.push(Tools.fractionSubtract(temp1, temp2));
                    break;
                case '/':
                    if (isFraction != 2) {
                        temp2 = stack.pop();
                        temp1 = stack.pop();
                        stack.push(Tools.fractionDivide(temp1, temp2));
                    }
                    break;
                case '*':
                    temp2 = stack.pop();
                    temp1 = stack.pop();
                    stack.push(Tools.fractionMultiply(temp1, temp2));
                    break;
                case '(':
                    //遇到左括号,即遇到分数的分子分母
                    isFraction = 1;
                    break;
                case ')':
                    //遇到右括号
                    break;
                default:
                    if (temp < '0' || temp > '9') {
                        break;
                    }
                    tempInt = "";
                    while (temp >= '0' && temp <= '9') {
                        tempInt += temp;
                        i++;
                        temp = postfixExpression.charAt(i);
                    }
                    if (isFraction == 0) {
                        result.naturalNumber = Integer.parseInt(tempInt);
                    } else if (isFraction == 1) {
                        isFraction = 2;
                        result.numerator = Integer.parseInt(tempInt);
                    } else if (isFraction == 2) {
                        isFraction = 0;
                        result.denominator = Integer.parseInt(tempInt);
                    }
                    if (temp != '(' && isFraction != 1 && isFraction != 2) {
                        stack.push(result);
                        result = new Fraction();
                    }
                    i--;
                    break;
            }
        }
        result = stack.pop();
        return result;
    }

    /**
     * 将输入转换成int类型
      */
    public static int inputToInt() {
        Scanner scanner = new Scanner(System.in);
        String string;
        string = scanner.next();
        string = Tools.removeSpaces(string);
        return Integer.parseInt(string);
    }

    /**
     * 去除字符串中的空格
     */
    public static String removeSpaces(String string) {
        String str = "";
        char temp;
        for (int i = 0; i < string.length(); i++) {
            temp = string.charAt(i);
            if (temp != ' ') {
                str += "" + temp;
            }
        }
        return str;
    }

    /**
     * 生成操作数
      */
    private static void spawnOperand(int naturalNumberMax, int denominatorMax,
                                     Arithmetic arithmetic) {
        Fraction fraction = new Fraction();
        fraction.naturalNumber = (int) (Math.random() * naturalNumberMax);
        switch ((int) (Math.random() * 2 + 1)) { // 生成小数或自然数
            case 1:
                fraction.denominator = (int) (Math.random() * denominatorMax + 1);
                fraction.numerator = (int) (Math.random() * fraction.denominator);
                break;
            case 2:
                break;
        }
        arithmetic.operands.add(fraction);
    }

    /**
     * 两个分数相加
      */
    public static Fraction fractionAdd(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        fractionT(fraction1, fraction2);
        fraction.denominator = fraction1.denominator;
        fraction.numerator = fraction1.numerator + fraction2.numerator;
        fractionY(fraction);
        return fraction;
    }

    /**
     * 两个分数相减
      */
    public static Fraction fractionSubtract(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        fractionT(fraction1, fraction2);
        fraction.denominator = fraction1.denominator;
        fraction.numerator = fraction1.numerator - fraction2.numerator;
        fractionY(fraction);
        return fraction;
    }

    /**
     * 两个分数相乘
      */
    public static Fraction fractionMultiply(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        fractionT(fraction1, fraction2);
        fraction.denominator = fraction1.denominator * fraction2.denominator;
        fraction.numerator = fraction1.numerator * fraction2.numerator;
        fractionY(fraction);
        return fraction;
    }

    /**
     * 两个分数相除
      */
    public static Fraction fractionDivide(Fraction fraction1, Fraction fraction2) {
        Fraction fraction = new Fraction();
        fractionT(fraction1, fraction2);
        fraction.numerator = fraction1.numerator * fraction2.denominator;
        fraction.denominator = fraction1.denominator * fraction2.numerator;
        fractionY(fraction);
        return fraction;
    }

    /**
     * 两个分数通分
      */
    private static void fractionT(Fraction fraction1, Fraction fraction2) {
        int lcm = getLCM(fraction1.denominator, fraction2.denominator);
        fraction1.numerator = lcm / fraction1.denominator * fraction1.numerator + lcm * fraction1.naturalNumber;
        fraction2.numerator = lcm / fraction2.denominator * fraction2.numerator + lcm * fraction2.naturalNumber;
        fraction1.denominator = fraction2.denominator = lcm;
        fraction1.naturalNumber = fraction2.naturalNumber = 0;
    }

    /**
     * 分数约分
      */
    private static void fractionY(Fraction fraction) {
        // 分子为0
        if (fraction.numerator == 0) {
            fraction.denominator = 1;
            return;
        }
        int gcd;
        if (fraction.numerator < 0 && fraction.denominator < 0) {
            fraction.numerator *= -1;
            fraction.denominator *= -1;
        }
        if (fraction.numerator < 0) {
            gcd = getGCD(-fraction.numerator, fraction.denominator);
        } else if (fraction.denominator < 0) {
            gcd = getGCD(fraction.numerator, -fraction.denominator);
        } else {
            gcd = getGCD(fraction.numerator, fraction.denominator);
        }
        fraction.numerator /= gcd;
        fraction.denominator /= gcd;
        fraction.naturalNumber += fraction.numerator / fraction.denominator;
        if (fraction.numerator < 0 || fraction.denominator < 0) {
            fraction.numerator %= -fraction.denominator;
        } else {
            fraction.numerator %= fraction.denominator;
        }
    }

    /**
     * 求两个数最大公约数
      */
    private static int getGCD(int num1, int num2) {
        int gcd;

        // 更相减损法求最大公约数
        int temp;
        while (true) {
            if (num1 == num2) {
                gcd = num1;
                break;
            }
            if (num1 < num2) {
                temp = num2;
                num2 = num1;
                num1 = temp;
            }
            num1 -= num2;
        }

        return gcd;
    }

    /**
     * 求两个数最小公倍数
      */
    private static int getLCM(int num1, int num2) {
        // 最大公约数辅助法求最小公倍数
        int lcm = num1 * num2 / getGCD(num1, num2);

        return lcm;
    }

    // /**
    //  * String类型转为Arithmetic类型
    //  */
    // public static Arithmetic stringToArithmetic(String string) {
    //
    // }

    /**
     * 判断分母是否为0
     */
    private boolean denominatorIsZero(Fraction fraction1, Fraction fraction2) {
        if (fraction1.denominator != 0 && fraction2.denominator != 0) {
            return false;
        }
        return true;
    }
}
