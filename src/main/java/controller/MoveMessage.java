package controller;

import model.Cell;

public class MoveMessage {
    private Cell beginCell;
    private Cell endCell;
    private int promote = -1;

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

    public int getPromote() {
        return promote;
    }

    public void setPromote(int promote) {
        this.promote = promote;
    }
}
