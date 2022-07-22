package soft.log;

import soft.Utils.MyCompareUtils;
import soft.view.Calculator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志记录类：负责记录计算过的式子
 */
public class LogRecord {

    //比较工具类
    private static final MyCompareUtils utils = new MyCompareUtils();

    /**
     * 函数功能：把固定的一行数据写入文件
     * @param str 要写入的数据
     * @throws IOException 文件打开失败，读取失败都会抛出异常
     */
    public static void write(String str) throws IOException {
//        File file = new File("D:\\test.txt");

        File file = new File(Calculator.class.getClassLoader().getResource("soft/History/history.txt").getFile());

        //创建输出流
        BufferedReader reader = new BufferedReader(new FileReader(file));
        //创建时间格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        //读取所有记录
        List<String> list = new ArrayList<>();
        String readStr;
        while((readStr = reader.readLine()) != null){
            list.add(readStr + "\n");
        }
        reader.close();
        //把当前记录格式化后插到最前面
        list.add(0,appendStrLength(str,161) + "\t" + dateFormat.format(new Date()) + "\n");
        //默认只能保存20条记录
        if(list.size() > 20){
            list.remove(list.size() - 1);
        }
        //创建输入流，写会数据进文件
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        for(String val : list){
            writer.write(val);
        }
        writer.close();
    }

    /**
     * 处理字符串到一定宽度
     * @param str 要处理的字符串
     * @param length 需要达到的宽度
     * @return 处理结果
     */
    private static String appendStrLength(String str , int length){
        if(str == null){
            str = "";
        }

        double strLen = 0;
        for(int i = 0 ; i<str.length(); i++){
            strLen += codeLength(str.charAt(i));
        }
        if(strLen>=length){
            return str;
        }
        //System.out.println(strLen);
        int remain = (int)(length - strLen) + 1;//计算所需补充空格长度
        for(int i =0 ; i< remain ;i++){
            str = str + " ";
        }
        return str;
    }

    /**
     * 函数功能：返回每一个有可能出现的字符在txt文件中的宽大
     * @param ch 要查询宽度的字符
     * @return 宽度
     */
    private static double codeLength(char ch){
        if(utils.compare(ch, '.', '\'')){
            return 0.82;
        }else if(utils.compare(ch, 'A', 'C')){
            return 2.4;
        }else if(utils.compare(ch, 'f')){
            return 1.18;
        }else if(utils.compare(ch, 'r', 't', 'J')){
            return 1.22;
        } else if(utils.compare(ch, '(', ')')){
            return 1.20;
        }else if(utils.compare(ch, 'D', '>', '+', '^', '=', 'G', 'U')){
            return 2.5;
        }else if(Character.isDigit(ch) || utils.compare(ch, 'b', 'g', 'h', 'n','o', 'p', 'q', 'u', 'P', 'X', 'Y', 'Z', 'B', 'K')){
            return 2;
        }else if(utils.compare(ch, 'S', 'T')){
            return 1.95;
        }else if(utils.compare(ch, 'E', 'F', 'v')){
            return 1.83;
        }else if(utils.compare(ch, '-', '*', '?', 's')){
            return 1.5;
        }else if(utils.compare(ch, '/')){
            return 1.4;
        }else if(utils.compare(ch, '@')){
            return 3.75;
        }else if(utils.compare(ch, '&')){
            return 3;
        }else if(utils.compare(ch, 'm')){
            return 3.1;
        }else if(utils.compare(ch, 'A')){
            return 2.4;
        }else if(utils.compare(ch, 'a', 'e', 'k', 'y')){
            return 1.8;
        }else if(utils.compare(ch, 'c', 'x', 'z', 'L')){
            return 1.67;
        }else if(utils.compare(ch, 'd')){
            return 1.33;
        }else if(utils.compare(ch, 'w', 'H', 'N', 'O', 'Q')){
            return 2.67;
        }else if(utils.compare(ch, 'M')){
            return 3.5;
        } else if(utils.compare(ch, 'R', 'V')){
            return 2.33;
        }else if(utils.compare(ch, 'W')){
            return 2.70;
        }else if(utils.compare(ch, '\t')){
            return 10;
        }
        return 1;
    }

}
