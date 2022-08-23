package controller;

import java.util.List;

import model.Cell;
import model.CellState;
import model.MoveResult;
import model.Player;
import model.Winner;

public class Message {
    private ConnectionType type;
    private Player turn;
    private CellState[][] board;
    private Winner winner;
    private Cell beginCell;
    private Cell endCell;
    private MoveResult moveResult;
    private List<Cell> possibleMoves;

    public Message() {
        
    }
    
    public Message(ConnectionType type, Player turn) {
        this.type = type;
        this.turn = turn;
    }

    public Message(ConnectionType type, List<Cell> moves) {
        this.type = type;
        this.possibleMoves = moves;
    }

    public Message(ConnectionType type, Player turn, CellState[][] board) {
        this.type = type;
        this.turn = turn;
        this.board = board;
    }

    public Message(ConnectionType type, Player turn, Cell beginCell, Cell endCell, MoveResult mr) {
        this.type = type;
        this.turn = turn;
        this.beginCell = beginCell;
        this.endCell = endCell;
        this.moveResult = mr;
    }

    public Message(ConnectionType type, Winner winner) {
        this.type = type;
        MoveResult mr = new MoveResult();
        mr.setWinner(winner);
        this.moveResult = mr;
    }
    
    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }

    public CellState[][] getBoard() {
        return board;
    }

    public void setBoard(CellState[][] board) {
        this.board = board;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public Cell getBeginCell() {
        return beginCell;
    }

    public void setBeginCell(Cell beginCell) {
        this.beginCell = beginCell;
    }

    public Cell getEndCell() {
        return endCell;
    }

    public void setEndCell(Cell endCell) {
        this.endCell = endCell;
    }

    public void setMoveResult(MoveResult moveResult) {
        this.moveResult = moveResult;
    }

    public MoveResult getMoveResult() {
        return moveResult;
    }

    public List<Cell> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(List<Cell> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
}
