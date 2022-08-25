package controller;

import java.util.List;

import model.Cell;
import model.Chess;
import model.Player;

public class Message {
    private ConnectionType type;
    private Player turn;
    private Cell beginCell;
    private Cell endCell;
    private List<Cell> possibleMoves;
    private Chess game;

    public Message() {
        
    }
    
    public Message(ConnectionType type, Player turn) {
        this.type = type;
        this.turn = turn;
    }

    public Message(ConnectionType type, Chess game) {
        this.type = type;
        this.game = game;
    }

    public Message(ConnectionType type, List<Cell> moves) {
        this.type = type;
        this.possibleMoves = moves;
    }

    public Message(ConnectionType type, Chess game, Cell beginCell, Cell endCell) {
        this.type = type;
        this.game = game;
        this.beginCell = beginCell;
        this.endCell = endCell;
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

    public List<Cell> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(List<Cell> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public void setGame(Chess game) {
        this.game = game;
    }

    public Chess getGame() {
        return game;
    }
}
