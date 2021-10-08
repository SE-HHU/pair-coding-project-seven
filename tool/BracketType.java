package tool;

/**
 * 表示括号类型
 */
public interface BracketType {
    int nullType_zero = 0;  //a+b+c+d
    int firstType_one = 1;  //(a+b)+c+d
    int secondType_one = 2; //a+(b+c)+d
    int thirdType_one = 3;  //a+b+(c+d)
    int forthType_one = 4;  //(a+b+c)+d
    int fifthType_one = 5;  //a+(b+c+d)
    int firstType_two = 6;  //(a+b)+(c+d)
    int secondType_two = 7; //((a+b)+c)+d
    int thirdType_two = 8;  //(a+(b+c))+d
    int forthType_two = 9;  //a+((b+c)+d)
    int fifthType_two = 10; //a+(b+(c+d))
}
