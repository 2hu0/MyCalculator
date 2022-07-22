package soft.control;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *按键监听器(已弃用)，监听键盘不同按键的按下和弹起事件，点击相对应的按钮，期望实现键盘输入的功能
 */
public class KeyUp extends KeyAdapter implements KeyListener {
    //计算器对象
    private final JButton button;

    private boolean throttle = false;

    public KeyUp(JButton button){
        this.button = button;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!throttle){
            if(e.getKeyCode() == 65){
                button.doClick();
            }
            if(e.getKeyCode() == 66){

            }
            if(e.getKeyCode() == 67){

            }
            if(e.getKeyCode() == 68){

            }
            if(e.getKeyCode() == 69){

            }
            if(e.getKeyCode() == 70){

            }
            if(e.getKeyCode() == 10){
                button.doClick();
                System.out.println(11);
            }
            if(e.getKeyCode() == 8){

            }
        }
        throttle = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throttle = false;
    }
}
