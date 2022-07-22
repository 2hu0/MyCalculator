package soft.test;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class doClickTest extends JButton {
    @Test
    public void run(){
        doClickTest test = new doClickTest();
        test.addActionListener(new clicking());


        JFrame frame = new JFrame();
        frame.setBounds(0,0,400,500);
        frame.setLayout(null);
        test.setBounds(100,100,200,200);
        frame.add(test);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //为什么按下空格会有两个22？
        doClickTest test1 = new doClickTest();
        test1.addActionListener(new clicking());
//        test1.addKeyListener(new keyup(test1,10));
        test1.addMouseListener(new click());
        test1.addMouseMotionListener(new click());
        test1.setText("111");



        doClickTest test2 = new doClickTest();
        test2.addActionListener(new clicking());
//        test2.addKeyListener(new keyup(test2,65));
        test2.addMouseListener(new click());
        test2.addMouseMotionListener(new click());
        test2.setText("111");

        JFrame frame = new JFrame();
        frame.setBounds(0,0,500,500);
        frame.setLayout(null);
        test1.setBounds(100,100,100,200);
        test2.setBounds(210,100,100,200);
        frame.add(test1);
        frame.add(test2);

//        frame.addKeyListener(new keyup(test1,10));
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();


        frame.setVisible(true);
    }
}

class clicking extends MouseAdapter implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(22);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(33);
    }
}

class click extends MouseAdapter implements MouseMotionListener{

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(33);
    }
}

class keyup extends KeyAdapter implements KeyListener{

    private JButton button;
    private int val;

    public keyup(JButton button,int val){
        this.button = button;
        this.val = val;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(11);
        if(e.getKeyCode() == val){
            button.doClick();
        }
    }
}