package soft.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 * 面板类，绘制一个圆角的面板
 */
public class MyPanel extends JPanel {

    public MyPanel () {
        super();
        //设置不透明(只有不透明才可以显示颜色)
        setOpaque(true);
        setBounds(25,15,500,120);
        setBackground(Color.white);
    }

    /**
     * 重写该函数可以实现jpanel组件样式的自定义
     * @param g 画笔对象
     */
    @Override
    public void paint( Graphics g) {
        //圆角半径
        int fieldX = 0;
        int fieldY = 0;
        int fieldWeight = getSize().width;
        int fieldHeight = getSize().height;
        //画圆角矩形
        RoundRectangle2D rect = new RoundRectangle2D.Double(fieldX, fieldY, fieldWeight, fieldHeight, 20, 20);
        g.setClip(rect);
        super.paint(g);
    }

}