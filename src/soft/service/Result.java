package soft.service;


import soft.Utils.SystemEnum;

/**
 * @author 2hu0
 */
public class Result {
    /**
     * 运算结果的字符串形式
     */
    private String answer;
    /**
     * 运算结果的进制数
     */
    private SystemEnum systemEnum;

    public Result(String answer, SystemEnum systemEnum) {
        this.answer = answer;
        this.systemEnum = systemEnum;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public SystemEnum getSystemEnum() {
        return systemEnum;
    }

    public void setSystemEnum(SystemEnum systemEnum) {
        this.systemEnum = systemEnum;
    }
}
