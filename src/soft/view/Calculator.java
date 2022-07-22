package soft.view;

import soft.Utils.ExpressionStatus;
import soft.Utils.MyCompareUtils;
import soft.control.Clicked;
import soft.control.Hover;
import soft.log.LogRecord;
import soft.service.CountObject;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 计算机类：可以创建并显示一个计算机窗体
 * 没有继承JFrame对象，所有要在该对象里面显示窗口，Main函数new出来的对象和JFrame没有关系
 */
public class Calculator {
    //主窗口
    private final JFrame jf = new JFrame("Calculator");
    //主页面
    private final JPanel window = new JPanel();
    //所有按钮
    private final MyButton[] button = new MyButton[34];
    //历史记录显示
    private final JLabel his = new JLabel("");
    //计算结果显示
    private final JLabel ans = new JLabel("0");
    //进制显示
    private final JLabel msg = new JLabel("HEX");
    //历史面板
    private final JLabel log = new JLabel("打开历史面板",JLabel.CENTER);
    //比较工具
    private final MyCompareUtils utils = new MyCompareUtils();
    //左右括号计数器
    private int count = 0;
    //记录输入信息的数组
    private final List<String> res = new ArrayList<>();
    //标志位，用来辨别当前res是不是结果
    public boolean isResult = true;
    //标志位，用来标记是否禁用小数点
    public boolean hasPoint = false;
    //标记位,标记当前是否发生了错误
    public boolean isError = false;
    //标记位，记录当前处于说明进制，默认是true，十进制
    public boolean system = true;
    //节流阀，代表目前有没有按键被按下(按下但是没有弹起)(已弃用)
    //public boolean throttle = false;
    //标记位，标记当前0是否可用
    public boolean zerolegal;
    //标记位，标记当前结果是不是二进制数
    public boolean isBinary = false;

    //构造函数
    public Calculator(){
        //初始化界面
        init();
        //设置计算器初始值
        setOrigin();
        //更新约束信息
        update();
    }

    public MyButton[] getButtons(){
        return button;
    }

    public JLabel getHis() {
        return his;
    }

    public JLabel getAns() {
        return ans;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 函数功能：获取答案栏的文本字体大小
     * @return 结果
     */
    public int getAnsFontSize(){
        return ans.getFont().getSize();
    }

    /**
     * 函数功能：获取答案栏文本长度
     * @return 结果
     */
    public int getAnsTextSize(){
        return ans.getFontMetrics(ans.getFont()).stringWidth(ans.getText());
    }

    /**
     * 函数功能：获取历史栏文本长度
     * @return 结果
     */
    public int getHisTextSize(){
        return his.getFontMetrics(his.getFont()).stringWidth(his.getText());
    }

    /**
     * 函数功能：设置答案栏字体样式
     * @param style 字体风格
     * @param size 字体大小
     */
    public void setAnsFont(int style,int size){
        ans.setFont(new Font("微软雅黑",style,size));
    }

    /**
     * 函数功能：设置答案显示内容
     * @param str 要显示的内容
     */
    public void setAnsText(String str){
        ans.setText(str);
    }

    /**
     * 函数功能：设置历史记录显示内容
     * @param str 要显示的内容
     */
    public void setHisText(String str){
        his.setText(str);
    }

    /**
     * 函数功能：判断某个字符串是不是操作数类型
     * @param val 需要判断的字符串
     * @return 结果
     */
    public boolean isNumber(String val){
        return (val.length() == 1 && Character.isLetter(val.charAt(0))) || (Character.isDigit(val.charAt(0)));
    }

    /**
     * 函数功能：设置计算器初始值
     */
    public void setOrigin(){
        res.clear();
        Collections.addAll(res,"DEC","0");
        isResult = true;
        count = 0;
    }

    /**
     * 函数功能：初始化窗体
     */
    private void init(){
        //初始化主页面
        initWindow();
        //主窗体上区域
        JPanel top = new JPanel();
        top.setBounds(0,0,550,150);
        //设置背景透明
        top.setBackground(null);
//        top.setBackground(Color.red);
        top.setLayout(null);
        //主窗体下区域
        JPanel bottom = new JPanel();
        bottom.setBounds(0,150,550,460);
        //设置背景透明
        bottom.setBackground(null);
//        bottom.setBackground(Color.green);
        //初始化历史面板
        initLog();

        window.add(top);
        window.add(bottom);
        window.add(log,BorderLayout.CENTER);

        //设置自定义圆角显示框
        MyPanel show = new MyPanel();
        show.setLayout(null);
        top.add(show);

        //设置显示答案label和历史答案label和进制信息label
        his.setOpaque(true);
        //设置背景透明
        his.setBackground(null);
//        his.setBackground(Color.ORANGE);
        his.setBounds(10,0,440,30);
        his.setFont(new Font("微软雅黑",Font.PLAIN,13));
        ans.setOpaque(true);
//        System.out.println(his.getFont().getSize());
        //设置背景透明
        ans.setBackground(null);
//        ans.setBackground(Color.YELLOW);
        ans.setBounds(10,30,480,90);
        ans.setFont(new Font("微软雅黑",Font.BOLD,50));
        msg.setOpaque(true);
        //设置背景透明
        msg.setBackground(null);
//        msg.setBackground(Color.pink);
        msg.setBounds(450,0,40,30);
        msg.setFont(new Font("微软雅黑",Font.PLAIN,13));
        show.add(his);
        show.add(ans);
        show.add(msg);

        //下区域设置居中的Panel
        bottom.setLayout(null);
        JPanel center = new JPanel();
        //设置背景透明
        center.setBackground(null);
//        center.setBackground(Color.BLUE);
        center.setBounds(25,0,500,455);
        bottom.add(center);

        //居中的panel设置绝对布局，并添加按钮
        center.setLayout(null);
        //初始化按钮
        initButton(center);
        //显示
        jf.setVisible(true);
        //设置关闭按钮
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * 函数功能：初始化历史记录面板
     */
    private void initLog(){
        //历史面板样式设置
        log.setBounds(245,610,80,40);
        log.setForeground(Color.gray);
        log.setBackground(null);
        log.setFont(new Font("微软雅黑",Font.PLAIN,12));
        log.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //面板鼠标悬浮样式和点击操作
        log.addMouseListener(new Hover(log));
        log.addMouseMotionListener(new Hover(log));
    }
    /**
     * 函数功能：初始化主页面
     */
    private void initWindow(){
        //设置主窗体布局:绝对布局
        jf.setLayout(null);
        //设置主窗体位置
        jf.setBounds(0,0,564,687);
        //不可拉伸
        jf.setResizable(false);
        //居中显示
        jf.setLocationRelativeTo(null);

        //jf好像不能直接改背景颜色，给他加一个panel
        //jf.getContentPane().setBackground(new Color(234,236,243));

        //主页面

        window.setBackground(new Color(234,236,243));
        window.setLayout(null);
        window.setBounds(0,0,564,687);
        jf.add(window);

        //=================================键盘输入功能==========================

        //通过测试可知，doClick()方法需要监听器实现 ActionListener 接口，
        // 同时想要点击某个按钮，需要先让该按钮获得焦点才能点击，但是由于按钮内部有文字，使按钮获得焦点非常不美观，
        // 所以本计算器禁用所有按钮的获得焦点功能，因此无法实现按键输入功能
        //jf.addKeyListener(new KeyUp(this));
    }

    /**
     * 函数功能：初始化所有按键加入center
     * @param center 是装按钮的面板
     */
    private void initButton(JPanel center){
        button[0] = new MyButton("DEC");
        button[1] = new MyButton("HEX");
        button[2] = new MyButton("1'sc");
        button[3] = new MyButton("2'sc");
        button[4] = new MyButton("AC");
        button[5] = new MyButton("OR");
        button[6] = new MyButton("AND");
        button[7] = new MyButton("XOR");
        button[8] = new MyButton("SHF");
        button[9] = new MyButton("CE");
        button[10] = new MyButton("E");
        button[11] = new MyButton("F");
        button[12] = new MyButton("(");
        button[13] = new MyButton(")");
        button[14] = new MyButton("/");
        button[15] = new MyButton("D");
        button[16] = new MyButton("7");
        button[17] = new MyButton("8");
        button[18] = new MyButton("9");
        button[19] = new MyButton("*");
        button[20] = new MyButton("C");
        button[21] = new MyButton("4");
        button[22] = new MyButton("5");
        button[23] = new MyButton("6");
        button[24] = new MyButton("-");
        button[25] = new MyButton("B");
        button[26] = new MyButton("1");
        button[27] = new MyButton("2");
        button[28] = new MyButton("3");
        button[29] = new MyButton("+");
        button[30] = new MyButton("A");
        button[31] = new MyButton("0");
        button[32] = new MyButton(".");
        button[33] = new MyButton("=");

        //设置按钮宽高
        int width = 92;
        int height = 56;
        //加入所有按钮
        for(int i=0;i<button.length;i++){
            center.add(button[i]);
            if(i == 33){
                //等号
                button[i].setBounds(306,396,194,56);
                button[i].setFont(new Font("微软雅黑",Font.BOLD,20));
                button[i].setForeground(Color.white);
                button[i].set(new Color(79,110,242),new Color(74,104,226),Color.white);
                button[i].setFocusable(false);
                button[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
                continue;
            }else if(i == 4){
                //AC
                button[i].set(new Color(255,190,71),new Color(236,179,77),Color.white);
                button[i].setForeground(Color.white);
            }else if(i == 16 || i == 17 || i == 18 || i == 21 || i == 22 || i == 23 || i == 26 || i == 27 || i == 28 || i == 31 || i == 32){
                //数字
                button[i].set(Color.white,new Color(223,224,229),Color.black);
            }else{
                //其他符号
                button[i].set(new Color(221,224,235),new Color(207,210,224),Color.black);
            }
            button[i].setBounds((10+width)*(i%5),(10+height)*(i/5),width,height);
            button[i].setFocusable(false);
            button[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        //绑定事件函数
        for (MyButton myButton : button) {
            myButton.addMouseListener(new Clicked(this, myButton, res));
            myButton.addMouseMotionListener(new Clicked(this, myButton, res));
        }
    }

    /**
     * 更新全局按钮(主要是进制约束，错误约束，括号约束，小数点约束)
     */
    public void update(){
        //全局刷新按钮
        for (MyButton myButton : button) {
            myButton.setEnabled(true);
        }
        //恢复当前进制
        if(system){
            button[0].setEnabled(false);
        }
        else{
            button[1].setEnabled(false);
        }
        //不同进制下禁用不同按钮
        if(!button[0].isEnabled()){
            button[10].setEnabled(false);
            button[11].setEnabled(false);
            button[15].setEnabled(false);
            button[20].setEnabled(false);
            button[25].setEnabled(false);
            button[30].setEnabled(false);
            msg.setText("DEC");
        }else{
            button[10].setEnabled(true);
            button[11].setEnabled(true);
            button[15].setEnabled(true);
            button[20].setEnabled(true);
            button[25].setEnabled(true);
            button[30].setEnabled(true);
            msg.setText("HEX");
        }
        //设置右括号是否禁用
        button[13].setEnabled(count != 0);
        //设置小数点禁用
        button[32].setEnabled(!hasPoint);
        //设置全局禁用
        if(isError){
            for (int i = 0; i < button.length; i++) {
                if(i == 4){
                    continue;
                }
                button[i].setEnabled(false);
            }
        }
        if(isResult || !zerolegal){
            button[31].setEnabled(false);
        }
        if(isBinary && isResult){
            button[2].setEnabled(false);
            button[3].setEnabled(false);
        }
    }

    /**
     * 计算要展示的字符串
     * @param res 记录输入的数组
     * @return 显示在标签的表达式
     */
    public String displayExpression(List<String> res){
        //记录结果
        StringBuilder buffer = new StringBuilder();
        //遍历整个按键数组,拼接有用信息
        for(int i = 0;i < res.size();i++){
            //因为是显示在屏幕的字符串，不需要包含进制信息
            if(utils.compare(res.get(i), "DEC", "HEX","BIN")){
                continue;
            }
            //带字母的运算符前后要包含空格
            if(utils.compare(res.get(i), "XOR" ,"OR", "AND", "SHF")) {
                //该运算符处于表达式第一位，最后一位，中间三种情况
                if (i == 0) {
                    buffer.append(res.get(i));
                    buffer.append(" ");
                } else if (i == res.size() - 1) {
                    buffer.append(" ");
                    buffer.append(res.get(i));
                } else {
                    buffer.append(" ");
                    buffer.append(res.get(i));
                    buffer.append(" ");
                }
            }
            //其他按键正常拼接就行了
            else{
                buffer.append(res.get(i));
            }

        }
        //返回结果
        return buffer.toString();
    }

    /**
     * 计算要进行运算的表达式
     * @param res 记录输入的数组
     * @param flag 是否要去除进制信息
     * @param change 是否要替换运算符
     * @return 调用工具计算的表达式
     */
    public String calExpression(List<String> res, boolean flag, boolean change){
        //记录式子的结果
        StringBuilder buffer = new StringBuilder();
        //同样遍历整个按键数组
        for(int i = 0;i < res.size();i++){
            if(flag && (utils.compare(res.get(i), "DEC", "HEX"))){
                continue;
            }
            //把需要的运算符转化为计算对象能识别的运算符
            if(change && Objects.equals(res.get(i), "OR")){
                res.set(i,"|");
            } else if(change && Objects.equals(res.get(i), "AND")){
                res.set(i,"&");
            }else if(change && Objects.equals(res.get(i), "SHF")){
                res.set(i,"@");
            }else if(change && Objects.equals(res.get(i), "XOR")){
                res.set(i,"^");
            }else if(change && Objects.equals(res.get(i), "1'sc")){
                res.set(i,"1'SC");
            }else if(change && Objects.equals(res.get(i), "2'sc")){
                res.set(i,"2'SC");
            }

            //如果是等号，先不拼接，后面再处理
            if(Objects.equals(res.get(i), "=")){
                break;
            }
            //同样运算符要前后空格
            if(utils.compare(res.get(i), "+", "-", "*", "/", "OR", "AND", "XOR", "SHF", "(", ")","|","&","@","^","1'SC","2'SC")) {
                //说明这个符号是负号
                if(utils.compare(res.get(i), "-") && utils.compare(res.get(i - 1), "DEC", "HEX")
                        && (isNumber(res.get(i + 1)) || utils.compare(res.get(i + 1), "("))) {
                    buffer.append(res.get(i));
                    continue;
                }
                //该运算符处于表达式第一位，最后一位，中间三种情况
                if (i == 0) {
                    buffer.append(res.get(i));
                    buffer.append(" ");
                } else if (i == res.size() - 1) {
                    buffer.append(" ");
                    buffer.append(res.get(i));
                } else {
                    buffer.append(" ");
                    buffer.append(res.get(i));
                    buffer.append(" ");
                }
            //如果是小数点，根据它前后是否是操作数补位(补的是0)
            } else if(utils.compare(res.get(i), ".")){
                //小数点不可能在第一位（因为在输入小数点的时候前面会补0），但是有可能在最后一位（好像也不可能最后一位，后面一定有等号）
                if(i != (res.size() - 1)){
                    //如果后面是操作数但是前面不是操作数
                    if(isNumber(res.get(i + 1)) && !isNumber(res.get(i - 1))){
                        buffer.append("0");
                        buffer.append(res.get(i));
                    }
                    //如果前面是操作数但是后面不是操作数
                    else if(isNumber(res.get(i - 1)) && !isNumber(res.get(i + 1))){
                        buffer.append(res.get(i));
                        buffer.append("0");
                    }
                    //如果左右都不是操作数，式子出错，不需要补0,实际上前面因为把这种情况避免了，但是为了避免出错还是保留这个
                    else{
                        buffer.append(res.get(i));
                    }
                }
                //小数点在最后一位？
                else{
                    buffer.append(res.get(i));
                }
            }
            //除了操作符和小数点，其他正常拼接即可
            else{
                buffer.append(res.get(i));
            }
        }

        //处理补足最后一个操作数和右括号的问题，表达式完整才需要补齐，为什么不要长度判断？因为最短的表达式长度是3 [( ) =] 或 [DEC 0 =]
        if(Objects.equals(res.get(res.size() - 1), "=")){
            //如果结尾是等号，最后一位操作数缺失，要根据符号补0或1
            if(utils.compare(res.get(res.size() - 3), "+", "-")){
                buffer.append("DEC0 ");
            }
            if(utils.compare(res.get(res.size() - 3), "*", "/")){
                buffer.append("DEC1 ");
            }
            //补足缺失的右括号
            buffer.append(") ".repeat(Math.max(0, count)));
            //再加上等号
            buffer.append("=");
        }
        //返回结果
        return buffer.toString();
    }

    public void resultChange(String expression){
        //TODO 计算表达式结果
        String targetSystem = system ? "DEC" : "HEX";
        String result = "";
        try {
            result = CountObject.doCalculate(targetSystem, expression);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //维护标志位
        res.clear();
        isResult = true;
        hasPoint = false;
        count = 0;
        setAnsFont(Font.BOLD,50);

        //如果结果太长，只截取前25位防止溢出
        String val = result.substring(0, Math.min(25,result.length()));
        //处理返回的结果
        if(!Objects.equals(result, ExpressionStatus.ZERO_ERROR)
                && !Objects.equals(result, ExpressionStatus.FORMAT_ERROR)
                && expression.charAt(expression.length() - 1) == '='){
            //该结果出错了
            res.add(targetSystem);
            Collections.addAll(res,val.split(""));
            isBinary = false;
        }else if(expression.charAt(expression.length() - 1) != '='){
            //改结果是反码补码运算返回的
            isBinary = true;
            res.add("BIN");
            Collections.addAll(res,val.split(""));
            //处理表达式让其写入日志美观
            expression += "=";
        } else{
            //该结果是等式运算返回的
            res.add(result);
            isError = true;
        }

        //把当前结果写入日志！
        try {
            LogRecord.write(expression + "> " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
