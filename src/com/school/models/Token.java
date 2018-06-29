package com.school.models;

import java.awt.Point;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Token {
    private final TokenType type;
    
    private final Point position;    
    private ImageIcon image = new ImageIcon();   
    
    public Token(TokenType tokenType, Point position){
        this.type = tokenType;
        this.position = position;
        
        if(tokenType == TokenType.PLAYER1) {
            try {
                this.image = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("resources/token_black.png")));
            } catch (IOException ex) {
                Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                this.image = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("resources/token_red.png")));
            } catch (IOException ex) {
                Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public TokenType getType(){
        return type;
    }
    
    public ImageIcon getImage() {
        return this.image;
    }   
    
    public Point getPosition(){
        return position;
    }
            
   public enum TokenType{
        PLAYER1,
        PLAYER2
   }         
}
