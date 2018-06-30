package com.school.ui;

import com.school.controller.ConnectFourController;
import com.school.interfaces.BoardListener;
import com.school.models.Token;
import com.school.models.Token.TokenType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class View extends JFrame implements BoardListener {

    private static final long serialVersionUID = 1L;

    private final ConnectFourController controller;
    
    private JButton[] controlButtons;

    private MyImageContainer[][] placeHolders;
    private View instance = this;

    private final JTextField message = new JTextField(20);
    private final JPanel centerPane = new JPanel();

    public View(ConnectFourController controller, int nbRows, int nbColumns) {
        this.controller = controller;
        
        this.setTitle("Connect4");

        this.configureWindow();

        this.setLayout(new BorderLayout());
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        panelNorth.add(this.message);
        this.message.setEditable(false);
        this.message.setText("Player 1 Turn");
        this.add(panelNorth, BorderLayout.NORTH);
        this.createMenu();
        this.setVisible(true);
        
        this.initBoard(nbRows, nbColumns);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartMenuItem = new JMenuItem("Restart");
        restartMenuItem.addActionListener(new RestartActionHandler());
        JMenuItem giveUpMenuItem = new JMenuItem("Give up");
        giveUpMenuItem.addActionListener(new GiveUpActionHandler());
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new QuitActionHandler());
        gameMenu.add(restartMenuItem);  
        gameMenu.add(giveUpMenuItem);
        gameMenu.add(quitMenuItem);
        menuBar.add(gameMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new AboutActionHandler());
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        this.setJMenuBar(menuBar);
    }

    private void initBoard(int nbRows, int nbColumns) {
        this.centerPane.removeAll();
        this.placeHolders = new MyImageContainer[nbColumns][nbRows];
        this.controlButtons = new JButton[nbColumns];

        centerPane.setLayout(new GridLayout(nbRows + 1, nbColumns));

        for (int i = 0; i < nbColumns; i++) {
            JButton button = new JButton("T");
            this.controlButtons[i] = button;
            button.addActionListener(new ButtonHandler(i));
            centerPane.add(button);
        }

        for (int row = 0; row < nbRows; row++) {
            for (int column = 0; column < nbColumns; column++) {
                MyImageContainer button = new MyImageContainer();
                button.setOpaque(true);
                placeHolders[column][row] = button;
                centerPane.add(button);
            }
        }
        this.add(centerPane, BorderLayout.CENTER);
        this.revalidate();
    }
    
    private void defineTurn(Token.TokenType playerTurn){
        if(!message.getText().contains("won") && !message.getText().contains("Game")){
            if(playerTurn == TokenType.PLAYER1){
                message.setText("Player 1 Turn");
            }
            else {
                message.setText("Player 2 Turn");
            }
        }
    }
    
    private void resetGame(){
        message.setText("Welcome again!");
        for(int i = 0; i < placeHolders.length; i++){
            for(int j = 0; j < placeHolders[i].length; j++){
                placeHolders[i][j].setImageIcon(null);
            }
        }
        
        for(int i = 0; i < controlButtons.length; i++){
            controlButtons[i].setEnabled(true);
        }
    }
    
    
    private void endGame(){
        for(int i = 0; i < controlButtons.length; i++){
            controlButtons[i].setEnabled(false);
        }
    } 

    private void configureWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(((screenSize.width * 3) / 6), ((screenSize.height * 4) / 7));
        setLocation(((screenSize.width - getWidth()) / 2), ((screenSize.height - getHeight()) / 2));
    }

    @Override
    public void onTokenAdded(Token token) {
        System.out.println("On token added.");
        this.placeHolders[token.getPosition().x][token.getPosition().y].setImageIcon(token.getImage());
        defineTurn(token.getOppositeType());
    }

    @Override
    public void onGameFinished(TokenType winnerTokenType) {
        if(winnerTokenType == TokenType.PLAYER1) {
            JOptionPane.showMessageDialog(this, "Youhoo player 1 won !");
            message.setText("Player 1 won");
        } else {
            JOptionPane.showMessageDialog(this, "Youhoo player 2 won !");
            message.setText("Player 2 won");
        }
        int dialogResult = JOptionPane.showConfirmDialog(null, "Start a new game?");
        if(dialogResult == JOptionPane.YES_OPTION){            
            resetGame();
            controller.resetGame();
        }
        else{
            endGame();
        }
    }

    @Override
    public void onColumnFilled(int columnIndex) {
        controlButtons[columnIndex].setEnabled(false);
    }

    @Override
    public void ongameBoardFull() {
        if(!message.getText().contains("won")){           
            JOptionPane.showMessageDialog(this, "Game OOOVEERR!!!! >:D");
            message.setText("Game Over");
            int dialogResult = JOptionPane.showConfirmDialog(null, "Start a new game?");
            if(dialogResult == JOptionPane.YES_OPTION){            
                resetGame();
                controller.resetGame();
            }
            else{
                endGame();
            } 
        }
    }

    private class QuitActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
        }
    }
    
    private class GiveUpActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Want to give up?");
            if(dialogResult == JOptionPane.YES_OPTION){            
                onGameFinished((message.getText().contains("1"))? TokenType.PLAYER2 : TokenType.PLAYER1);
            }
        }
    }

    private class ButtonHandler implements ActionListener {

        private final int columnIndex;

        private ButtonHandler(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            System.out.println("Action on button: " + columnIndex);
            View.this.controller.addToken(columnIndex);
            
        }
    }

    private class RestartActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            controller.resetGame();
            resetGame();
        }
    }

    private class AboutActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            JOptionPane.showMessageDialog(View.this, "GUI for Connect4\n420-520-SF TP1\n\nAuthor: François Gagnon", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
