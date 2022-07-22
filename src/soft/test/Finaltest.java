package soft.test;

import org.junit.Test;
import soft.service.CountObject;

/**
 * @author 2hu0
 */
public class Finaltest {
    /**
     * 测试反码
     * DEC0反码为00000000
     * DEC-25反码为10011000
     * DEC25反码为 01100100
     * BIN101反码 01010000
     * BIN-101反码10100000
     */
    @Test
    public void test1() {
        String x = "BIN101 2'SC";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println(calculate);
    }

    /**
     * 测试补码
     * BIN-101 = 1011.0000
     */
    @Test
    public void test2() {
        String x = "BIN-101 2'SC";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println(calculate);
    }

    /**
     * 测试移位
     * DEC1 @ BIN011 = "BIN1000";
     */
    @Test
    public void test3() {
        String x = "DEC-24 1'SC";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * 压力测试
     * 缺操作数
     */
    @Test
    public void testError() {
        String x = "( + HEX6 ) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * 缺操作符
     */
    @Test
    public void testError2() {
        String x = "( DEC6 - DEC9 ) DEC4 =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * 算个移位
     */
    @Test
    public void testError3() {
        String x = "DEC0 - ( DEC3 @ DEC9 ) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * (-1)是对的
     * ((-1))也是对的
     * 随便来几个括号 只要左等于右 都是对的
     */
    @Test
    public void testError4() {
        //DEC(9)
        String x ="BIN((((-1)))) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * HEX-5 +  ( HEX-6 ) = -B
     */
    @Test
    public void test5() {
        String x = "HEX-5 +  ( HEX-6 ) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * 只能是值求反码而不是式子
     */
    @Test
    public void test6() {
        String x = "(DEC3 + DEC4) 1'SC";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     *(DEC5 + DEC3)
     * 字符串两端是空格可以是任意个
     * 字符与字符之间的空格也可以是任意个
     */
    @Test
    public void test7() {
        String x = "   ( DEC5 + DEC3 ) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * DEC((-9)是可以的
     * DEC( - 9 )不可以
     */
    @Test
    public void testerror3() {
        String x = "DEC12 @ DEC1.0 =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * 格式错误
     * DECFF + DECFF
     */
    @Test
    public void testerror4() {
        String x = "DECFF + DECFF =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * shf 缺操作数
     * DEC0 - ( @ DEC9 ) =
     */
    @Test
    public void testerror5() {
        String x = "DEC0 - ( @ DEC9 ) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     * 数值测试
     */
    @Test
    public void testRes() {
        String x = "DEC1.2 + ( BIN101.1 * HEXB ) =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    /**
     *
     */
    @Test
    public void testRes2() {
        String x  = "DEC1.0 ^ DEC2.00000 =";
        CountObject.calculate(x);
        String calculate = CountObject.calculate(x);
        System.out.println("calculate = " + calculate);
    }

    @Test
    public void testUtil() {
    }

}
