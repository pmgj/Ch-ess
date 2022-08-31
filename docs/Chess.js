import CellState from './CellState.js';
import State from './State.js';
import Piece from './Piece.js';
import Player from './Player.js';
import Cell from './Cell.js';
import Winner from './Winner.js';

export default class Chess {
    constructor() {
        this.rows = 8;
        this.cols = 8;
        this.turn = Player.PLAYER1;
        this.castlingKingsidePlayer1 = true;
        this.castlingQueensidePlayer1 = true;
        this.castlingKingsidePlayer2 = true;
        this.castlingQueensidePlayer2 = true;
        this.castling = null;
        this.winner = Winner.NONE;
        this.promotion = false;
        this.promotionCell = null;
        this.promotedPiece = null;
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
    getCastling() {
        return this.castling;
    }
    getWinner() {
        return this.winner;
    }
    getPromotionCell() {
        return this.promotionCell;
    }
    getPromotedPiece() {
        return this.promotedPiece;
    }
    move(beginCell, endCell) {
        if (this.winner !== Winner.NONE) {
            throw new Error("This game is already finished.");
        }
        if (this.promotion) {
            throw new Error("You must choose a piece to promote.");
        }
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
        let moves = this.showPossibleMoves(beginCell);
        if (!moves.some(z => z.equals(endCell))) {
            throw new Error("This move is invalid.");
        }
        /* Castling */
        this.castling = this.performCastling(beginCell, endCell);
        this.setCastling(beginCell);
        /* Move */
        this.board[dr][dc] = this.board[or][oc];
        this.board[or][oc] = new CellState(State.EMPTY);
        /* Promotion */
        if (this.isPawn(endCell) && (dr == 0 || dr == this.rows - 1)) {
            this.promotion = true;
            this.promotionCell = endCell;
            return;
        }
        this.promotedPiece = null;
        this.promotionCell = null;
        this.turn = (this.turn === Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
        this.winner = this.endOfGame();
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
    isKing({ x, y }) {
        return this.board[x][y].getPiece() == Piece.KING;
    }
    isPawn({ x, y }) {
        return this.board[x][y].getPiece() == Piece.PAWN;
    }
    possibleMoves(cell) {
        let { x, y } = cell;
        let moves = [], tempCell;
        let addMoves = positions => moves.push(...positions.filter(temp => this.onBoard(temp) && this.getState(temp) !== this.getState(cell)));
        let selectPositions = (cell, directions) => {
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
        let rookPositions = cell => selectPositions(cell, [{ x: -1, y: 0 }, { x: 1, y: 0 }, { x: 0, y: -1 }, { x: 0, y: 1 }]);
        let bishopPositions = cell => selectPositions(cell, [{ x: -1, y: -1 }, { x: -1, y: 1 }, { x: 1, y: -1 }, { x: 1, y: 1 }]);
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
                addMoves([new Cell(x - 2, y - 1), new Cell(x - 2, y + 1), new Cell(x + 2, y - 1), new Cell(x + 2, y + 1), new Cell(x - 1, y - 2), new Cell(x - 1, y + 2), new Cell(x + 1, y - 2), new Cell(x + 1, y + 2)]);
                break;
            case Piece.KING:
                addMoves([new Cell(x - 1, y - 1), new Cell(x - 1, y), new Cell(x - 1, y + 1), new Cell(x, y - 1), new Cell(x, y + 1), new Cell(x + 1, y - 1), new Cell(x + 1, y), new Cell(x + 1, y + 1)]);
                let { castlingKing, castlingQueen, row, rook } = this.board[x][y].getState() === State.PLAYER1 ? { castlingKing: this.castlingKingsidePlayer1, castlingQueen: this.castlingQueensidePlayer1, row: 7, rook: new CellState(State.PLAYER1, Piece.ROOK) } : { castlingKing: this.castlingKingsidePlayer2, castlingQueen: this.castlingQueensidePlayer2, row: 0, rook: new CellState(State.PLAYER2, Piece.ROOK) };
                if (castlingKing && this.board[row][4].equals(this.board[x][y]) && this.isEmpty(new Cell(row, 5)) && this.isEmpty(new Cell(row, 6)) && this.board[row][7].equals(rook)) {
                    moves.push(new Cell(row, 6));
                }
                if (castlingQueen && this.board[row][4].equals(this.board[x][y]) && this.isEmpty(new Cell(row, 3)) && this.isEmpty(new Cell(row, 2)) && this.isEmpty(new Cell(row, 1)) && this.board[row][0].equals(rook)) {
                    moves.push(new Cell(row, 2));
                }
                break;
            case Piece.ROOK:
                moves = rookPositions(cell);
                break;
            case Piece.BISHOP:
                moves = bishopPositions(cell);
                break;
            case Piece.QUEEN:
                let dg = bishopPositions(cell);
                let hv = rookPositions(cell);
                moves = dg.concat(hv);
                break;
        }
        return moves;
    }
    getKing(state) {
        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                let temp = new Cell(i, j);
                if (this.isKing(temp) && this.board[i][j].getState() == state) {
                    return temp;
                }
            }
        }
        return null;
    }
    isCheck() {
        let cs = this.turn === Player.PLAYER1 ? State.PLAYER1 : State.PLAYER2;
        let king = this.getKing(cs);
        let op = this.turn === Player.PLAYER1 ? State.PLAYER2 : State.PLAYER1;
        return this.board.flat().some((e, i) => {
            let cell = new Cell(Math.floor(i / this.cols), i % this.cols);
            if (this.getState(cell) === op) {
                let moves = this.possibleMoves(cell);
                if (moves.some(elem => elem.equals(king))) {
                    return true;
                }
            }
            return false;
        });
    }
    showPossibleMoves(cell) {
        let ret = [];
        let piece = this.getPiece(cell);
        if ((piece === State.PLAYER1 && this.turn !== Player.PLAYER1)
            || (piece === State.PLAYER2 && this.turn !== Player.PLAYER2)) {
            return ret;
        }
        let pm = this.possibleMoves(cell);
        let { x: or, y: oc } = cell;
        let check = this.isCheck();
        let bIsKing = this.isKing(cell);
        let chess = new Chess();
        for (let m of pm) {
            let { x: dr, y: dc } = m;
            let boardCopy = Array(this.rows).fill().map(() => Array(this.cols).fill());
            for (let i = 0; i < this.board.length; i++) {
                for (let j = 0; j < this.board[i].length; j++) {
                    let temp = this.board[i][j];
                    boardCopy[i][j] = new CellState(temp.getState(), temp.getPiece());
                }
            }
            boardCopy[dr][dc] = boardCopy[or][oc];
            boardCopy[or][oc] = new CellState(State.EMPTY);
            chess.board = boardCopy;
            chess.turn = this.turn;
            let notInCheckCondition = !chess.isCheck(); // The new movement does not create a check
            let isTwoStepKing = bIsKing && Math.abs(oc - dc) === 2;
            let kingNotInCheckCondition = !(isTwoStepKing && check); // Castling is allowed if king is not in check
            let middleSquareAllowed = ret.some(elem => elem.equals(new Cell(or, (oc + dc) / 2)));
            let middleSquareNotInCheckCondition = !(isTwoStepKing && !middleSquareAllowed); // Castling is allowed if the king does not pass through a square that is attacked by an enemy piece
            if (notInCheckCondition && kingNotInCheckCondition && middleSquareNotInCheckCondition) {
                ret.push(m);
            }
        }
        return ret;
    }
    performCastling(beginCell, endCell) {
        if (!this.isKing(beginCell)) {
            return null;
        }
        let { x: or, y: oc } = beginCell;
        let { x: dr, y: dc } = endCell;
        if (Math.abs(dc - oc) === 2) {
            let a = this.board[or][oc].getState() === State.PLAYER1 ? dr : 0;
            let { b, c } = dc - oc === 2 ? { b: dc - 1, c: dc + 1 } : { b: dc + 1, c: dc - 2 };
            this.board[a][b] = this.board[a][c];
            this.board[a][c] = new CellState(State.EMPTY);
            return [new Cell(a, c), new Cell(a, b)];
        }
        return null;
    }
    setCastling(beginCell) {
        let { x: or, y: oc } = beginCell;
        if (this.getState(beginCell) == State.PLAYER1) {
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() === Piece.ROOK && or === this.rows - 1 && oc === this.cols - 1)) {
                this.castlingKingsidePlayer1 = false;
            }
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() === Piece.ROOK && or === this.rows - 1 && this.oc === 0)) {
                this.castlingQueensidePlayer1 = false;
            }
        }
        if (this.getState(beginCell) == State.PLAYER2) {
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() === Piece.ROOK && or === 0 && oc === this.cols - 1)) {
                this.castlingKingsidePlayer2 = false;
            }
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() === Piece.ROOK && or === 0 && oc === 0)) {
                this.castlingQueensidePlayer2 = false;
            }
        }
    }
    endOfGame() {
        let canMoveSomePiece = () => {
            let cs = this.turn === Player.PLAYER1 ? State.PLAYER1 : State.PLAYER2;
            return this.board.flat().some((e, i) => {
                let cell = new Cell(Math.floor(i / this.cols), i % this.cols);
                if (this.getState(cell) === cs) {
                    let moves = this.showPossibleMoves(cell);
                    if (moves.length > 0) {
                        return true;
                    }
                }
                return false;
            });
        };
        return canMoveSomePiece() ? Winner.NONE : this.isCheck() ? (this.turn == Player.PLAYER1 ? Winner.PLAYER2 : Winner.PLAYER1) : Winner.DRAW;
    }
    getPromotionList() {
        return [Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN];
    }
    promote(piece) {
        if (!this.promotion) {
            throw new Error("You can not promote a piece right now.");
        }
        this.promotedPiece = piece;
        let { x, y } = this.promotionCell;
        this.board[x][y] = new CellState(this.turn == Player.PLAYER1 ? State.PLAYER1 : State.PLAYER2, piece);
        this.promotion = false;
        this.turn = this.turn == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        this.winner = this.endOfGame();
    }
}