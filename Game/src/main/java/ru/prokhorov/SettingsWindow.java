package ru.prokhorov;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JFrame {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 550;
    private static final int MIN_CELL_LEN = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 9;
    private static final String FIELD_SIZE_PREFIX = "Field size: ";
    private static final String WIN_LENGTH = "Ceil length: ";
    private static final String POWER_AI_EASY = "Power AI easy";
    private static final String POWER_AI = "Power AI hard";
    private static boolean thePowerOfTheGame;

    private JSlider sliderCeilLength;
    private JSlider sliderFieldSize;
    private JRadioButton humanVsAI;
    private JRadioButton humanVsHuman;
    private JRadioButton powerAIEasy;
    private JRadioButton powerAIHard;

    private GameWindow gameWindow;

    public SettingsWindow(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(this.gameWindow);
        setResizable(false);
        setTitle("Settings game");
        setLayout(new GridLayout(13, 1));

        addChooseGameMode();
        addSetFieldSize();

        JButton buttonStart = new JButton("Start new game");

        buttonStart.addActionListener(e ->{
            submitSettings(gameWindow);
        });
        add(buttonStart);
    }

    private void submitSettings(GameWindow gameWindow) {
        int gameMode;
        if(humanVsAI.isSelected()){
            gameMode = GameMap.MODE_VS_AI;
        }else{
            gameMode = GameMap.MODE_VS_HUMAN;
        }

        if(powerAIEasy.isSelected()){
            thePowerOfTheGame = false;
        }else{
            thePowerOfTheGame = true;
        }

        int fieldSize = sliderFieldSize.getValue();
        int cellLength = sliderCeilLength.getValue();
        gameWindow.startGame(gameMode, fieldSize, cellLength);
        setVisible(false);
    }

    private void addSetFieldSize() {
        JLabel labelFieldSize = new JLabel(FIELD_SIZE_PREFIX + MAX_FIELD_SIZE);
        JLabel labelCellLength = new JLabel(WIN_LENGTH + MIN_CELL_LEN);
        sliderFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        sliderCeilLength = new JSlider(MIN_CELL_LEN, MAX_FIELD_SIZE, MIN_FIELD_SIZE);

        sliderFieldSize.addChangeListener(e -> {
                    labelFieldSize.setText(String.valueOf(FIELD_SIZE_PREFIX + sliderFieldSize.getValue()));
                    int currentValue = sliderFieldSize.getValue();
                    sliderCeilLength.setMaximum(currentValue);
                }
        );

        sliderCeilLength.addChangeListener(e -> labelCellLength.setText(WIN_LENGTH + sliderCeilLength.getValue()));
        add(new JLabel("Choose field size: "));
        add(labelFieldSize);
        add(sliderFieldSize);
        add(new JLabel("Choose cell length: "));
        add(labelCellLength);
        add(sliderCeilLength);
    }

    private void addChooseGameMode() {

        add(new JLabel("Choose game mode:"));
        humanVsAI = new JRadioButton("Human vs AI", true);
        humanVsHuman = new JRadioButton("Human vs Human");
        powerAIEasy = new JRadioButton(POWER_AI_EASY);
        powerAIHard = new JRadioButton((POWER_AI));
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(humanVsAI);
        gameMode.add(humanVsHuman);
        add(humanVsAI);
        add(humanVsHuman);
        add(new JLabel("Choose power mode:"));
        ButtonGroup powerMode = new ButtonGroup();
        powerMode.add(powerAIEasy);
        powerMode.add(powerAIHard);
        add(powerAIEasy);
        add(powerAIHard);
    }

    public static boolean getThePowerGame(){
        return thePowerOfTheGame;
    }
}
