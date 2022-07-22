package soft.Utils;


import java.math.BigDecimal;
import java.math.MathContext;


/**
 * 工具类 可以实现十进制、十六进制、二进制的互相转化
 * 既可以支持带小数点的，也可以支持不带小数点的
 * 可以实现浮点数十六进制串转十进制功能 例如 0x1.8p1 - > 3
 * 此外也封装了一些针对于本项目所需要的一些功能方法
 * @author 2hu0
 */
public class StringUtils {
    public static final String POINT = ".";

    /**
     * 浮点数十六进制串转为十进制
     *
     * @param hexString 给定十六进制字符串
     * @return DecString 转换后十进制字符串
     */
    public static String doubleHex2Dec(String hexString) {
        // 这个可以得到 形如 0x1.8p1  (3) 的浮点数十六进制表达式
        //用 substring裁去(-)0x
        //用于存储符号位
        String judgeSign = "";
        if (hexString.startsWith(POINT)) {
            //如果是负数 截去3
            judgeSign = "-";
            hexString = hexString.substring(3);
        } else {
            //正数裁去2 即可
            hexString = hexString.substring(2);
        }
        //用这个可以得到是乘2的多少次方  用pow表示
        int index = hexString.indexOf("p");
        String pow_string = hexString.substring(index + 1);
        int pow = Integer.parseInt(pow_string);
        //得到中间的十六进制表达串 1.8p1->1.8
        hexString = hexString.substring(0, hexString.length() - pow_string.length() - 1);
        //现在对 表达式进行处理 (转为我们需要的十进制数)
        String init_dec = StringUtils.middleDoubleHex2Dec(hexString);
        BigDecimal res = BigDecimal.valueOf(Double.parseDouble(init_dec));
        res = res.multiply((new BigDecimal(2).pow(pow)));
        return judgeSign + res.toString();

    }

    /**
     * 浮点数十六进制转十进制 (处理不带 后面p的)
     * 位于中间过程
     *
     * @param hexString 给定十六进制字符串
     */
    public static String middleDoubleHex2Dec(String hexString) {
        hexString = hexString.toUpperCase();
        String[] split = hexString.split(POINT);
        int x = Integer.parseInt(split[0]);
        BigDecimal res = new BigDecimal(x);
        for (int i = 1; i < split.length; i++) {
            BigDecimal pow = (new BigDecimal(16)).pow(-i, MathContext.DECIMAL64);
            //如果是数字的话
            res = getBigDecimal(split, res, i, pow);
        }
        return res.toString();
    }

    public static BigDecimal getBigDecimal(String[] split, BigDecimal res, int i, BigDecimal pow) {
        if (Character.isDigit(split[i].charAt(0))) {
            res = res.add(new BigDecimal(Integer.parseInt(split[i])).multiply(pow));
        } else {
            switch (split[i]) {
                case "A":
                    res = res.add(new BigDecimal(HexConstant.HEX_A.val).multiply(pow));
                    return res;
                case "B":
                    res = res.add(new BigDecimal(HexConstant.HEX_B.val).multiply(pow));
                    return res;
                case "C":
                    res = res.add(new BigDecimal(HexConstant.HEX_C.val).multiply(pow));
                case "D":
                    res = res.add(new BigDecimal(HexConstant.HEX_D.val).multiply(pow));
                    return res;
                case "E":
                    res = res.add(new BigDecimal(HexConstant.HEX_E.val).multiply(pow));
                    return res;
                case "F":
                    res = res.add(new BigDecimal(HexConstant.HEX_F.val).multiply(pow));
                default:
                    return res;
            }
        }
        return res;
    }

    /**
     * 十进制整数转十六进制
     *
     * @param data 十进制字符串
     */
    public static String dec2Hex(int data) {
        return Integer.toHexString(data).toUpperCase();
    }

    /**
     * 十进制小数转十六进制' 调用重载函数
     *
     * @param d_name 传来的浮点数的String表达形式
     */
    public static String smallHex2Dec(String d_name) {
        return smallHex2Dec(d_name, 32);
    }

    /**
     * 十进制小数转十六进制的重载函数
     *
     * @param dName 浮点数
     * @param count  位数
     */
    public static String smallHex2Dec(String dName, int count) {
        double data = Double.parseDouble(dName);
        BigDecimal new_Data = new BigDecimal(data);
        //每次的乘积
        BigDecimal multi;
        StringBuilder res = new StringBuilder();
        while (count >= 0 && !new_Data.equals(0)) {
            multi = new_Data.multiply(new BigDecimal(16));
            int dec = multi.intValue();
            if (dec > 9) {
                switch (dec) {
                    case 10:
                        res.append("A");
                        new_Data = multi.subtract(new BigDecimal(dec));
                        count--;
                        continue;
                    case 11:
                        res.append("B");
                        new_Data = multi.subtract(new BigDecimal(dec));
                        count--;
                        continue;
                    case 12:
                        res.append("C");
                        new_Data = multi.subtract(new BigDecimal(dec));
                        count--;
                        continue;
                    case 13:
                        res.append("D");
                        new_Data = multi.subtract(new BigDecimal(dec));
                        count--;
                        continue;
                    case 14:
                        res.append("E");
                        new_Data = multi.subtract(new BigDecimal(dec));
                        count--;
                        continue;
                    case 15:
                        res.append("F");
                        new_Data = multi.subtract(new BigDecimal(dec));
                        count--;
                        continue;
                    default:
                        continue;
                }
            } else {
                res.append(dec);
                new_Data = multi.subtract(new BigDecimal(dec));
                count--;
            }
        }
        return res.toString();
    }

    /**
     * 十进制数字转十六进制 (合体版本)
     */
    public static String allDecToHex(String double_data) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < double_data.length(); i++) {
            if (double_data.charAt(i) == '.') {
                double data = Double.parseDouble(double_data);
                //整数部分
                int int_data = (int) data;
                //小数部分
                BigDecimal bigDecimal = new BigDecimal(data);
                BigDecimal intDecimal = new BigDecimal(int_data);
                BigDecimal d_data = bigDecimal.subtract(intDecimal);

                res.append(dec2Hex(int_data));
                res.append(".");
                String a = smallHex2Dec(String.valueOf(d_data));
                res.append(a);
                return res.toString();
            }
        }
        //走到这说明没有小数点
        int data = Integer.parseInt(double_data);
        return res.append(dec2Hex(data)).toString();

    }

    /**
     * 十六进制整数转十进制
     *
     * @param hex 目标字符串
     */
    public static String int_Hex2Dec(String hex) {
        Integer data = Integer.parseInt(hex, 16);
        return String.valueOf(data);
    }

    /**
     * 十六进制小数转十进制
     * @param hex 给定十进制小数(不含小数点 只接收小数点后面的位数)
     */
    public static String small_Hex2Dec(String hex) {
        BigDecimal res = new BigDecimal("0.0");
        for (int i = 0; i < hex.length(); i++) {
            BigDecimal pow = (new BigDecimal(16)).pow(-1 - i, MathContext.DECIMAL64);
            //考虑 0 - 9
            if (hex.charAt(i) >= '0' && hex.charAt(i) <= '9') {
                res = res.add(new BigDecimal(Integer.parseInt(String.valueOf(hex.charAt(i)))).multiply(pow));
            } else {
                switch (String.valueOf(hex.charAt(i))) {
                    case "A":
                        res = res.add(new BigDecimal(HexConstant.HEX_A.val).multiply(pow));
                        continue;
                    case "B":
                        res = res.add(new BigDecimal(HexConstant.HEX_B.val).multiply(pow));
                        continue;
                    case "C":
                        res = res.add(new BigDecimal(HexConstant.HEX_C.val).multiply(pow));
                    case "D":
                        res = res.add(new BigDecimal(HexConstant.HEX_D.val).multiply(pow));
                        continue;
                    case "E":
                        res = res.add(new BigDecimal(HexConstant.HEX_E.val).multiply(pow));
                        continue;
                    case "F":
                        res = res.add(new BigDecimal(HexConstant.HEX_F.val).multiply(pow));
                    default:
                        return "";
                }
            }
        }
        return res.toString();
    }

    /**
     * 十六进制转十进制 (整数小数合体版本)
     * @param hexString 给定十六进制串 可以带小数点
     * @return StringUtils.int_Hex2Dec(hexString);
     */
    public static String allHex2Dec(String hexString) {
        if (hexString.contains(POINT)) {
            String decData = hexString.substring(0, hexString.indexOf(POINT));
            String smallData = hexString.substring(hexString.indexOf(POINT) + 1);
            //整数部分
            String decString = StringUtils.int_Hex2Dec(decData);
            BigDecimal decAns = new BigDecimal(decString);
            //小数部分
            String smallString = StringUtils.small_Hex2Dec(smallData);
            BigDecimal smallRes = new BigDecimal(smallString);
            //将小数部分和整数部分相加
            BigDecimal finalRes = decAns.add(smallRes);
            return finalRes.toString();
        } else {
            //说明没有小数点 是整数
            return StringUtils.int_Hex2Dec(hexString);
        }
    }

    /**
     * 十进制整数转二进制
     *
     * @param data 整数数据
     * @return binary.reverse().toString()
     */
    public static String intDec2Binary(int data) {
        return Integer.toBinaryString(data);
    }

    /**
     * 十进制小数转二进制
     */
    public static String double2Binary(String d_name) throws Exception {
        //利用方法重装实现默认参数
        return double2Binary(d_name, 100);
    }

    /**
     * 十进制小数转二进制
     * @param dName 小数点后面的数字
     * @param count 位数
     * @return res.toString();
     */
    public static String double2Binary(String dName, int count) {
        double data = Double.parseDouble(dName);
        //每次的乘积
        double multi;
        StringBuilder res = new StringBuilder();
        while (count >= 0 && data != 0) {
            multi = data * 2;
            if (multi >= 1) {
                res.append(1);
                data = multi - 1;
            } else {
                res.append(0);
                data = multi;
            }
            count--;
        }
        return res.toString();
    }

    /**
     * 十进制数转二进制 （整数和小数合体版本）
     */
    public static String allToBinary(String double_data) throws Exception {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < double_data.length(); i++) {
            if (double_data.charAt(i) == '.') {
                double data = Double.parseDouble(double_data);
                //整数部分
                int int_data = (int) data;
                //小数部分
                BigDecimal bigDecimal = new BigDecimal(data);
                BigDecimal intDecimal = new BigDecimal(int_data);
                BigDecimal d_data = bigDecimal.subtract(intDecimal);
                res.append(intDec2Binary(int_data));
                res.append(".");
                //获取小数部分的字符串
                String smallRes = double2Binary(String.valueOf(d_data));
                if (!"".equals(smallRes)) {
                    res.append(smallRes);
                }else{
                    res.deleteCharAt(res.length()-1);
                }
                return res.toString();
            }
        }
        //走到这说明没有小数点
        int data = Integer.parseInt(double_data);
        return res.append(intDec2Binary(data)).toString();
    }

    /**
     * 二进制转十进制
     * @param binaryString 给定二进制字符串 （可以包含小数点）
     */
    public static String binary2Dec(String binaryString) {
        if (binaryString.contains(POINT)) {
            String leftData = binaryString.substring(0, binaryString.indexOf(POINT));
            BigDecimal leftRes = new BigDecimal("0.0");
            BigDecimal rightRes = new BigDecimal("0.0");
            String rightData = binaryString.substring(binaryString.indexOf(POINT) + 1);
            leftRes = getBigDecimal(leftData, leftRes);
            for (int i = 0; i < rightData.length(); i++) {
                BigDecimal pow = new BigDecimal(2).pow(-1 - i, MathContext.DECIMAL128);
                int val = Integer.parseInt(String.valueOf(rightData.charAt(i)));
                rightRes = rightRes.add(new BigDecimal(val).multiply(pow));
            }
            BigDecimal finalRes = leftRes.add(rightRes);
            return finalRes.toString();
        } else {
            BigDecimal finalRes = new BigDecimal("0.0");
            finalRes = getBigDecimal(binaryString, finalRes);
            return finalRes.toString();
        }
    }

    private static BigDecimal getBigDecimal(String leftData, BigDecimal leftRes) {
        for (int i = leftData.length() - 1, count = 0; i >= 0; i--) {
            BigDecimal pow = new BigDecimal(2).pow(count, MathContext.DECIMAL128);
            int val = Integer.parseInt(String.valueOf(leftData.charAt(i)));
            leftRes = leftRes.add(new BigDecimal(val).multiply(pow));
            count++;
        }
        return leftRes;
    }

    /**
     * 十六转二进制
     * 思路 ：十六->十->二进制
     */
    public static String hex2Binary(String hexString) throws Exception {
        String decString = StringUtils.allHex2Dec(hexString);
        return StringUtils.allToBinary(decString);
    }

    /**
     * 二进制转十六进制
     * 思路 二->十->十六
     */
    public static String binary2Hex(String binaryString) {
        String decString = StringUtils.binary2Dec(binaryString);
        String hexString = StringUtils.allDecToHex(decString);
        return hexString;
    }

    /**
     * 实现任意进制转十进制
     *
     * @param initString    给定字符串 可以是任意进制
     * @param currentSystem 当前进制
     */
    public static String everySystem2Dec(String initString, SystemEnum currentSystem) {
        switch (currentSystem) {
            case DEC:
                return initString;
            case BINARY:
                return StringUtils.binary2Dec(initString);
            case HEX:
                return StringUtils.allHex2Dec(initString);
        }
        return "error !";
    }

    /**
     * 任意进制转二进制
     *
     * @param initString    给定字符串 可以是任意进制
     * @param currentSystem 当前进制
     */
    public static String evertSystem2Binary(String initString, SystemEnum currentSystem) throws Exception {
        switch (currentSystem) {
            case BINARY:
                return initString;
            case DEC:
                return StringUtils.allToBinary(initString);
            case HEX:
                return StringUtils.hex2Binary(initString);
        }
        return "error";
    }

    /**
     * 任意进制转十六进制
     * @param initString    给定字符串 可以是任意进制
     * @param currentSystem 当前进制
     */
    public static String everySystem2Hex(String initString, SystemEnum currentSystem) {
        switch (currentSystem) {
            case BINARY:
                return StringUtils.binary2Hex(initString);
            case DEC:
                return StringUtils.allDecToHex(initString);
            case HEX:
                return initString;
        }
        return "error !";
    }

    /**
     * 从 a进制 到b进制的转换
     */
    public static String changeSystem(String initString, SystemEnum currentSystem, SystemEnum targetSystem) throws Exception {
        switch (currentSystem) {
            case DEC:
                switch (targetSystem) {
                    case BINARY:
                        initString = StringUtils.allToBinary(initString);
                        return initString;
                    case DEC:
                        return initString;
                    case HEX:
                        initString = StringUtils.allDecToHex(initString);
                        return initString;
                }
            case BINARY:
                switch (targetSystem) {
                    case BINARY:
                        return initString;
                    case DEC:
                        initString = StringUtils.binary2Dec(initString);
                        return initString;
                    case HEX:
                        initString = StringUtils.binary2Hex(initString);
                        return initString;
                }
            case HEX:
                switch (targetSystem) {
                    case HEX:
                        return initString;
                    case BINARY:
                        initString = StringUtils.hex2Binary(initString);
                        return initString;
                    case DEC:
                        initString = StringUtils.allHex2Dec(initString);
                        return initString;
                }
        }
        return initString;
    }

    /**
     * 判断一个数小数点后的全部位数是不是为0
     */
    public static boolean judgeZeroAfterPoint(String str) {
        if (!str.contains(POINT)) {
            return true;
        }
        String target = str.substring(str.indexOf(".") + 1);
        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

}
