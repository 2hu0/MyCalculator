package soft.service;


import soft.Utils.StringUtils;
import soft.Utils.SystemEnum;

/**
 * @author 2hu0
 */
public class CountObject {
    private static Work work = new Work();


    /**
     * 表达式求值用
     *
     * @param exp 界面给定表达式
     */
    public static String calculate(String exp) {
        Expression expression = new Expression(exp);
        work.doWork.accept(expression);
        return work.getRes();
    }

    /**
     * 表达式求值
     * @param exp 界面给定表达式
     */
    public static String doCalculate(String targetSystem, String exp) {
        SystemEnum systemEnum = selectSystem(targetSystem);
        Expression expression = new Expression(exp,systemEnum);
        work.doWork.accept(expression);
        return work.getRes();
    }

    /**
     * 单个数值 求其各种进制用
     * @param targetSystem 目标进制
     * @param current      当前数值字符串 (BIN101)
     */
    public static String calculate(String targetSystem, String current) throws Exception {
        SystemEnum systemEnum = SystemEnum.DEC;
        selectSystem(targetSystem);
        Operand operand = Operand.getOperand(current);
        StringUtils.changeSystem(operand.getOperandData(), operand.getSystemEnum(), systemEnum);
        work.getResult().setAnswer(StringUtils.changeSystem(operand.getOperandData(), operand.getSystemEnum(), systemEnum));
        return work.getRes();
    }

    private static SystemEnum selectSystem(String targetSystem) {
        SystemEnum systemEnum = SystemEnum.DEC;
        switch (targetSystem) {
            case "DEC":
                systemEnum = SystemEnum.DEC;
                break;
            case "BIN":
                systemEnum = SystemEnum.BINARY;
                break;
            case "HEX":
                systemEnum = SystemEnum.HEX;
                break;
            default:break;
        }
        return systemEnum;
    }
}
