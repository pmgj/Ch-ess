package model;

public class Cell {

    private int x;
    private int y;

    public Cell() {

    }

    public Cell(int row, int col) {
        this.x = row;
        this.y = col;
    }

    @Override
    public boolean equals(Object obj) {
        Cell cell = (Cell) obj;
        return (cell.x == this.x && cell.y == this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int row) {
        this.x = row;
    }

    public void setY(int col) {
        this.y = col;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
