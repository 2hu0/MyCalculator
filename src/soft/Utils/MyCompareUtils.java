package soft.Utils;

import java.util.Objects;

/**
 * 字符串比较工具类
 */
public class MyCompareUtils {

    /**
     * 函数功能：比较目标字符串和任意数量的字符串的相等关系
     * @param str 目标字符串
     * @param args 要比较的字符串
     * @return 比较结果
     */
    public boolean compare(String str, String... args){
        for (int i = 0; i < args.length; i++) {
            if(Objects.equals(str,args[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 函数功能：逐一比较 ch 和 后面的字符的关系，有一个相同就返回true
     * @param ch 目标字符
     * @param args 要比较的字符
     * @return 比较结果
     */
    public boolean compare(char ch, char... args){
        for (int i = 0; i < args.length; i++) {
            if(Objects.equals(ch,args[i])){
                return true;
            }
        }
        return false;
    }
}
