package soft.test;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class LinkLabel extends JLabel {

    public static void main(String[] args) {

        JFrame jf = new JFrame("一个超链接实现的例子");

        jf.setBounds(0,0,400,400);
        jf.setLayout(null);
        jf.setBackground(null);

        JLabel label = new JLabel("这是下划线");
        label.setBackground(Color.PINK);
        label.setOpaque(true);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setForeground(Color.black);
        label.setBounds(0,0,200,200);

        label.addMouseListener(new over(label));
        label.addMouseMotionListener(new over(label));

        jf.add(label);
        jf.setVisible(true);

    }

}


class over extends MouseAdapter implements MouseMotionListener{
    private JLabel label;
    public over(JLabel label){
        this.label = label;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        label.setText("<HTML><U>这是下划线</U></HTML>");
        label.setForeground(Color.BLUE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        label.setText("这是下划线");
        label.setForeground(Color.black);
    }
}
