package soft.test;


import org.junit.Test;
import soft.service.Expression;
import soft.service.Operand;
import soft.Utils.StringUtils;
import soft.Utils.SystemEnum;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
    public static void main(String[] args) {
        String res = "abcdef";
//        res = Long.toBinaryString(Long.parseLong(res,2) ^ Long.parseLong("10111",2));
//        System.out.println(res);
//        System.out.println(res.substring(1,res.length()));
//        System.out.println(0^1^1);
        System.out.println(Long.toBinaryString(Double.doubleToRawLongBits(1.34)));
    }
    @org.junit.Test
    public void test1() throws Exception {
        String x = "2.24";
        System.out.println(StringUtils.allToBinary(x));
    }
    @org.junit.Test
    public void test2() {
       String x = "-4";
        Integer a = Integer.parseInt(x);
        System.out.println(a);

    }
    @org.junit.Test
//            101.000110011001100110
//            101.0001100110011001100110011001100110011001100110011
    public void test3() throws Exception {
        Operand operand = new Operand();
        operand.setSystemEnum(SystemEnum.DEC);
        operand.setOperandData("3234.14159265357123456123123");
        String x = StringUtils.allToBinary(operand.getOperandData());
        System.out.println(x);
//     System.out.println(Operand.intDec2Binary(5));
    }

    @org.junit.Test
    public void test4(){
        String a = "110010100010.0010010000111111011010101000100001110001";
        System.out.println(a.length());
        String x = "0001100110011001100110011001100110011001100110011001101";
        System.out.println(x.length());
    }

    @org.junit.Test
    public void test5() {
        String x= "123";
//        System.out.println(Long.toHexString(Long.parseLong(x)).toUpperCase());
//        System.out.println(Long.toHexString(Long.parseLong(x)));
        //System.out.println(Double.doubleToLongBits(Double.parseDouble("0.2")));
//        System.out.println(Long.toHexString(Double.doubleToLongBits(Double.parseDouble("0.1"))));
//        System.out.println(Double.toHexString(1.23));
//        long l = Double.doubleToLongBits(6.91);
//        System.out.println(l);
//        System.out.println(Long.toHexString(l));
//        System.out.println(Double.toHexString(3));


        System.out.println(Double.toHexString(124));
    }
   //测试转中缀
    @org.junit.Test
    public void test6() {
        String x = "D4 * D5 - D8 + D60 + D8 / D2 ";
        Expression expression = new Expression(x);
        List<Object> infixExpressionList = expression.getInfixExpressionList();
        System.out.println(infixExpressionList);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < infixExpressionList.size(); i++) {
            System.out.println(infixExpressionList.get(i).getClass() == Operand.class);
            if (infixExpressionList.get(i).getClass().equals(Operand.class)) {
                Operand operand = (Operand) infixExpressionList.get(i);
                strings.add(operand.getOperandData());
                continue;
            }
            System.out.println((String) infixExpressionList.get(i));
            strings.add((String) infixExpressionList.get(i));
        }

        System.out.println(strings);
        //测试中缀转后缀
    }@org.junit.Test
    public void test7() {
        String x = "D4 * D5 - D8 + D60 + D8 / D2 ";
        Expression expression = new Expression(x);
        List<Object> infixExpressionList = expression.getInfixExpressionList();
        System.out.println(infixExpressionList);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < infixExpressionList.size(); i++) {
            System.out.println(infixExpressionList.get(i).getClass() == Operand.class);
            if (infixExpressionList.get(i).getClass().equals(Operand.class)) {
                Operand operand = (Operand) infixExpressionList.get(i);
                strings.add(operand.getOperandData());
                continue;
            }
            strings.add((String) infixExpressionList.get(i));
        }
        System.out.println(strings);
        expression.getSuffixExpression();
        List<Object> list = expression.getSuffixExpression();
        ArrayList<String> strings1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getClass() == Operand.class);
            if (list.get(i).getClass().equals(Operand.class)) {
                Operand operand = (Operand) list.get(i);
                strings1.add(operand.getOperandData());
                continue;
            }
            strings1.add((String) list.get(i));
        }
        System.out.println(strings1);

    }

    @org.junit.Test
    public void test8() {
        String hexDoubleString = Double.toHexString(-1.0);
        System.out.println("hexDoubleString = " + hexDoubleString);
        hexDoubleString = hexDoubleString.substring(2);
        System.out.println("hexDoubleString = " + hexDoubleString);
        int index = hexDoubleString.indexOf("p");
        String pow_string =  hexDoubleString.substring(index+1);
        System.out.println(pow_string);
        hexDoubleString = hexDoubleString.substring(0,hexDoubleString.length()-pow_string.length()-1);
        System.out.println(hexDoubleString);
    }

    @org.junit.Test
    public void test9(){
        BigDecimal bigDecimal = new BigDecimal(2);
        System.out.println(bigDecimal.multiply((new BigDecimal(3)).pow(-1, MathContext.DECIMAL64)));
    }

    @org.junit.Test
    public void test10() {
        String hexString = "1.f";
        hexString = hexString.toUpperCase();
        String[] split = hexString.split("\\.");
        int x = Integer.parseInt(split[0]);
        BigDecimal res = new BigDecimal(x);
        for (int i = 1;i<split.length;i++) {
            BigDecimal pow = (new BigDecimal(16)).pow(i - 2, MathContext.DECIMAL64);
            //如果是数字的话
            res = StringUtils.getBigDecimal(split, res, i, pow);
        }
        System.out.println(res.toString());
    }

    @org.junit.Test
    public void test11() {
        String DecString = "0x1.fp6";
        String judgeSign = "";
        String hexDoubleString = Double.toHexString(Double.parseDouble(DecString));
        if (hexDoubleString.startsWith("-")){
            //如果是负数 截去3
            judgeSign = "-";
            hexDoubleString = hexDoubleString.substring(3);
        }else {
            //正数裁去2 即可
            hexDoubleString = hexDoubleString.substring(2);
        }
        //用这个可以得到是乘2的多少次方  用pow表示
        int index = hexDoubleString.indexOf("p");
        String pow_string =  hexDoubleString.substring(index+1);
        int pow = Integer.parseInt(pow_string);
        //得到中间的十六进制表达串 1.8p1->1.8
        hexDoubleString = hexDoubleString.substring(0,hexDoubleString.length()-pow_string.length()-1);
        //现在对 表达式进行处理 (转为我们需要的十六进制数)
        String init_dec = StringUtils.doubleHex2Dec(hexDoubleString);
        BigDecimal res = new BigDecimal(Double.parseDouble(init_dec));
        res = res.multiply((new BigDecimal(2).pow(pow)));
        System.out.println(judgeSign + res.toString());
    }

    @org.junit.Test
    public void test13() {
        int x = 1234;
        String s = StringUtils.dec2Hex(x);
        System.out.println("s = " + s);
    }

    @org.junit.Test
    public void test14() {
        String x = "123456.3";
        System.out.println(StringUtils.allDecToHex(x));
    }

    @org.junit.Test
    public void test15() {
        String x = "7BC";
        System.out.println(StringUtils.int_Hex2Dec(x));
    }

    @org.junit.Test
    public void test16() {
        String x = "123";
        System.out.println(StringUtils.small_Hex2Dec(x));
    }

    @org.junit.Test
    public void test17() {
        String x = "7B.1233";
        System.out.println(StringUtils.allHex2Dec(x));
    }

    @org.junit.Test
    public void test18() {
        System.out.println(StringUtils.binary2Dec("1110.110101011111101010110101010101"));
    }

     @org.junit.Test
    public void test19() throws Exception {
         String x = "7B.1";
         System.out.println(StringUtils.hex2Binary(x));
     }

     @org.junit.Test
    public void test20() {
        String x = "1000.111";
         System.out.println(StringUtils.binary2Hex(x));
     }

     @org.junit.Test
    public void test21() throws Exception {
         Operand operand = new Operand();
         operand.setOperandData("101");
         operand.setSystemEnum(SystemEnum.BINARY);
         operand.setNegative(true);
         System.out.println(operand.getBinary());
         System.out.println(Operand.addressOperand(operand,SystemEnum.DEC));
     }

     @org.junit.Test
      public void test22() {
         String x = "1  3 4";
         String[] split = x.split("[ ]{1,2}");
     }

     @org.junit.Test
    public void test23() throws Exception {
         Operand operand = new Operand();
         operand.setOperandData("101.110101");
         operand.setSystemEnum(SystemEnum.BINARY);
         operand.get1SC();
         System.out.println(operand.get1SC());
     }

     @org.junit.Test
    public void test24() throws Exception {
         Operand operand = new Operand();
         operand.setOperandData("101.110101");
         operand.setSystemEnum(SystemEnum.BINARY);
         operand.setNegative(true);
         System.out.println(operand.get2SC());
     }

     @org.junit.Test
    public void test25() {
         System.out.println(3|1);
         System.out.println();
     }
     @org.junit.Test
     public void test26() throws Exception {
         Operand operand = new Operand();
         operand.setOperandData("101");
         operand.setSystemEnum(SystemEnum.BINARY);
         operand.doShf("2");
         System.out.println(operand.doShf("2"));
     }

     @org.junit.Test
    public void test27() {
        int data = Integer.parseInt("-5");
         System.out.println(data);
     }
     @org.junit.Test
     public void test28() {
         System.out.println(Long.parseLong("10101",2));
     }

     @org.junit.Test
    public void test29() {
        String x= " 1 + (3-2) ";
         x = x.replaceAll("\\(","");
         x = x.replaceAll("\\)","");
         System.out.println("x = " + x);
     }

     @org.junit.Test
    public void test30() {
        String x = "1  2  3  1 2 3 4 5 ";
         String[] split = x.split("\\s+");
     }

     @org.junit.Test
    public void test31() {
         String str = "0.00";
         String pattern = "^0\\.0+$";

         Pattern r = Pattern.compile(pattern);
         Matcher m = r.matcher(str);
         System.out.println(m.matches());
     }

     @Test
    public void test0() throws Exception {
        String x = "9999999999.999999999999";
         long start = System.nanoTime();
         String s = StringUtils.allToBinary(x);
         long end = System.nanoTime();
         double cost = (double) (end - start) / 1e6;
         System.out.println(cost);
     }

     @Test
    public void test99() {
        String x = "10000";
         String string = Long.toBinaryString(Double.doubleToLongBits(Double.parseDouble(x)));
         System.out.println(string);
     }



}
