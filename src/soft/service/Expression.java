package soft.service;


import soft.Utils.ExpressionStatus;
import soft.Utils.StringUtils;
import soft.Utils.SystemEnum;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 对传来的运算表达式进行封装处理
 *
 * @author 2hu0
 */
public class Expression {
    /**
     * 操作栈
     */
    private final Deque<String> operatorStack = new LinkedList<>();
    /**
     * 界面传来的String运算表达式
     * （中缀）
     */
    private String expression;
    /**
     * 存放后缀表达式(用于计算带运算符号的使用)
     */
    private final List<Object> suffixExpression = new ArrayList<>();

    /**
     * 运算求值的时候 表达式需要呈现的一个进制情况
     * 默认十进制
     */
    private SystemEnum expressionCurrentSystem = SystemEnum.DEC;
    /**
     * 表达式的指令功能
     */
    private WorkFunction workFunction;
    /**
     * 是否保留原表达式
     */
    private boolean isKeep = false;

    public boolean isKeep() {
        return isKeep;
    }

    public Expression(String expression) {
        this.expression = expression;
    }

    public SystemEnum getExpressionCurrentSystem() {
        return expressionCurrentSystem;
    }


    public List<Object> getSuffixExpression() {
        return suffixExpression;
    }


    public WorkFunction getWorkFunction() {
        return workFunction;
    }

    public void setExpressionCurrentSystem(SystemEnum expressionCurrentSystem) {
        this.expressionCurrentSystem = expressionCurrentSystem;
    }

    public Expression(String expression, SystemEnum expressionCurrentSystem) {
        this.expression = expression;
        this.expressionCurrentSystem = expressionCurrentSystem;
    }

    /**
     * 将String类型的表达式 转换为对象 按照下标存放集合中
     * 如 1 + 123 -3.5 = List["(operand)1","+","(operand)123","-","(operand3.5)"]
     */
    public List<Object> getInfixExpressionList() {
        ArrayList<Object> res = new ArrayList<>();
        //去掉首尾空格
        expression = expression.trim();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(this.expression.split("\\s+")));
        int i = 0;
        while (i < list.size()) {
            if (list.get(i).startsWith("DEC") || list.get(i).startsWith("HEX") || list.get(i).startsWith("BIN")) {
                Operand.getOperand(list.get(i));
                res.add(Operand.getOperand(list.get(i)));
                i++;
                continue;
            }
            res.add(list.get(i));
            i++;
        }
        return res;
    }

    /**
     * 判断是什么类型的表达式
     * 这里直接接受客户串来的原始表达式
     * 确定指令类型 以及需要返回的格式
     */
    public String addressExpression() {
        String status = isLegalExpression();
        if (status.equals(ExpressionStatus.FORMAT_ERROR)) {
            return ExpressionStatus.FORMAT_ERROR;
        }
        if (expression.endsWith(WorkFunction.EVALUATE_EXPRESSION.val)) {
            this.workFunction = WorkFunction.EVALUATE_EXPRESSION;
            //去掉 =
            expression = expression.substring(0, expression.length() - 2);
        } else if (expression.endsWith(WorkFunction.GET_ONES_COMPLEMENT_CODE.val)) {
            //不合法就返回
            if (ExpressionStatus.FORMAT_ERROR.equals(judgeIsLegal(expression))) {
                return ExpressionStatus.FORMAT_ERROR;
            }
            this.workFunction = WorkFunction.GET_ONES_COMPLEMENT_CODE;
            //去掉 1'SC
            expression = expression.substring(0, expression.length() - WorkFunction.GET_ONES_COMPLEMENT_CODE.val.length() - 1);
        } else if (expression.endsWith(WorkFunction.GET_COMPLEMENTAL_CODE.val)) {
            //不合法就返回
            if (ExpressionStatus.FORMAT_ERROR.equals(judgeIsLegal(expression))) {
                return ExpressionStatus.FORMAT_ERROR;
            }
            this.workFunction = WorkFunction.GET_COMPLEMENTAL_CODE;
            //去掉2'SC
            expression = expression.substring(0, expression.length() - WorkFunction.GET_COMPLEMENTAL_CODE.val.length() - 1);
        }
        //创建后缀表达式
        this.createSuffixExpression();
        return ExpressionStatus.CORRECT;
    }

    /**
     * 逆波兰表达式的转换
     * 中缀转后缀
     * 如4 * 5 - 8 + 60 + 8 / 2 => 4 5 * 8 - 60 + 8 2 / +
     * TODO: 2022/5/27 后期改一下返回类型
     */

    public void createSuffixExpression() {
        List<Object> infixExpressionList = this.getInfixExpressionList();
        for (Object item : infixExpressionList) {
            if (item.getClass().equals(Operand.class)) {
                suffixExpression.add(item);
            } else if ("(".equals(item)) {
                operatorStack.push("(");
            } else if (")".equals(item)) {
                //如果是右括号 就依次弹出s1栈顶的运算符 并且压入s2直到遇到左括号
                while (!"(".equals(operatorStack.peek())) {
                    suffixExpression.add(operatorStack.pop());
                }
                operatorStack.pop(); //让 ( 弹出
            } else {
                //当item的优先级小于等于s1栈顶运算符, 将s1栈顶的运算符弹出并加入到s2中
                while (!operatorStack.isEmpty() && Operation.getValue(operatorStack.peek()) >= Operation.getValue((String) item)) {
                    suffixExpression.add(operatorStack.pop());
                }
                //还需要将item入栈
                operatorStack.push((String) item);
            }
        }
        //将s1中剩余的运算符依次弹出来并且加入s2
        while (!operatorStack.isEmpty()) {
            suffixExpression.add(operatorStack.pop());
        }
    }

    /**
     * 完成后缀表达式的计算
     * @return expressionResVal
     */
    public String evaluateExpression() throws Exception {
        //讨论特殊情况 当只有一个元素时候
        if (suffixExpression.size() == 1) {
            Object o = suffixExpression.get(0);
            if (o.getClass().equals(String.class)) {
                return ExpressionStatus.FORMAT_ERROR;
            } else if (o.getClass().equals(Operand.class)) {
                boolean isNegative = ((Operand) o).isNegative();
                this.isKeep = true;
                String init = ((Operand) o).getOperandData();
                //进行进制转换
                //TODO 进制转换
                ((Operand) o).setOperandData(StringUtils.changeSystem(init, ((Operand) o).getSystemEnum(), this.expressionCurrentSystem));
                if (isNegative) {
                    return "-" + ((Operand) o).getOperandData();

                } else {
                    return ((Operand) o).getOperandData();
                }
            }
        }
        //建立栈 (Java官方推荐用Deque代替Stack)
        Deque<Operand> deque = new LinkedList<>();
        // 去遍历后缀表达式
        for (Object o : suffixExpression) {
            //如果是具体操作数的话
            if (o.getClass().equals(Operand.class)) {
                //入栈
                deque.addFirst((Operand) o);
            } else {
                //弹出两个数，并且运算再入栈
                Operand operand1 = deque.pop();
                if (deque.isEmpty()) {
                    //操作数缺失
                    return ExpressionStatus.FORMAT_ERROR;
                }
                Operand operand2 = deque.pop();

                //转为十进制
                String operand1Str = StringUtils.everySystem2Dec(operand1.getOperandData(), operand1.getSystemEnum());
                String operand2Str = StringUtils.everySystem2Dec(operand2.getOperandData(), operand2.getSystemEnum());
                //判断正负
                if (operand1.isNegative() == true) {
                    operand1Str = "-" + operand1Str;
                }
                if (operand2.isNegative() == true) {
                    operand2Str = "-" + operand2Str;
                }
                //转换 高精度
                BigDecimal bigOperand1 = new BigDecimal(operand1Str);
                BigDecimal bigOperand2 = new BigDecimal(operand2Str);
                switch ((String) o) {
                    case "+":
                        bigOperand1 = bigOperand1.add(bigOperand2);
                        deque.push(new Operand(SystemEnum.DEC, bigOperand1.toPlainString()));
                        break;
                    case "-":
                        bigOperand1 = bigOperand2.subtract(bigOperand1);
                        deque.push(new Operand(SystemEnum.DEC, bigOperand1.toPlainString()));
                        break;
                    case "*":
                        bigOperand1 = bigOperand2.multiply(bigOperand1);
                        deque.push(new Operand(SystemEnum.DEC, bigOperand1.toPlainString()));
                        break;
                    case "/":
                        //如果除数是0的话
                        if (judgeZero(bigOperand1.toPlainString())) {
                            return ExpressionStatus.ZERO_ERROR;
                        }
                        bigOperand1 = bigOperand2.divide(bigOperand1, MathContext.DECIMAL128);
                        deque.push(new Operand(SystemEnum.DEC, bigOperand1.toPlainString()));
                        break;
                    default:
                        //能走到这说明 这个操作符是位运算符 我现在要判断两个 运算数的格式
                        if (operand1Str.contains(".") || operand2Str.contains(".")) {
                            boolean flag = StringUtils.judgeZeroAfterPoint(operand1Str) && StringUtils.judgeZeroAfterPoint(operand2Str);
                            if (!flag) {
                                return ExpressionStatus.FORMAT_ERROR;
                            }
                        }
                        int num1 = bigOperand1.intValue();
                        int num2 = bigOperand2.intValue();
                        int res;
                        switch ((String) o) {
                            case "^":
                                res = num2 ^ num1;
                                deque.push(new Operand(SystemEnum.DEC, String.valueOf(res)));
                                break;
                            case "|":
                                res = num2 | num1;
                                deque.push(new Operand(SystemEnum.DEC, String.valueOf(res)));
                                break;
                            case "&":
                                res = num2 & num1;
                                deque.push(new Operand(SystemEnum.DEC, String.valueOf(res)));
                                break;
                            case "@":
                                System.out.println(num2);
                                Operand operand = new Operand(SystemEnum.DEC, Integer.toString(num2));
                                String resStr = operand.doShf(Integer.toString(num1));
                                deque.push(new Operand(SystemEnum.DEC, resStr));
                                break;
                            default:
                        }
                }
            }
        }
        //最终留在栈里面的数据就是运算结果
        if (deque.size() > 1) {
            //说明缺失操作符
            return ExpressionStatus.FORMAT_ERROR;
        }
        //可能带负号
        //TODO bug修复
        Operand resOperand = deque.peek();
        if (!(resOperand.getOperandData().startsWith("-")) && resOperand.isNegative()) {
            resOperand.setOperandData("-" + resOperand.getOperandData());
        }
        return deque.pop().getOperandData();
    }


    /**
     * 求反码
     *
     * @return res
     */
    public String get1Sc() throws Exception {
        Operand operand = getScOperand();
        boolean isNegative = operand.isNegative();
        String res = operand.get1SC();
        res = addressScRes(res, isNegative);
        return res;
    }

    /**
     * 求补码
     */
    public String get2Sc() throws Exception {
        Operand operand = getScOperand();
        String res = operand.get2SC();
        res = addressScRes(res, operand.isNegative());
        return res;
    }


    /**
     * 得到专门用来处理 求反码补码的对象
     *
     * @return operand
     */
    private Operand getScOperand() {
        expression = expression.replaceAll("\\)", "");
        expression = expression.replaceAll("\\(", "");
        return Operand.getOperand(expression);
    }

    /**
     * 让反码补码 变成8位二进制数
     */
    private String addressScRes(String target, boolean isNegative) {
        // 首先是不含.的情况
        StringBuilder targetBuilder = new StringBuilder(target);
        int count = 1;
        if (target.contains(".")) {
            if (isNegative == true) {
                while (targetBuilder.length() < 9) {
                    targetBuilder.insert(count++, "1");
                }
            } else {
                while (targetBuilder.length() < 9) {
                    targetBuilder.insert(count++, "0");
                }
            }
        } else {
            if (isNegative == true) {
                while (targetBuilder.length() < 8) {
                    targetBuilder.insert(count++, "1");
                }
            } else {
                while (targetBuilder.length() < 8) {
                    targetBuilder.insert(count++, "0");
                }
            }
        }
        target = targetBuilder.toString();
        return target;
    }

    /**
     * 用于判断表达式的合法性
     */
    //TODO
    public String isLegalExpression() {
        expression = expression.trim();
        //处理特殊情况 DEC- ( DEC5 + DEC3 )
        StringBuilder sb = new StringBuilder(expression);
        //记录改变的次数
        int count = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '-' && expression.charAt(i + 1) == ' ' && expression.charAt(i - 1) != ' ') {
                sb.insert(i + count, "0 ");
                count = count + 2;
            }
        }
        expression = sb.toString();
        //先把表达式分割
        String[] split = expression.split("\\s+");
        if (split.length == 2) {
            expression = expression.replaceAll("\\(", "");
            expression = expression.replaceAll("\\)", "");
        }
        //判断格式
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (!(s.startsWith("DEC") || s.startsWith("BIN") || s.startsWith("HEX"))) {
                if ("(".equals(s)) {
                    if (")".equals(split[i + 1])) {
                        return ExpressionStatus.FORMAT_ERROR;
                    }
                }
                continue;
            }
            String system = s.substring(0, 3);
            String data = s.substring(3);
            char ch = s.charAt(3);
            boolean isNegative = ch == '-';
            int index;
            switch (system) {
                case "HEX":
                    index = isNegative ? 1 : 0;
                    while (index < data.length()) {
                        char c = data.charAt(index);
                        if (!(Character.isDigit(c) || (c >= 'A' && 'F' >= c) || c == '.' || c == '(' || c == ')' || c == '-')) {
                            return ExpressionStatus.FORMAT_ERROR;
                        }
                        index++;
                    }
                    break;
                case "DEC":
                    index = isNegative ? 1 : 0;
                    while (index < data.length()) {
                        char c = data.charAt(index);
                        if (!(Character.isDigit(c) || c == '.' || c == '(' || c == ')' || c == '-')) {
                            return ExpressionStatus.FORMAT_ERROR;
                        }
                        index++;
                    }
                    break;
                case "BIN":
                    index = isNegative ? 1 : 0;
                    while (index < data.length()) {
                        char c = data.charAt(index);
                        if (!(c == '0' || c == '1' || c == '.' || c == '(' || c == ')' || c == '-')) {
                            return ExpressionStatus.FORMAT_ERROR;
                        }
                        index++;
                    }
                    break;
                default:
                    break;
            }
        }
        return ExpressionStatus.CORRECT;

    }

    /**
     * 正则表达式判断是不是 0或者 0.00000....
     */
    //TODO
    public static boolean judgeZero(String str) {
        if ("0".equals(str)) {
            return true;
        } else {
            String pattern = "^0\\.0+$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            return m.matches();
        }
    }

    /**
     * 只能是数值求反码而不是 式子
     *
     * @param str 传来的expression
     */
    public String judgeIsLegal(String str) {
        String[] split = str.split("\\s+");
        if (split.length != 2) {
            return ExpressionStatus.FORMAT_ERROR;
        } else {
            if (!(split[0].startsWith("DEC") || split[0].startsWith("HEX") || split[0].startsWith("BIN"))) {
                return ExpressionStatus.FORMAT_ERROR;
            }
        }
        return ExpressionStatus.CORRECT;
    }

}
