package com.school.interfaces;

import com.school.models.Token;
import com.school.models.Token.TokenType;


public interface BoardListener {
    void onTokenAdded(Token token);
    void onGameFinished(TokenType winnerTokenType);
    void onColumnFilled(int columnIndex);
    void ongameBoardFull();
}
