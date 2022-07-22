package soft.test;


import org.junit.Test;
import soft.service.Expression;
import soft.service.Operand;
import soft.service.Work;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

/**
 * @author 2hu0
 */
public class TestFunc {
    /**
     * 这里先写一个通用的遍历表达式List集合的方法
     * 后续直接调用这个即可
     */
    public void printExpressionList(List<Object> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClass() == Operand.class) {
                System.out.print(((Operand)list.get(i)).getOperandData()+" ");
                continue;
            }
            System.out.print((String)list.get(i)+" ");
        }
        System.out.println();
    }
    @Test
    /**
     * 前台传来一个中缀 字符串
     * "DEC1 + DEC3 * (DEC4 + DEC5) = ": 我这里得到后缀表达式:
     * 1 3 4 5 + * + =
     */
    public void testSuffix() {
        String receiveString = "DEC1 + DEC3 * ( DEC4 + DEC5 ) =";
        //1 将表达式封装为表达式对象
        Expression expression = new Expression(receiveString);
        //去初步解析一下这个表达式 判断功能
        expression.addressExpression();
        printExpressionList(expression.getSuffixExpression());
        System.out.print("运算结果为");
        //System.out.println(expression.evaluateExpression());
    }


    /**input = "DEC10 1'SC"
     * res = 10
     *表达式的功能是  GET_COMPLEMENTAL_CODE
     * input = "BIN101 2'SC"
     * res = 101
     * 表达式的功能是  GET_ONES_COMPLEMENT_CODE
     */
    @Test
    public void testSuffix2() {
        String receiveString = "DEC10 1'SC";
        //String receiveString = "BIN101 2'SC";
        Expression expression = new Expression(receiveString);
        //去初步解析一下这个表达式 判断功能
        expression.addressExpression();
        System.out.println("表达式的功能是  " + expression.getWorkFunction());
        System.out.print("解析后的结果为  ");
        printExpressionList(expression.getSuffixExpression());
        System.out.println("运算结果为");
        //System.out.println(expression.evaluateExpression());

    }

    /**
     *Input  = "HEX7B + BIN2 * ( DEC5.1 + BIN101.1 ) "
     * res = 7B 2 5.1 101.1 + * +
     */
    @Test
    public void testSuffix3() {
        String receiveString = "HEX7B + BIN1 * ( DEC5.1 + BIN101.1 ) =";
        //1 将表达式封装为表达式对象
        Expression expression = new Expression(receiveString);
        //去初步解析一下这个表达式 ,判断功能
        expression.addressExpression();
        printExpressionList(expression.getSuffixExpression());
        System.out.println("需要返回的进制格式为 " + expression.getExpressionCurrentSystem());
        //System.out.println(expression.evaluateExpression());
    }

    @Test
    public void test() {
        String receive = "DEC1 @ DEC3";
        Expression expression = new Expression(receive);
        Work work = new Work();
        expression.addressExpression();
        //String s = expression.evaluateExpression();
        printExpressionList(expression.getSuffixExpression());
        //System.out.println("1 << 3 =  " + s);
    }

    /**具体流程
     *Input  = "HEX7B + BIN2 * ( DEC5.1 + BIN101.1 ) "
     * res = 7B 2 5.1 101.1 + * +
     */
    @Test
    public void testMain() {
        //1创建计算器对象
        Work work = new Work();
        //前台传来的表达式
        String receiveString = "HEX7B + BIN1 * ( DEC5.1 + BIN101.1 ) =";
        //2 将表达式封装为表达式对象
        Expression expression = new Expression(receiveString);
        //3 将表达式对象传入计算器类中的doWork方法
        work.doWork.accept(expression);
        //返回结果
        System.out.println("需要返回的进制格式为 " + expression.getExpressionCurrentSystem());
        System.out.println(work.getRes());

    }




}
