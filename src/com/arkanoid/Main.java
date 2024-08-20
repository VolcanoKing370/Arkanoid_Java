package com.arkanoid;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello World!");
        JFrame jframe = new JFrame();

        GameLogic logic = new GameLogic();
        jframe.setBounds(150, 50, 640, 480);
        jframe.setResizable(false);
        jframe.setTitle("Arkanoid - Java");
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        jframe.add(logic);
    }
}
