package soft.control;

import soft.Utils.MyCompareUtils;
import soft.view.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 点击事件类，为每一个JButton绑定点击事件，实现相应的操作
 */
public class Clicked extends MouseAdapter implements MouseMotionListener {
    //计算器对象
    private final Calculator calculator;
    //点击的按钮对象
    private final JButton button;
    //记录输入信息的数组
    private final List<String> res;
    //比较工具
    private final MyCompareUtils utils = new MyCompareUtils();

    public Clicked(Calculator calculator, JButton button, List<String> res){
        this.button = button;
        this.calculator = calculator;
        this.res = res;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //============================================添加按键约束=====================================
        //按到ac直接设置启用小数点和错误复位
        if(Objects.equals(button.getName(), "AC")){
            calculator.hasPoint = false;
            calculator.isError = false;
            calculator.isBinary = false;
        }
        //约束一：两种进制只能有一种存在
        if(utils.compare(button.getName(),"DEC")){
            calculator.system = true;
        }
        else if(utils.compare(button.getName(),"HEX") && !calculator.getButtons()[0].isEnabled()){
            calculator.system = false;
        }
        //需要立刻更新按钮的状态，否则每次切换按钮都会输入一个进制信息
        calculator.update();

        //约束二：右括号数量不能超过左括号
        if(utils.compare(button.getName(),"(")){
            calculator.setCount(calculator.getCount() + 1);
        }
        else if(utils.compare(button.getName(),")")){
            //java牛逼
            calculator.setCount(Math.max(calculator.getCount() - 1, 0));
        }

        //约束三：每一个数字只能有一个小数点
        //每次输入小数点后都禁用它
        if(utils.compare(button.getName(),".")){
            calculator.hasPoint = true;
        }
        //输入一个运算符代表要写入一个新的数，启用小数点
        if(utils.compare(button.getName(), "+", "-", "*", "/", "OR", "AND", "XOR", "SHF")){
            calculator.hasPoint = false;
        }

        //约束四：操作数不能以00000开头
        //输入0的时候，判断上一位输入，是操作符或者大小括号说明这个0是一个操作数的开头，此时禁用按钮0
        if(utils.compare(button.getName(), "0")
                && utils.compare(res.get(res.size() - 1), "+","-","*","/","OR","AND","SHF","XOR","(",")")){
            calculator.zerolegal = false;
        }
        //输入小数点或者操作符或者左括号或者0 或者输入了一个非0的操作数 的时候，说明这时候要开始一个新的操作数，或者开始写小数了，此时开启按钮0
        if(utils.compare(button.getName(), ".", "+","-","*","/","OR","AND","SHF","XOR","(",")")
                || (calculator.isNumber(button.getName()) && !utils.compare(button.getName(), "0"))){
            calculator.zerolegal = true;
        }

        //输入CE约束，每次输入CE时，要维护好小数点和0的禁用状态
        if(utils.compare(button.getName(),"CE")){

            //如果要删除的数是0，要启用按键0（属于约束四：0约束）
            if(utils.compare(res.get(res.size() - 1),"0")){
                calculator.zerolegal = true;
            }
            //如果要删除的是左右括号要维护好count
            if(utils.compare(res.get(res.size() - 1),"(")){
                calculator.setCount(calculator.getCount() - 1);
            }
            if(utils.compare(res.get(res.size() - 1),")")){
                calculator.setCount(calculator.getCount() + 1);
            }

            //如果数组不为空,查看最后一位（将要删除的）是什么来决定要不要启用小数点
            if(res.size() > 0){
                //记录最后一位是什么输入，这一位是等下要删除的
                String val = res.get(res.size() - 1);
                //如果是操作符，要判断整个操作符前面的是不是操作数
                if(utils.compare(button.getName(), "+", "-", "*", "/", "OR", "AND", "XOR", "SHF")){
                    //找到操作符前一位的输入
                    int index = res.size() - 2;
                    boolean hasPoint = false;
                    //往前找，遇到数字继续找看有没有小数点，遇到操作符说明前一位输入是一个数并且没有小数点，启用小数点；遇到小数点说明该数有小数点
                    while(index >= 0){
                        if(utils.compare(button.getName(), "+", "-", "*", "/", "OR", "AND", "XOR", "SHF")){
                            break;
                        }
                        if(utils.compare(res.get(index), ".")){
                            hasPoint = true;
                            break;
                        }
                        index--;
                    }
                    calculator.hasPoint = hasPoint;
                }
                //如果删除的是小数点，直接启用小数点；如果删除的是除了操作符和小数点之外的，不需要改动小数点禁用状态
                else if(utils.compare(val, ".")){
                    calculator.hasPoint = false;
                }
            }
            //数组是空的（好像没有可能），说明没有小数点，启用小数点
            //else calculator.hasPoint = false;
        }

        //==============================处理键入的值，生成计算表达式并显示============================

        //按键没有被禁用才能按
        if(button.isEnabled()){
            //获取按钮名称
            String val = button.getName();

            //==================================判断按键类型=============================

            //AC键清除全部内容
            if(utils.compare(val,"AC")){
                //设置初始状态，必须设置否则出错
                calculator.setOrigin();
                //设置答案显示字体
                calculator.setAnsFont(Font.BOLD,50);
            }
            //CE清除一位输入
            else if(utils.compare(val,"CE")){
                //如果该数不是结果只要清除一位即可
                if(!calculator.isResult){
                    String now = res.remove(res.size() - 1);
                    //如果数组是 [(] ，移除之后就变成了空数组，所以这里有必要判断数组不为空才要移除小数点前面的0或者操作数前面的进制信息
                    if(res.size() > 0){
                        //如果是 0. 的形式，小数点清除了要把前面的0也去除
                        if(utils.compare(now,".") && utils.compare(res.get(res.size() - 1),"0") && !calculator.isNumber(res.get(res.size() - 2))){
                            res.remove(res.size() - 1);
                        }
                        //数字清除了要抹去进制信息
                        if(utils.compare(res.get(res.size() - 1), "HEX","DEC")){
                            res.remove(res.size() - 1);
                        }
                    }
                }
                //如果该数是结果，清除全部
                else {
                    res.clear();
                }
                //如果把输入都删了，要重新设置初始状态，必须设置否则出错
                if(res.size() == 0){
                    calculator.setOrigin();
                }
            }
            //需要进行运算的按键计算表达式，（注意：在该else if分支下调用calExpression方法有可能会出现下标越界异常，这是正常的）
            else if(utils.compare(val,"=", "1'sc", "2'sc")){
                //先把符号加入表达式
                res.add(" ");
                res.add(val);

                //调试专用
                //System.out.println(calculator.calExpression(res,false,false));

                //把要计算的表达式显示在历史栏
                calculator.setHisText(calculator.calExpression(res,false,false));
                //如果太长了就去掉进制信息
               if(calculator.getHisTextSize() >= 440){
                   calculator.setHisText(calculator.calExpression(res,true,false));
               }
               //如果还是太长了，把运算符也换成短的运算符
                if(calculator.getHisTextSize() >= 440){
                    calculator.setHisText(calculator.calExpression(res,true,true));
                }
               //要是还是太长了，直接摆烂
               if(calculator.getHisTextSize() >= 440){
                    calculator.setHisText("显示不下了呜呜呜~");
               }

               //显示计算结果
                calculator.resultChange(calculator.calExpression(res,false,true));

//               //设置答案
//                res.clear();
//                Collections.addAll(res,"DEC","5","2","0");
//
//                //维护标志位
//                calculator.isResult = true;
//                calculator.hasPoint = false;
//                calculator.setCount(0);
//                calculator.setAnsFont(Font.BOLD,50);
            }
            //如果是运算符，需要排除重叠现象
            else if(utils.compare(val, "+", "-", "*", "/", "OR", "AND", "XOR", "SHF")){
                //如果前面的是计算的结果，输入运算符可以实现累加运算，同时下一个输出就不是结果了
                if(calculator.isResult){
                    //要把结果标记符设置false，否则下次输入操作数会直接清零
                    calculator.isResult = false;
                    //实现在表达式第一位输入符号
                    if(Objects.equals(val, "-") && res.size() == 2 && Objects.equals(res.get(res.size() - 1), "0")){
                        res.clear();
                        if(calculator.system){
                            Collections.addAll(res, "DEC","-");
                        }else {
                            Collections.addAll(res, "HEX","-");
                        }
                    }
                }
                //先记录上一个输入
                String pre = null;
                if(res.size() != 0){
                    pre = res.get(res.size() - 1);
                }
                //如果上一个输入也是运算符的话，去除上一个的运算符
                if(utils.compare(pre, "+", "-", "*", "/", "OR", "AND", "XOR", "SHF")){
                    res.remove(res.size()-1);
                }

                //负数只可能出现在两个位置，开头，左括号右边，开头的情况前面已经解决，这两种情况都说明负号是一个操作数的开始，要在前面加上进制
                if(Objects.equals(pre, "(") && Objects.equals(val, "-")){
                    if(calculator.getButtons()[0].isEnabled()){
                        res.add(calculator.getButtons()[1].getName());
                    }else{
                        res.add(calculator.getButtons()[0].getName());
                    }
                }

                //把现在输入的运算符输入进去（这是最正常的情况）
                res.add(val);
            }
            //括号不用管
            else if(Objects.equals(val, "(") || Objects.equals(val, ")")){
                //如果前面是运算结果，输入括号要把前面的结果清除，添加历史记录再加括号
                if(calculator.isResult){
                    calculator.setHisText("Ans = " + calculator.getAns().getText());
                    res.clear();
                    calculator.isResult = false;
                }
                res.add(val);
            }
            //数字或者字母或者小数点要去判断前面有没有进制信息
            else {
                //如果前面是运算结果，输入操作数要把前面的结果清除，添加历史记录
                if(calculator.isResult){
                    calculator.getHis().setText("Ans = " + calculator.getAns().getText());
                    res.clear();
                    calculator.isResult = false;
                }
                //记录上面一位输入
                String pre = null;
                if(res.size() != 0){
                    pre = res.get(res.size() - 1);
                }
                //如果前面是 - ，有可能是负号，也有可能是减号
                if(Objects.equals(pre, "-")){
                    if(!utils.compare(res.get(res.size() - 2),"HEX","DEC","(")){
                        //说明是减号，要加进制信息
                        if(calculator.getButtons()[0].isEnabled()){
                            res.add(calculator.getButtons()[1].getName());
                        }else{
                            res.add(calculator.getButtons()[0].getName());
                        }
                    }
                    //如果按的是 "." 就需要在前面补0，负号或者减号都要补0
                    if(Objects.equals(button.getName(), ".")){
                        res.add("0");
                    }
                    //说明是负号
                    res.add(val);
                }
                //如果按的是数字或字母，并且上一个是0，并且上上个是进制信息或者是负号，说明0是没用的，要去除，避免了 DEC012 这样的情况
                else if(res.size() > 1 && Objects.equals(pre, "0") && !Objects.equals(val, ".")
                        && utils.compare(res.get(res.size()-2), "DEC", "HEX", "-")){
                    res.remove(res.size()-1);
                    res.add(val);
                }
                //如果按的是小数点，或者是 数字或字母但是前面没有0
                else{
                    //如果上一位是操作数，直接添加该操作数
                    if(pre != null && pre.length() == 1 && (calculator.isNumber(pre) || utils.compare(pre, "."))){
                        res.add(val);
                    }
                    //但是如果前面是其他东西，说明该输入是整个操作数的第一位，所以前面要加进制信息
                    else{
                        if(calculator.getButtons()[0].isEnabled()){
                            res.add(calculator.getButtons()[1].getName());
                        }else{
                            res.add(calculator.getButtons()[0].getName());
                        }
                        //如果按的是操作数的第一位，并且按的是 "." 就需要在前面补0
                        if(Objects.equals(button.getName(), ".")){
                            res.add("0");
                        }
                        res.add(val);
                    }
                }
            }
        }


        //   ==================测试单元===============
        //  |                                       |
        //  |    如果程序出现了意向不到的异常,不要惊慌,    |
        //  |    只要打开下面的注释,通过调试观察控制台     |
        //  |    res的值,就能很快发现是哪里出现了bug      |
        //  |                                       |
        //   ========================================

        //System.out.println(res);
        //不建议在表达式不完整的时候使用displayExpression函数观察输出，因为很有可能会数组下标越界
        //System.out.println("========================");

        //更新按钮
        calculator.update();
        //先显示内容
        calculator.setAnsText(calculator.displayExpression(res));
        //根据内容大小调整字号
        for(int i =0; i < 3;i++){
            if(calculator.getAnsTextSize() >= 480){
                int size = Math.max(calculator.getAnsFontSize() - 10, 20);
                calculator.setAnsFont(size == 50 ? Font.BOLD : Font.PLAIN,size);
            }
            else if(calculator.getAnsTextSize() <= 350){
                int size = Math.min(calculator.getAnsFontSize() + 10, 50);
                calculator.setAnsFont(size == 50 ? Font.BOLD : Font.PLAIN,size);
                //如果放大之后溢出了，就再次缩小
                if(calculator.getAnsTextSize() >= 480){
                    //这里必然是缩小 所以字体一定是Font.PLAIN
                    calculator.setAnsFont(Font.PLAIN,size - 10);
                }
            }
        }
        //如果字号调整最小之后还是溢出显示 错误
        if(calculator.getAnsFontSize() == 20 && calculator.getAnsTextSize() >= 480){
            //设置初始字体
            calculator.setAnsFont(Font.PLAIN,50);
            //清除所有输入
            res.clear();
            //设置结果位，并且初始化结果信息(报错信息)
            calculator.isResult = true;
            Collections.addAll(res,"DEC","Overflow error!");
            //重新显示内容
            calculator.setAnsText(calculator.displayExpression(res));
            //设置全局错误
            calculator.isError = true;
            //更新键盘(全局错误后要更新键盘)
            calculator.update();
        }
    }

}

