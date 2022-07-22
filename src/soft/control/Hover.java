package soft.control;

import soft.view.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

public class Hover extends MouseAdapter implements MouseMotionListener {

    private JLabel label;

    public Hover(JLabel label){
        this.label = label;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        label.setForeground(new Color(0,174,236));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        label.setForeground(Color.gray);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //点击鼠标打开历史面板
        File file = new File(Calculator.class.getClassLoader().getResource("soft/History/history.txt").getFile());
        System.out.println(11);
        //如果文件打不开
        if(!Desktop.isDesktopSupported()){
            label.setText("无法打开文件");
            label.setForeground(Color.gray);
            label.removeMouseListener(this);
            label.removeMouseListener(this);
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("文件打开异常！");
            }
        }
    }
}
