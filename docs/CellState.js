export default class CellState {
    constructor(state, piece) {
        this.state = state;
        this.piece = piece;
    }
    getState() {
        return this.state;
    }
    getPiece() {
        return this.piece;
    }
    equals(obj) {
        return this.piece === obj.piece && this.state === obj.state;
    }
}