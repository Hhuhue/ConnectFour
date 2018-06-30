package com.school.controller;

import com.school.interfaces.Controller;
import com.school.models.Board;
import com.school.ui.View;

public class ConnectFourController implements Controller {
    private final View view;
    private Board board;
    private final int nbRows;
    private final int nbColumns;
    private final int nbWins;
    
    public ConnectFourController(int nbRows, int nbColumns, int nbWins) {
        this.board = new Board(nbRows, nbColumns, nbWins);
        this.view = new View(this, nbRows, nbColumns);
        
        this.board.addListener(view);
        this.nbColumns = nbColumns;
        this.nbWins = nbWins;
        this.nbRows = nbRows;
    }
    
    @Override
    public void addToken(int columnIndex) {
        this.board.addToken(columnIndex);
    }

    @Override
    public void resetGame() {
        board = new Board(nbRows, nbColumns, nbWins);
        board.addListener(view);
    }
    
}
