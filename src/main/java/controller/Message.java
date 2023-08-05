package controller;

import java.util.List;

import model.Cell;
import model.Chess;
import model.Player;

public record Message(ConnectionType type, Player turn, Cell beginCell, Cell endCell, List<Cell> possibleMoves, Chess game) {

}
