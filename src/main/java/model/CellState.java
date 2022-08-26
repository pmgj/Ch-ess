package model;

public class CellState {
    private State state;
    private Piece piece;

    public CellState() {

    }

    public CellState(State state, Piece piece) {
        this.state = state;
        this.piece = piece;
    }

    public CellState(State state) {
        this.state = state;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CellState)) {
            return false;
        }
        CellState temp = (CellState) obj;
        return this.piece == temp.piece && this.state == temp.state;
    }
}
