import CellState from './CellState.js';
import State from './State.js';
import Piece from './Piece.js';
import Player from './Player.js';
import Cell from './Cell.js';

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
        let moves = this.possibleMoves(beginCell);
        if (!moves.some(z => z.equals(endCell))) {
            throw new Error("This move is invalid.");
        }
        this.board[dr][dc] = this.board[or][oc];
        this.board[or][oc] = new CellState(State.EMPTY);
        this.turn = (this.turn === Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
    }
    getState({ x, y }) {
        return this.board[x][y].getState();
    }
    getPiece({ x, y }) {
        return this.board[x][y].getPiece();
    }
    onBoard({ x, y }) {
        let inLimit = (value, limit) => value >= 0 && value < limit;
        return (inLimit(x, this.rows) && inLimit(y, this.cols));
    }
    isEmpty({ x, y }) {
        return this.board[x][y].getState() == State.EMPTY;
    }
    possibleMoves(cell) {
        let { x, y } = cell;
        let moves = [], tempCell;
        let addMoves = positions => moves.push(...positions.filter(temp => this.onBoard(temp) && this.getState(temp) !== this.getState(cell)));
        switch (this.getPiece(cell)) {
            case Piece.PAWN:
                let { a, b, c, d } = this.board[x][y].getState() === State.PLAYER1 ? { a: x - 1, b: x - 2, c: 6, d: State.PLAYER2 } : { a: x + 1, b: x + 2, c: 1, d: State.PLAYER1 };
                tempCell = new Cell(a, y);
                if (this.onBoard(tempCell) && this.isEmpty(tempCell)) {
                    moves.push(tempCell);
                }
                tempCell = new Cell(b, y);
                if (x === c && this.isEmpty(new Cell(a, y)) && this.isEmpty(tempCell)) {
                    moves.push(tempCell);
                }
                tempCell = new Cell(a, y - 1);
                if (this.onBoard(tempCell) && this.getState(tempCell) === d) {
                    moves.push(tempCell);
                }
                tempCell = new Cell(a, y + 1);
                if (this.onBoard(tempCell) && this.getState(tempCell) === d) {
                    moves.push(tempCell);
                }
                break;
            case Piece.KNIGHT:
                let positions = [new Cell(x - 2, y - 1), new Cell(x - 2, y + 1), new Cell(x + 2, y - 1), new Cell(x + 2, y + 1), new Cell(x - 1, y - 2), new Cell(x - 1, y + 2), new Cell(x + 1, y - 2), new Cell(x + 1, y + 2)];
                addMoves(positions);
                break;
            case Piece.KING:
                let positions2 = [new Cell(x - 1, y - 1), new Cell(x - 1, y), new Cell(x - 1, y + 1), new Cell(x, y - 1), new Cell(x, y + 1), new Cell(x + 1, y - 1), new Cell(x + 1, y), new Cell(x + 1, y + 1)];
                addMoves(positions2);
                break;
            case Piece.ROOK:
                moves = this.rookPositions(cell);
                break;
            case Piece.BISHOP:
                moves = this.bishopPositions(cell);
                break;
            case Piece.QUEEN:
                let dg = this.bishopPositions(cell);
                let hv = this.rookPositions(cell);
                moves = dg.concat(hv);
                break;
        }
        return moves;
    }
    selectPositions(cell, directions) {
        let moves = [];
        let { x: row, y: col } = cell;
        let piece = this.getState(cell);
        for (let { x, y } of directions) {
            for (let k = row + x, l = col + y, c = new Cell(k, l); this.onBoard(c); k += x, l += y, c = new Cell(k, l)) {
                let tempPiece = this.getState(c);
                if (tempPiece === State.EMPTY || piece !== tempPiece) {
                    moves.push(c);
                }
                if (tempPiece !== State.EMPTY) {
                    break;
                }
            }
        }
        return moves;
    }
    rookPositions(cell) {
        let lin = [{ x: -1, y: 0 }, { x: 1, y: 0 }, { x: 0, y: -1 }, { x: 0, y: 1 }];
        return this.selectPositions(cell, lin);
    }
    bishopPositions(cell) {
        let lin = [{ x: -1, y: -1 }, { x: -1, y: 1 }, { x: 1, y: -1 }, { x: 1, y: 1 }];
        return this.selectPositions(cell, lin);
    }
}