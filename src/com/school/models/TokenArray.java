package com.school.models;

import com.school.interfaces.ITokenArray;
import java.awt.Point;

public class TokenArray implements ITokenArray {
    private final Token[][] array;   
    private final int rows;    
    private final int columns;
    private final int COLUMN_TOP = 0;
    
    public TokenArray(int rowNumber, int columnNumber)
    {
        if(rowNumber < 1 || columnNumber < 1) 
            throw new IllegalArgumentException("The values must be greater than 0");
            
        rows = rowNumber;
        columns = columnNumber;
        array = new Token[columnNumber][rowNumber];
    }
    
    @Override
    public Token getTokenAt(Point position)
    {
        if(position == null)
            throw new IllegalArgumentException("The point is null");
        
        return getTokenAt(position.x, position.y);
    }
    
    @Override
    public Token getTokenAt(int xPosition, int yPosition)
    {
        if(!areCoordinatesValid(xPosition, yPosition))
            throw new IllegalArgumentException("Position is invalid : (" +xPosition + ", " + yPosition + ").");
            
        return array[xPosition][yPosition];
    }
    
    @Override
    public boolean hasTokenAt(Point position) {
         if(position == null)
            throw new IllegalArgumentException("The point is null");
        
        return hasTokenAt(position.x, position.y);
    }
    
    @Override
    public boolean hasTokenAt(int xPosition, int yPosition) {
        if(!areCoordinatesValid(xPosition, yPosition))
            throw new IllegalArgumentException("Position is invalid : (" +xPosition + ", " + yPosition + ").");
        
        return getTokenAt(xPosition, yPosition) != null;
    }
    
    @Override
    public boolean isColumnFull(int columnIndex)
    {
        if(columnIndex < 0 || columnIndex >= columns)
            throw new IllegalArgumentException("The column index is invalid : " + columnIndex);
        
        return array[columnIndex][COLUMN_TOP] != null;
    }
    
    @Override
    public void addToken(Token tokenToAdd)
    {
        if(tokenToAdd == null)
            throw new IllegalArgumentException("The token is null");
        
        int xPosition = tokenToAdd.getPosition().x;
        int yPosition = tokenToAdd.getPosition().y;
        if(!areCoordinatesValid(xPosition, yPosition))
            throw new IndexOutOfBoundsException("Position is invalid : (" +xPosition + ", " + yPosition + ").");
        
        if(isColumnFull(xPosition))
            throw new UnsupportedOperationException("The column is full.");
        
        array[xPosition][yPosition] = tokenToAdd;      
    }
    
    @Override
    public boolean areCoordinatesValid(int x, int y)
    {
        return (x >= 0 && x < columns) && (y >= 0 && y < rows);
    }    
}
