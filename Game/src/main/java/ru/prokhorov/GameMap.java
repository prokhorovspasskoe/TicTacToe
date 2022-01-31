package ru.prokhorov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameMap extends JPanel {
    public static final int MODE_VS_AI = 0;
    public static final int MODE_VS_HUMAN = 1;
    public static final int DOT_AI = 2;
    public static final int DOT_HUMAN = 1;
    public static final int DOT_EMPTY = 0;
    private static final Random random = new Random();
    private static final int DOT_PADDING = 7;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private int[][] field;
    private int fieldSizeX;
    private int fieldSizeY;
    private int cellWidth;
    private int cellHeight;
    private boolean isGameOver;
    private boolean isInitialized;
    private int gameMode;
    private int playerNumTurn;
    private int winLength;
    private int stateGameOver;

    public GameMap(){
        isInitialized = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
    }

    private boolean checkLine(int x, int y, int incrementX, int incrementY, int len, int dot){
        int endXLine = x + (len - 1) * incrementX;
        int endYLine = y + (len - 1) * incrementY;
        if(!isCellValid(endYLine, endXLine)) return false;
        for (int i = 0; i < len; i++) {
            if(field[y + i * incrementY][x + i * incrementX] != dot) return false;
        }
        return true;
    }

    private boolean checkWin(int dot, int length){
        for(int y = 0; y < fieldSizeY; y++){
            for(int x = 0; x < fieldSizeX; x++){
                if(checkLine(x, y, 1, 0, length, dot)) return true;
                if(checkLine(x, y, 1, 1, length, dot)) return true;
                if(checkLine(x, y, 0, 1, length, dot)) return true;
                if(checkLine(x, y, 1, -1, length, dot)) return true;
            }
        }
        return false;
    }

    private boolean checkDraw(){
        for(int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++) {
                if(isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    private boolean playerTurn(MouseEvent event, int dot){
        int cellX = event.getX() / cellWidth;
        int cellY = event.getY() / cellHeight;
        if(!isCellValid(cellX, cellY) || !isCellEmpty(cellX, cellY)) return false;
        field[cellY][cellX] = dot;
        repaint();
        playerNumTurn = playerNumTurn == 1 ? 2 : 1;
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if(!isInitialized) return;
        int width = getWidth();
        int height = getHeight();
        cellHeight = height / fieldSizeY;
        cellWidth = width / fieldSizeX;
        g.setColor(Color.BLACK);

        for(int i = 0; i < fieldSizeY; i++){
            int y = i * cellHeight;
            g.drawLine(0, y, width, y);
        }
        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, height);
        }
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if(isCellEmpty(x, y)) continue;
                if(field[y][x] == DOT_HUMAN){
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                }else if(field[y][x] == DOT_AI){
                    g.setColor(Color.RED);
                    g.fillRect(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                }
            }
        }
        if(isGameOver){
            GreetingOfTheWinner greetingOfTheWinner = new GreetingOfTheWinner(stateGameOver);
            greetingOfTheWinner.setVisible(true);
        }
    }
    private boolean gameCheck(int dot, int stateGameOver){
        if(checkWin(dot, winLength)){
            this.stateGameOver = stateGameOver;
            isGameOver = true;
            repaint();
            return true;
        }
        if(checkDraw()){
            this.stateGameOver = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    private boolean scanField(int dot, int length){
        for(int y = 0; y < fieldSizeY; y++){
            for(int x = 0; x < fieldSizeX; x++){
                if(isCellEmpty(x, y)){
                    field[y][x] = dot;
                    if(checkWin(dot, length)){
                        if(dot == DOT_AI) return true;
                        if(dot == DOT_HUMAN){
                            field[y][x] = DOT_AI;
                            return true;
                        }
                    }
                    field[y][x] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    private void aiTurn(){
        if(scanField(DOT_AI, winLength)) return;
        if(scanField(DOT_HUMAN, winLength)) return;
        if(scanField(DOT_AI, winLength - 1)) return;
        if(scanField(DOT_HUMAN, winLength - 1)) return;
        if(scanField(DOT_AI, winLength - 2)) return;
        if(scanField(DOT_HUMAN, winLength - 2)) return;
        aiTurnEasy();
    }

    private void aiTurnEasy(){
        int x, y;
        do{
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }while(!isCellEmpty(x, y));
        field[y][x] = DOT_AI;
    }

    private boolean isCellValid(int x, int y){
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY;
    }

    private boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    private void update(MouseEvent e) {
        if(isGameOver || !isInitialized) return;
        int dot = gameMode == MODE_VS_AI ? DOT_HUMAN : DOT_AI;
        if(!playerTurn(e, dot)) return;
        if(gameCheck(dot, STATE_WIN_HUMAN)) return;
        boolean b = SettingsWindow.getThePowerGame();
        if(gameMode == MODE_VS_AI && b == true){
            aiTurn();
            repaint();
            if(gameCheck(DOT_AI, STATE_WIN_AI)) return;
        }
        if(gameMode == MODE_VS_AI && b == false){
            aiTurnEasy();
            repaint();
            if(gameCheck(DOT_AI, STATE_WIN_AI)) return;
        }
    }

    public void startNewGame(int gameMode, int fieldSize, int cellLength) {
        this.gameMode = gameMode;
        this.fieldSizeX = this.fieldSizeY = fieldSize;
        this.winLength = cellLength;
        this.playerNumTurn = 1;
        field = new int[fieldSizeY][fieldSizeX];
        isInitialized = true;
        isGameOver = false;
        repaint();
    }
}
