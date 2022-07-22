package soft.service;

/**枚举类别
 * 带有运算符的求值运算
 * 或者求反码、补码
 * @author 2hu0
 */

public enum WorkFunction {
    /**
     * 运算符表达式求值
     */
    EVALUATE_EXPRESSION("="),
    /**
     * 反码
     */
    GET_ONES_COMPLEMENT_CODE("1'SC"),
    /**
     * 补码
     */
    GET_COMPLEMENTAL_CODE("2'SC");

    public String val;
    static {
        EVALUATE_EXPRESSION .val = "=";
        GET_COMPLEMENTAL_CODE.val = "2'SC";
        GET_ONES_COMPLEMENT_CODE.val = "1'SC";
    }

    WorkFunction(String s) {

    }

}
