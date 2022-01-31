package ru.prokhorov;

import javax.swing.*;

public class GreetingOfTheWinner extends JFrame{
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 300;
    JLabel greetingLabel = new JLabel();

    public GreetingOfTheWinner(int state){
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setTitle("Greeting!");
        setLocationRelativeTo(this);
        if(state == 1) {
            greetingLabel.setText("HUMAN WINS!");
        }else if(state == 2){
            greetingLabel.setText("AI WINS!");
        }else{
            greetingLabel.setText("DRAW!");
        }
        add(greetingLabel);
        setVisible(true);
    }
}
