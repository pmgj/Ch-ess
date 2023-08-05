package controller;

import model.Cell;
import model.Piece;

public record MoveMessage(Cell beginCell, Cell endCell, Piece promote) {

}
