import CellState from './CellState.js';
import State from './State.js';
import Piece from './Piece.js';
import Player from './Player.js';

export default class Chess {
    constructor() {
        this.rows = 8;
        this.cols = 8;
        this.turn = Player.PLAYER1;
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
    getTurn() {
        return this.turn;
    }
    move(beginCell, endCell) {
        let { x: or, y: oc } = beginCell;
        let { x: dr, y: dc } = endCell;
        if (!beginCell || !endCell) {
            throw new Error("The value of one of the cells does not exist.");
        }
        let currentState = this.getState(beginCell);
        if ((currentState === State.PLAYER1 && this.turn === Player.PLAYER2) || (currentState === State.PLAYER2 && this.turn === Player.PLAYER1)) {
            throw new Error("It's not your turn.");
        }
        if (beginCell.equals(endCell)) {
            throw new Error("Origin and destination must be different.");
        }
        if (!this.onBoard(beginCell) || !this.onBoard(endCell)) {
            throw new Error("Origin or destination are not in the board.");
        }
        if (currentState === State.EMPTY) {
            throw new Error("Origin does not have a piece.");
        }
        /* Realizar movimento */
        this.board[dr][dc] = this.board[or][oc];
        this.board[or][oc] = new CellState(State.EMPTY);
        this.turn = (this.turn === Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
    }
    getState({ x, y }) {
        return this.board[x][y].getState();
    }
    onBoard({ x, y }) {
        let inLimit = (value, limit) => value >= 0 && value < limit;
        return (inLimit(x, this.rows) && inLimit(y, this.cols));
    }
}