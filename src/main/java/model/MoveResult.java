package model;

import java.util.List;

public class MoveResult {
    private Winner winner;
    private Cell enPassant;
    private List<Cell> castling;
    private boolean check;

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public Cell getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(Cell enPassant) {
        this.enPassant = enPassant;
    }

    public List<Cell> getCastling() {
        return castling;
    }

    public void setCastling(List<Cell> castling) {
        this.castling = castling;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }
}
