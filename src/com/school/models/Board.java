package com.school.models;

import com.school.interfaces.BoardListener;
import com.school.interfaces.ITokenArray;
import com.school.models.Token.TokenType;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private final Set<BoardListener> listeners = new HashSet<>();
    private final int rowCount;
    private final int columnCount;
    private final int winCount;
    private final ITokenArray tokenArray;
    private int[] columnSizes;
    
    private TokenType playerTurn;

    public Board(int rowCount, int columnCount, int winCount) {
        int bigestValue = (rowCount >= columnCount)? rowCount : columnCount;

        if(rowCount < 1 || columnCount < 1 || winCount > bigestValue )
            throw new IllegalArgumentException("Invalid arguments");
        
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.winCount = winCount;

        this.tokenArray = new TokenArray(rowCount, columnCount);
        this.columnSizes = new int[columnCount];
        this.playerTurn = TokenType.PLAYER1;
    }
    
    public void addListener(BoardListener listener) {
        this.listeners.add(listener);
    }
    
    public TokenType getPlayerTurn(){
        return playerTurn;
    }

    public void addToken(int columnIndex) {
        if(columnIndex >= columnCount ||columnIndex < 0)
            throw new IllegalArgumentException("Index too big or too small");
        
        int rowIndex = rowCount - columnSizes[columnIndex] - 1;
        
        Token newToken = new Token(playerTurn, new Point(columnIndex, rowIndex));
        tokenArray.addToken(newToken);
        columnSizes[columnIndex]++;
        fireOnTokenAddedEvent(newToken);
        
        if(tokenArray.isColumnFull(columnIndex)) {
            fireOnColumnFilledEvent(columnIndex);
        }
        
        if(isBoardFull()){
            fireOnBoardFull();
        }
        
        playerTurn = newToken.getOppositeType();
        checkForWin(newToken);
    }
    
    private boolean isBoardFull(){
        for(int size : columnSizes){
            if(size < rowCount) return false;
        }
        return true;
    }
    
    private void fireOnTokenAddedEvent(Token tokenAdded) {
        for(BoardListener listener : listeners) {
            listener.onTokenAdded(tokenAdded);
        }
    }
    
    private void fireOnGameFinishedEvent(Token tokenEndingGame) {
        for(BoardListener listener : listeners) {
            listener.onGameFinished(tokenEndingGame.getType());
        }
    }
    
    private void fireOnColumnFilledEvent(int columnIndex) {
        for(BoardListener listener : listeners) {
            listener.onColumnFilled(columnIndex);
        }
    }
    
    private void fireOnBoardFull() {
        for(BoardListener listener : listeners) {
            listener.ongameBoardFull();
        }
    }

    private void checkForWin(Token addedToken) {
        for(Direction direction : Direction.values()){
            if (getConsecutiveTokenCount(addedToken, direction) >= winCount){
                fireOnGameFinishedEvent(addedToken);
                break;
            }
        }
    }
    
     private Point getMovement(Direction direction){
         Point point = new Point(0,0);
         switch(direction){
             case HORIZONTAL : point = new Point(1,0); break;             
             case VERTICAL : point = new Point(0,1); break;
             case RIGHT_DIAGONAL : point = new Point(1,-1); break;
             case LEFT_DIAGONAL : point = new Point(1,1); break;
         }
         return point;
     }
    
    private int getConsecutiveTokenCount(Token token, Direction direction){
        Point fowardMovement = getMovement(direction);        
        Point backwardMovement = new Point(fowardMovement.x * -1, fowardMovement.y * -1);
        final int ADDED_TOKEN = 1;

        int sameTokenCount = getConsecutiveTokenCountInDirection(fowardMovement, token) + getConsecutiveTokenCountInDirection(backwardMovement, token) + ADDED_TOKEN;
        return sameTokenCount;
    }
    
    private int getConsecutiveTokenCountInDirection(Point movement, Token addedToken){
        int sameTokenCount = 0;
        int currentX = addedToken.getPosition().x + movement.x;        
        int currentY = addedToken.getPosition().y + movement.y;

        while(isTokenOfSameType(currentX, currentY, addedToken)){
            sameTokenCount++;
            currentX += movement.x;        
            currentY += movement.y;
        }

        return sameTokenCount;
    }

    private boolean isTokenOfSameType(int currentX, int currentY, Token addedToken){
        return tokenArray.areCoordinatesValid(currentX, currentY) && 
               tokenArray.hasTokenAt(currentX, currentY) && 
               tokenArray.getTokenAt(currentX, currentY).getType() == addedToken.getType();
    }
    
    private enum Direction{
        HORIZONTAL,
        VERTICAL,
        RIGHT_DIAGONAL,
        LEFT_DIAGONAL
    }
}
