package controller;

import model.Cell;

public class MoveMessage {
    private Cell beginCell;
    private Cell endCell;

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
}
