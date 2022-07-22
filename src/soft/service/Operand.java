package soft.service;



import soft.Utils.ExpressionStatus;
import soft.Utils.StringUtils;
import soft.Utils.SystemEnum;

import java.math.BigDecimal;



/**
 * 对运算对象进行的封装
 *
 * @author 2hu0
 */
public class Operand {
    /**
     * 运算对象的当前进制
     */
    private SystemEnum systemEnum = SystemEnum.DEC;
    /**
     * 运算对象的String表达形式
     */
    private String operandData;
    /**
     * 运算对象的正负
     */
    private boolean isNegative = true;

    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    public Operand(SystemEnum systemEnum, String operandData) {
        this.systemEnum = systemEnum;
        if (operandData.charAt(0) == '-') {
            this.isNegative = true;
            this.operandData = operandData.substring(1);
        }else {
            this.isNegative = false;
            this.operandData = operandData;
        }
    }

    public Operand(String operandData) {
        this.operandData = operandData;
    }

    public Operand() {
    }

    public boolean isNegative() {
        return isNegative;
    }

    @Override
    public String toString() {
        return "Operand{" +
                "systemEnum=" + systemEnum +
                ", operandData='" + operandData + '\'' +
                '}';
    }

    public SystemEnum getSystemEnum() {
        return systemEnum;
    }

    public void setSystemEnum(SystemEnum systemEnum) {
        this.systemEnum = systemEnum;
    }

    public String getOperandData() {
        return operandData;
    }

    public void setOperandData(String operandData) {
        this.operandData = operandData;
    }

    /**
     * 操作数串->操作数对象 例如 ( DEC-9 ) -> 9 这个9是封装过的对象
     * 可以通过字段说明这个值目前所处于的状态 包括 进制  是正还是负等
     *
     * @param operandData String表达形式
     * @return operand 新对象
     */
    //TODO
    public static Operand getOperand(String operandData) {
        //获取标识符 DEC HEX BIN
        String system = operandData.substring(0, 3);
        Operand operand = new Operand();
        if (operandData.charAt(3) == '-') {
            String temp = operandData.substring(4);
            if (Expression.judgeZero(temp) == true) {
                //说明是-0.000这种情况
                operand.setNegative(false);
                operand.setOperandData(temp);
            }else {
                //说明是负数 从符号后面＋
                operand.setNegative(true);
                operand.setOperandData(operandData.substring(4, operandData.length()));
            }
        } else {
            //说明是正数
            operand.setNegative(false);
            operand.setOperandData(operandData.substring(3, operandData.length()));
        }
        switch (system) {
            case "DEC":
                operand.setSystemEnum(SystemEnum.DEC);
                break;
            case "HEX":
                operand.setSystemEnum(SystemEnum.HEX);
                break;
            case "BIN":
                operand.setSystemEnum(SystemEnum.BINARY);
                break;
            default:
                break;
        }
        return operand;
    }

    /**
     * 对该运算对象进行进制转换处理，
     * 返回新对象
     *
     * @param targetSystem 需要转换成的进制
     * @return newOperand
     */
    public static Operand addressOperand(Operand oldOperand, SystemEnum targetSystem) {
        Operand newOperand = new Operand();
        switch (targetSystem) {
            case DEC:
                switch (oldOperand.systemEnum) {
                    case BINARY:
                        newOperand.setOperandData(StringUtils.binary2Dec(oldOperand.operandData));
                        newOperand.setSystemEnum(SystemEnum.DEC);
                        return newOperand;
                    case HEX:
                        newOperand.setOperandData(StringUtils.allHex2Dec(oldOperand.operandData));
                        newOperand.setSystemEnum(SystemEnum.DEC);
                        return newOperand;
                }
            case HEX:
                switch (oldOperand.systemEnum) {
                    case BINARY:
                        newOperand.setOperandData(StringUtils.binary2Hex(oldOperand.operandData));
                        newOperand.setSystemEnum(SystemEnum.HEX);
                        return newOperand;
                    case DEC:
                        newOperand.setOperandData(StringUtils.allDecToHex(oldOperand.operandData));
                        newOperand.setSystemEnum(SystemEnum.HEX);
                        return newOperand;
                    default:
                        return null;
                }
            default:
                return newOperand;

        }

    }

    /**
     * 求原码 （就是求二进制）
     * TODO bug已修复
     */

    public String getBinary() throws Exception {
        String res = this.operandData;
        if (res.equals(0) || (res.startsWith("0.") && StringUtils.judgeZeroAfterPoint(res))) {
            return res;
        }
        //判断当前运算符的进制情况 统一变成二进制
        switch (this.systemEnum) {
            case DEC:
                res = StringUtils.allToBinary(this.getOperandData());
                break;
            case HEX:
                res = StringUtils.hex2Binary(this.getOperandData());
                break;
            case BINARY:
                break;
        }
        BigDecimal bigDecimal = new BigDecimal(res);
        if (BigDecimal.ZERO.equals(bigDecimal)) {
            return res;
        }
        if (isNegative == false) {
            res = "0" + res;
        } else {
            res = "1" + res;
        }

        return res;
    }

    /**
     * 求反码
     */
    public String get1SC() throws Exception {
        if (isNegative == true) {
            String trueForm = this.getBinary();
            StringBuilder sb = new StringBuilder();
            //初始化为符号位
            sb.append(trueForm.substring(0, 1));
            for (int i = 1; i < trueForm.length(); i++) {
                if (trueForm.charAt(i) == '0') {
                    sb.append("1");
                } else if (trueForm.charAt(i) == '1') {
                    sb.append("0");
                } else if (trueForm.charAt(i) == '.') {
                    sb.append(trueForm.charAt(i));
                }
            }
            return sb.toString();
            //如果是正数 反码就为其正数本身
        } else {
            return this.getBinary();
        }
    }

    /**
     * 求补码
     * 正数的补码是其本身
     * 负数的补码是在其原码的基础上 符号位不变
     * 其余各位取反 最后加1
     */
    public String get2SC() throws Exception {
        String res = "";
        if (isNegative == false) {
            //整数或者零的反码 补码是其本身
            res = this.getBinary();
        } else {
            //先得到反码
            String inverse = this.get1SC();
            BigDecimal index = new BigDecimal(StringUtils.binary2Dec(inverse));
            //反码+1
            index = index.add(new BigDecimal(1));
            //再次转二进制
            res = StringUtils.allToBinary(index.toString());
        }
        return res;
    }

    /**
     * 移位数
     * count代表要移动的位数
     * 只能用来操作整型
     * 默认是左移
     * 如果 count = “-1”
     * 那就是向右移动1位
     */
    public String doShf(String count) throws Exception {

        if (count.contains(".")) {
            if (StringUtils.judgeZeroAfterPoint(count)) {
                //如果小数点后全部为0,说明小数点可以去掉了
                count = count.substring(0,count.indexOf("."));
            }else {
                //否则报错
                return ExpressionStatus.FORMAT_ERROR;
            }
        }
        //字符串结果
        StringBuilder res = new StringBuilder();
        if (this.isNegative == true && !this.operandData.startsWith("-")) {
            //如果是负数的话
            res.insert(0, '-');
        }
        switch (this.systemEnum) {
            case BINARY:
                res.append(StringUtils.binary2Dec(this.operandData));
                break;
            case HEX:
                res.append(StringUtils.allHex2Dec(this.operandData));
                break;
            default:
                res.append(operandData);
        }
        if (res.toString().contains(".")) {
            //如果是5.000我如何处理？
            //判断这个数是不是整型或者小数点后全为0 的数字

            String dec_str = res.substring(0, res.toString().indexOf("."));
            int dec_data = Integer.parseInt(dec_str);
            //如果说 这个数 它减去了 它整数部分的数字 返回的结果是0的话
            //可以说明这个数字小数点后面的数全都为0
            //再者，直接截取小数点后面的数字为单个字符串
            //暴力的去遍历它 看看是不是为0 即可
            //这里我们选第一种吧
            double resData = Double.parseDouble(res.toString());
            if (resData - dec_data != 0) {
                return ExpressionStatus.FORMAT_ERROR;
            }
            int data = Integer.parseInt(res.substring(0, res.indexOf(".")).toString());
        }
        int data = Integer.parseInt(res.toString());
        //需要移动的位数
        int target = Integer.parseInt(count);
        if (target >= 0) {
            //x这里是左移 target位数
            data = data << target;
        } else {
            //进到这里说明target为负数
            //进行右移 | target| 位
            target = -target;
            data = data >> target;
        }
        return Integer.toString(data);
    }

}
