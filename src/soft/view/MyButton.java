package soft.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 * 按钮类，定义了计算器的按钮的基本样式，可以更改颜色，支持渐变色
 */
public class MyButton extends JButton {

    //正常的按钮颜色(支持渐变色，从 BUTTON_COLOR1 渐变到 BUTTON_COLOR2)
    private Color BUTTON_COLOR1 = Color.white;
    private Color BUTTON_COLOR2 = Color.white;

    //按下鼠标后的背景颜色(支持渐变)
    private Color BUTTON_COLOR11 = Color.white;
    private Color BUTTON_COLOR22 = Color.white;

    //字体的默认颜色
    private Color BUTTON_FOREGROUND_COLOR1 = Color.black;

    //按下按钮时字体的颜色
    private Color BUTTON_FOREGROUND_COLOR2 = Color.black;

    //判断是否按下
    private boolean hover;

    //初始化按钮
    public MyButton(String name) {
        //设置按钮文本内容
        setText(name);
        //设置按钮名字
        super.setName(name);
        Init();
    }

    //设置按钮的样式，参数分别是正常样式，按下时样式，字体颜色，注意参数可以给null，但是不能缺省
    public void set(Color color1,Color color2,Color color3) {
        this.BUTTON_COLOR1 = color1 == null ? new Color(221,224,235) : color1;
        this.BUTTON_COLOR2 = BUTTON_COLOR1;
        this.BUTTON_COLOR11 = color2 == null ? new Color(207,210,224) : color2;
        this.BUTTON_COLOR22 = BUTTON_COLOR11;
        this.BUTTON_FOREGROUND_COLOR1 = color3 == null ? Color.black : color3;
        this.BUTTON_FOREGROUND_COLOR2 = BUTTON_FOREGROUND_COLOR1;
    }

    public void Init() {
        //不显示边框
        setBorderPainted(false);
        //字体颜色
        setForeground(BUTTON_FOREGROUND_COLOR1);
        //去除文字边框
        setFocusPainted(false);
        //设置背景透明
        setContentAreaFilled(false);
        //设置字体
        setFont(new Font("微软雅黑",Font.BOLD,18));
        //监听鼠标移入移出事件
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {  //鼠标移动到上面时
                //本计算器hover不需要改字体颜色(改不改都行)
                setForeground(BUTTON_FOREGROUND_COLOR2);
                //如果按钮不可用，不用更改hover颜色
                if(!isEnabled()){
                   return;
                }
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {  //鼠标移开时
                setForeground(BUTTON_FOREGROUND_COLOR1);
                hover = false;
                repaint();
            }
        });
        if(!isEnabled()){
            BUTTON_COLOR1 = new Color(207,210,224);
            BUTTON_COLOR2 = BUTTON_COLOR1;
        }
    }

    @Override
    protected void paintComponent( Graphics g) {
        //创建画笔
        Graphics2D g2d = (Graphics2D) g.create();
        //获取按钮宽高
        int h = getHeight();
        int w = getWidth();
        //设置画笔渲染设置
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //渐变色设置，本计算器没有用到渐变按钮，但是保存了该功能
        GradientPaint p1;
        GradientPaint p2;
        //设置渐变
        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,
                    new Color(100, 100, 100));
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3,
                    new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1,
                    new Color(0, 0, 0));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,
                    h - 3, new Color(0, 0, 0, 50));
        }
        //构造圆角矩形
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 20, 20);
        Shape clip = g2d.getClip();
        g2d.clip(r2d);

        GradientPaint gp = null;

        if(!hover){
            if(isEnabled()){
                gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F, h, BUTTON_COLOR2, true);
            }
            else {
                gp = new GradientPaint(0.0F, 0.0F, new Color(207,210,224), 0.0F, h, new Color(207,210,224), true);
            }
        }else{
            gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR11, 0.0F, h, BUTTON_COLOR22, true);
        }
        //设置背景色
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);



        //原来的笔
        g2d.setClip(clip);
        //画圆角矩形边框
        g2d.setPaint(p1);
//        g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);//没有边框好看点
        g2d.setPaint(p2);
//        g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);

        g2d.dispose();
        super.paintComponent(g);
    }

}