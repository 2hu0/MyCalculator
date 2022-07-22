package soft.service;


import soft.Utils.ExpressionStatus;
import soft.Utils.StringUtils;
import soft.Utils.SystemEnum;

import java.util.function.Consumer;

/**
 * 对计算器功能的一些封装
 *
 * @author 2hu0
 */
public class Work {
    /**
     * 传入的表达式
     */
    private Expression expression;
    /**
     * 式子返回的结果对象
     */
    private Result result = new Result("", SystemEnum.DEC);

    /**
     * 封装好的一个对表达式求值方法
     */
    public Consumer<Expression> doWork = targetExpression -> {
        //获取状态码
        String status = targetExpression.addressExpression();
        if (status.equals(ExpressionStatus.FORMAT_ERROR)) {
            result.setAnswer(ExpressionStatus.FORMAT_ERROR);
            return;
        }
        switch (targetExpression.getWorkFunction()) {
            case EVALUATE_EXPRESSION:
                try {
                    String ans = targetExpression.evaluateExpression();
                    result.setAnswer(ans);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result.getAnswer().equals(ExpressionStatus.FORMAT_ERROR) || result.getAnswer().equals(ExpressionStatus.ZERO_ERROR)) {
                    break;
                }
                try {
                    if (targetExpression.isKeep() == true) {
                        result.setSystemEnum(targetExpression.getExpressionCurrentSystem());
                    }else {
                        if (result.getAnswer().startsWith("-")){
                            String temp = result.getAnswer().substring(1);
                            result.setAnswer("-" + StringUtils.changeSystem(temp, SystemEnum.DEC, targetExpression.getExpressionCurrentSystem()));
                        }else {
                            result.setAnswer(StringUtils.changeSystem(result.getAnswer(), SystemEnum.DEC, targetExpression.getExpressionCurrentSystem()));
                        }
                        result.setSystemEnum(targetExpression.getExpressionCurrentSystem());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addressZero();
                break;
            case GET_ONES_COMPLEMENT_CODE:

                try {
                    targetExpression.get1Sc();
                    result.setAnswer(targetExpression.get1Sc());
                    if (result.getAnswer().equals(ExpressionStatus.FORMAT_ERROR) || result.getAnswer().equals(ExpressionStatus.ZERO_ERROR)) {
                        break;
                    }
                    result.setSystemEnum(SystemEnum.BINARY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GET_COMPLEMENTAL_CODE:
                try {
                    result.setSystemEnum(SystemEnum.BINARY);
                    result.setAnswer(targetExpression.get2Sc());
                    if (result.getAnswer().equals(ExpressionStatus.FORMAT_ERROR) || result.getAnswer().equals(ExpressionStatus.ZERO_ERROR)) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    };




    public Result getResult() {
        return result;
    }





    public String getRes() {
        return result.getAnswer();
    }

    public void addressZero() {
        if (result.getAnswer().contains(".")) {
            int index = result.getAnswer().indexOf(".");
            String left = result.getAnswer().substring(0, index);
            String right = result.getAnswer().substring(index);
            int count;
            for (count = right.length() - 1; count >= 0; count--) {
                if (right.charAt(count) == '0') {
                    continue;
                } else {
                    //找到第一个不为0的下标
                    if (count == 0) {
                        //如果遍历到小数点 说明小数点之后都是0
                        //就把小数点给去掉
                        right = "";
                    } else {
                        right = right.substring(0, count + 1);
                        break;
                    }
                }
            }
            result.setAnswer(left + right);
        }
    }
}
