import CellState from './CellState.js';
import State from './State.js';
import Piece from './Piece.js';

export default class Chess {
    constructor() {
        this.board = [
            [new CellState(State.PLAYER2, Piece.ROOK), new CellState(State.PLAYER2, Piece.KNIGHT),
            new CellState(State.PLAYER2, Piece.BISHOP), new CellState(State.PLAYER2, Piece.QUEEN),
            new CellState(State.PLAYER2, Piece.KING), new CellState(State.PLAYER2, Piece.BISHOP),
            new CellState(State.PLAYER2, Piece.KNIGHT), new CellState(State.PLAYER2, Piece.ROOK)],
            [new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN),
            new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN),
            new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN),
            new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN)],
            [new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY)],
            [new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY)],
            [new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY)],
            [new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
            new CellState(State.EMPTY), new CellState(State.EMPTY)],
            [new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN),
            new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN),
            new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN),
            new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN)],
            [new CellState(State.PLAYER1, Piece.ROOK), new CellState(State.PLAYER1, Piece.KNIGHT),
            new CellState(State.PLAYER1, Piece.BISHOP), new CellState(State.PLAYER1, Piece.QUEEN),
            new CellState(State.PLAYER1, Piece.KING), new CellState(State.PLAYER1, Piece.BISHOP),
            new CellState(State.PLAYER1, Piece.KNIGHT), new CellState(State.PLAYER1, Piece.ROOK)]
        ];    
    }
    getBoard() {
        return this.board;
    }
}