package com.school.interfaces;

import com.school.models.Token;
import java.awt.Point;


public interface ITokenArray {
    
    public Token getTokenAt(Point position);
    
    public Token getTokenAt(int xPosition, int yPosition);
    
    public boolean hasTokenAt(Point position);
    
    public boolean hasTokenAt(int xPosition, int yPosition);
    
    public boolean isColumnFull(int columnIndex);
    
    public void addToken(Token tokenToAdd);
    
    public boolean areCoordinatesValid(int x, int y);
    
}
