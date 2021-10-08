package service;

import tool.Fraction;
import tool.Tools;

import java.io.*;
import java.util.Scanner;

/**
 * 显示界面
 * 能够自动生成四则运算练习题
 * 可以定制题目数量
 * 用户设置最大数
 * 是否有括号
 * 输出方式  输出到文件
 */
public class Gui {
    public static void main(String[] args) {
        int selection; // 选择生成题目或核对答案
        selection = firstMenu();
        if (selection == 1) {
            secondMenu1();
        } else if (selection == 2) {
            secondMenu2();
        }
    }

    /**
     * 第一级菜单
     */
    private static int firstMenu() {
        System.out.println("========================================================");
        System.out.println("||****************************************************||");
        System.out.println("                  欢迎使用四则运算程序");
        System.out.println("                     请根据提示输入");
        System.out.println("||****************************************************||");
        System.out.println("========================================================");
        System.out.println("             请选择：1.生成题目   2.核对答案");
        int selection;
        while (true) {
            try {
                selection = Tools.inputToInt();
                System.out.println("========================================================");
                if (selection == 1 || selection == 2) {
                    break;
                } else {
                    System.out.print("输入错误！请重新输入：");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("输入错误！请重新输入：");
            }
        }
        return selection;
    }

    /**
     * 生成题目菜单
     */
    private static void secondMenu1() {
        int questionNumber;   //题目数量
        int naturalNumberMax; //自然数最大值
        int denominatorMax;   //分母最大值
        boolean bracket;      //是否含有括号,true表示有括号，false表示没有括号

        System.out.print("请输入生成题目数量：");
        while (true) {
            try {
                questionNumber = Tools.inputToInt();
                System.out.println("========================================================");
                if (questionNumber > 0) {
                    break;
                } else {
                    System.out.print("输入错误！请重新输入：");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("输入错误！请重新输入：");
            }
        }
        System.out.print("请输入自然数的最大值 [0,x),（输入x必须为正数）：");
        while (true) {
            try {
                naturalNumberMax = Tools.inputToInt();
                System.out.println("========================================================");
                if (naturalNumberMax > 0) {
                    break;
                } else {
                    System.out.print("输入错误！请重新输入：");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("输入错误！请重新输入：");
            }
        }
        System.out.print("请输入分母的最大值 0 - x,（输入x必须为正数）：");
        while (true) {
            try {
                denominatorMax = Tools.inputToInt();
                System.out.println("========================================================");
                if (denominatorMax > 0) {
                    break;
                } else {
                    System.out.print("输入错误！请重新输入：");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("输入错误！请重新输入：");
            }
        }
        System.out.print("请选择是否有括号 Y/N：");
        String string;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            string = scanner.next();
            System.out.println("========================================================");
            Tools.removeSpaces(string);
            if ("Y".equals(string) || "y".equals(string)) {
                bracket = true;
                break;
            } else if ("N".equals(string) || "n".equals(string)) {
                bracket = false;
                break;
            } else {
                System.out.print("输入错误！请重新输入：");
            }
        }

        // 生成四则运算表达式，并打印
        Tools.spawnArithmetic(questionNumber, naturalNumberMax, denominatorMax, bracket);

        System.out.println("生成题目完成");
    }

    /**
     * 核对答案菜单
     */
    private static void secondMenu2() {

        //创建文件输入流
        String arithmeticFileName = "./Exercises.txt";
        String resultFileName = "./Answers.txt";
        File arithmeticFile = new File(arithmeticFileName);
        File resultFile = new File(resultFileName);
        BufferedReader arithmeticBufferedReader;
        BufferedReader resultBufferedReader;
        try {
            arithmeticFile.createNewFile();
            resultFile.createNewFile();
            arithmeticBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(arithmeticFile)));
            resultBufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(resultFile)));
        } catch (IOException e) {
            System.out.println("请检查文件！");
            return;
        }

        // 创建文件输出流
        String gradeFileName = "./Grade.txt";
        File gradeFile = new File(gradeFileName);
        BufferedWriter gradeBufferedWriter;
        try {
            gradeFile.createNewFile();
            gradeBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(gradeFile)));
        } catch (IOException e) {
            System.out.println("请检查文件！");
            return;
        }

        // 核对答案
        checkAnswer(arithmeticBufferedReader, resultBufferedReader, gradeBufferedWriter);

        System.out.println("核对完成！");

        // 关闭流
        try {
            arithmeticBufferedReader.close();
            resultBufferedReader.close();
            gradeBufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 核对答案
     */
    private static void checkAnswer(BufferedReader arithmeticBufferedReader,
                                    BufferedReader resultBufferedReader,
                                    BufferedWriter gradeBufferedWriter) {
        String correctStr = "(";
        String wrongStr = "(";
        // int repeat = 0;
        // StringBuilder stringBuffer = new StringBuilder();
        String str1;
        String[] arithmeticStr;
        String str2;
        String[] resultStr;
        try {
            // 读取题目和答案
            while ((str1 = arithmeticBufferedReader.readLine()) != null &&
                    (str2 = resultBufferedReader.readLine()) != null) {
                arithmeticStr = str1.split(". ");
                resultStr = str2.split(". ");

                String index = arithmeticStr[0] + ",";
                //String arithmetic = arithmeticStr[1].substring(0, arithmeticStr[1].length() - 2);
                // if (Database.hashSet.contains(arithmetic)) {
                //     repeat++;
                //     stringBuffer.append(repeat + arithmetic + "\n");
                // }

                // 得到题目的正确答案
                Fraction result = Tools.getResult(arithmeticStr[1]);

                // 与给定的答案文件进行比较
                if (resultStr[1].equals(result.toString())) {
                    correctStr += index;
                } else {
                    wrongStr += index;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] split;
        correctStr = correctStr.substring(0, correctStr.length() - 1);
        split = correctStr.split(",");
        correctStr = "Correct:" + split.length + correctStr + ")";
        wrongStr = wrongStr.substring(0, wrongStr.length() - 1);
        split = wrongStr.split(",");
        wrongStr = "Wrong:" + split.length + wrongStr + ")";


        try {
            gradeBufferedWriter.write(correctStr);
            gradeBufferedWriter.newLine();
            gradeBufferedWriter.write(wrongStr);
            gradeBufferedWriter.newLine();
            // gradeBufferedWriter.write("Repeat:" + repeat);
            // if (repeat != 0) {
            //     gradeBufferedWriter.newLine();
            //     gradeBufferedWriter.write(stringBuffer.toString());
            // }
            gradeBufferedWriter.flush();
            gradeBufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}