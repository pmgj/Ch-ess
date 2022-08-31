package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Chess {

    private final int rows = 8;
    private final int cols = 8;
    private CellState[][] board = {
            { new CellState(State.PLAYER2, Piece.ROOK), new CellState(State.PLAYER2, Piece.KNIGHT),
                    new CellState(State.PLAYER2, Piece.BISHOP), new CellState(State.PLAYER2, Piece.QUEEN),
                    new CellState(State.PLAYER2, Piece.KING), new CellState(State.PLAYER2, Piece.BISHOP),
                    new CellState(State.PLAYER2, Piece.KNIGHT),
                    new CellState(State.PLAYER2, Piece.ROOK) },
            { new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN),
                    new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN),
                    new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN),
                    new CellState(State.PLAYER2, Piece.PAWN), new CellState(State.PLAYER2, Piece.PAWN) },
            { new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY) },
            { new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY) },
            { new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY) },
            { new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY), new CellState(State.EMPTY),
                    new CellState(State.EMPTY), new CellState(State.EMPTY) },
            { new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN),
                    new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN),
                    new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN),
                    new CellState(State.PLAYER1, Piece.PAWN), new CellState(State.PLAYER1, Piece.PAWN) },
            { new CellState(State.PLAYER1, Piece.ROOK), new CellState(State.PLAYER1, Piece.KNIGHT),
                    new CellState(State.PLAYER1, Piece.BISHOP), new CellState(State.PLAYER1, Piece.QUEEN),
                    new CellState(State.PLAYER1, Piece.KING), new CellState(State.PLAYER1, Piece.BISHOP),
                    new CellState(State.PLAYER1, Piece.KNIGHT), new CellState(State.PLAYER1, Piece.ROOK) }
    };
    private Winner winner = Winner.NONE;
    private Player turn = Player.PLAYER1;
    private boolean castlingKingsidePlayer1 = true;
    private boolean castlingQueensidePlayer1 = true;
    private boolean castlingKingsidePlayer2 = true;
    private boolean castlingQueensidePlayer2 = true;
    private List<Cell> castling;
    private Cell enPassant = null;
    private boolean promotion = false;
    private Cell promotionCell = null;
    private Piece promotedPiece = null;

    public Player getTurn() {
        return this.turn;
    }

    public CellState[][] getBoard() {
        return this.board;
    }

    public Cell getEnPassant() {
        return enPassant != null && this.isEmpty(enPassant) ? enPassant : null;
    }

    public List<Cell> getCastling() {
        return castling;
    }

    public Winner getWinner() {
        return winner;
    }

    public Cell getPromotionCell() {
        return promotionCell;
    }

    public Piece getPromotedPiece() {
        return promotedPiece;
    }

    private State getState(Cell cell) {
        return this.board[cell.getX()][cell.getY()].getState();
    }

    private Piece getPiece(Cell cell) {
        return this.board[cell.getX()][cell.getY()].getPiece();
    }

    private boolean onBoard(Cell cell) {
        BiFunction<Integer, Integer, Boolean> inLimit = (value, limit) -> value >= 0 && value < limit;
        return (inLimit.apply(cell.getX(), rows) && inLimit.apply(cell.getY(), cols));
    }

    private boolean isEmpty(Cell cell) {
        return this.board[cell.getX()][cell.getY()].getState() == State.EMPTY;
    }

    private boolean isKing(Cell cell) {
        return this.board[cell.getX()][cell.getY()].getPiece() == Piece.KING;
    }

    private boolean isPawn(Cell cell) {
        return this.board[cell.getX()][cell.getY()].getPiece() == Piece.PAWN;
    }

    private List<Cell> performCastling(Cell beginCell, Cell endCell) {
        if (!this.isKing(beginCell)) {
            return null;
        }
        int or = beginCell.getX(), oc = beginCell.getY();
        int dr = endCell.getX(), dc = endCell.getY();
        if (Math.abs(dc - oc) == 2) {
            int a = this.board[or][oc].getState() == State.PLAYER1 ? dr : 0;
            int b = dc - oc == 2 ? dc - 1 : dc + 1;
            int c = dc - oc == 2 ? dc + 1 : dc - 2;
            this.board[a][b] = this.board[a][c];
            this.board[a][c] = new CellState(State.EMPTY);
            return Arrays.asList(new Cell(a, c), new Cell(a, b));
        }
        return null;
    }

    private void setCastling(Cell beginCell) {
        int or = beginCell.getX(), oc = beginCell.getY();
        if (this.getState(beginCell) == State.PLAYER1) {
            if (this.isKing(beginCell)
                    || (this.board[or][oc].getPiece() == Piece.ROOK && or == rows - 1 && oc == cols - 1)) {
                castlingKingsidePlayer1 = false;
            }
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() == Piece.ROOK && or == rows - 1 && oc == 0)) {
                castlingQueensidePlayer1 = false;
            }
        }
        if (this.getState(beginCell) == State.PLAYER2) {
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() == Piece.ROOK && or == 0 && oc == cols - 1)) {
                castlingKingsidePlayer2 = false;
            }
            if (this.isKing(beginCell) || (this.board[or][oc].getPiece() == Piece.ROOK && or == 0 && oc == 0)) {
                castlingQueensidePlayer2 = false;
            }
        }
    }

    private Cell getKing(State state) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                var temp = new Cell(i, j);
                if (this.isKing(temp) && this.board[i][j].getState() == state) {
                    return temp;
                }
            }
        }
        return null;
    }

    public void move(Player player, Cell beginCell, Cell endCell) throws Exception {
        if (this.winner != Winner.NONE) {
            throw new Exception("This game is already finished.");
        }
        if (this.promotion) {
            throw new Exception("You must choose a piece to promote.");
        }
        /* Valores das células existem? */
        if (beginCell == null || endCell == null) {
            throw new Exception("Origin or destination cell does not exist.");
        }
        int or = beginCell.getX(), oc = beginCell.getY();
        int dr = endCell.getX(), dc = endCell.getY();
        State currentPiece = getState(beginCell);
        /* É a sua vez de jogar */
        if (this.turn != player) {
            throw new Exception("It's not your turn.");
        }
        /* Você está tentando jogar uma peça sua? */
        if ((currentPiece == State.PLAYER1 && this.turn == Player.PLAYER2)
                || (currentPiece == State.PLAYER2 && this.turn == Player.PLAYER1)) {
            throw new Exception("You can not move an opponent piece.");
        }
        /* Origem e destino devem ser diferentes */
        if (beginCell.equals(endCell)) {
            throw new Exception("Origin and destination cells must be different.");
        }
        /* Origem e destino estão no tabuleiro? */
        if (!this.onBoard(beginCell) || !this.onBoard(endCell)) {
            throw new Exception("Origin or destination cell is not on board.");
        }
        /* Origem possui uma peça? */
        if (currentPiece == State.EMPTY) {
            throw new Exception("Origin cell is empty.");
        }
        /* A jogada é possível? */
        List<Cell> moves = this.showPossibleMoves(beginCell);
        if (!moves.stream().anyMatch(z -> z.equals(endCell))) {
            throw new Exception("This move is invalid.");
        }
        /* Armazenar peça para possível en passant */
        if (this.isPawn(beginCell) && Math.abs(or - dr) == 1 && Math.abs(oc - dc) == 1
                && this.isEmpty(endCell)) {
            int tx = enPassant.getX(), ty = enPassant.getY();
            this.board[tx][ty] = new CellState(State.EMPTY);
        } else {
            this.enPassant = null;
        }
        if (this.isPawn(beginCell)) {
            var p1 = this.board[or][oc].getState() == State.PLAYER1 && or == 6 && dr == 4;
            var p2 = this.board[or][oc].getState() == State.PLAYER2 && or == 1 && dr == 3;
            if (p1 || p2) {
                this.enPassant = endCell;
            }
        }
        /* Castling */
        this.castling = this.performCastling(beginCell, endCell);
        this.setCastling(beginCell);
        /* Realizar movimento */
        this.board[dr][dc] = this.board[or][oc];
        this.board[or][oc] = new CellState(State.EMPTY);
        /* Promotion */
        if (this.isPawn(endCell) && (dr == 0 || dr == rows - 1)) {
            this.promotion = true;
            this.promotionCell = endCell;
            return;
        }
        this.promotedPiece = null;
        this.promotionCell = null;
        this.turn = (this.turn == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
        this.winner = this.endOfGame();
    }

    private Winner endOfGame() {
        Supplier<Boolean> canMoveSomePiece = () -> {
            State cs = this.turn == Player.PLAYER1 ? State.PLAYER1 : State.PLAYER2;
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    Cell cell = new Cell(i, j);
                    if (this.getState(cell) == cs) {
                        List<Cell> moves = this.showPossibleMoves(cell);
                        if (!moves.isEmpty()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        };
        return canMoveSomePiece.get() ? Winner.NONE
                : this.isCheck() ? (this.turn == Player.PLAYER1 ? Winner.PLAYER2 : Winner.PLAYER1) : Winner.DRAW;
    }

    public boolean isCheck() {
        State cs = this.turn == Player.PLAYER1 ? State.PLAYER1 : State.PLAYER2;
        Cell king = this.getKing(cs);
        State op = this.turn == Player.PLAYER1 ? State.PLAYER2 : State.PLAYER1;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                Cell cell = new Cell(i, j);
                if (this.getState(cell) == op) {
                    List<Cell> moves = this.possibleMoves(cell);
                    if (moves.stream().anyMatch(elem -> elem.equals(king))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Cell> showPossibleMoves(Cell cell) {
        List<Cell> ret = new ArrayList<>();
        State piece = this.getState(cell);
        if ((piece == State.PLAYER1 && this.turn == Player.PLAYER1)
                || (piece == State.PLAYER2 && this.turn == Player.PLAYER2)) {
            List<Cell> pm = this.possibleMoves(cell);
            int or = cell.getX(), oc = cell.getY();
            boolean check = this.isCheck();
            boolean bIsKing = this.isKing(cell);
            Chess chess = new Chess();
            pm.forEach(m -> {
                int dr = m.getX(), dc = m.getY();

                CellState[][] boardCopy = new CellState[this.rows][this.cols];
                for (int i = 0; i < this.board.length; i++) {
                    for (int j = 0; j < this.board[i].length; j++) {
                        var temp = this.board[i][j];
                        boardCopy[i][j] = new CellState(temp.getState(), temp.getPiece());
                    }
                }

                boardCopy[dr][dc] = boardCopy[or][oc];
                boardCopy[or][oc] = new CellState(State.EMPTY);
                chess.board = boardCopy;
                chess.turn = this.turn;
                // The new movement does not create a check
                boolean notInCheckCondition = !chess.isCheck();
                boolean isTwoStepKing = bIsKing && Math.abs(oc - dc) == 2;
                // Castling is allowed if king is not in check
                boolean kingNotInCheckCondition = !(isTwoStepKing && check);
                boolean middleSquareAllowed = ret.stream().anyMatch(elem -> elem.equals(new Cell(or, (oc + dc) / 2)));
                // Castling is allowed if the king does not pass through a square that is
                // attacked by an enemy piece
                boolean middleSquareNotInCheckCondition = !(isTwoStepKing && !middleSquareAllowed);
                if (notInCheckCondition && kingNotInCheckCondition && middleSquareNotInCheckCondition) {
                    ret.add(m);
                }
            });
        }
        return ret;
    }

    public List<Piece> getPromotionList() {
        return List.of(Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN);
    }

    public void promote(Piece piece) throws Exception {
        if (!this.promotion) {
            throw new Exception("You can not promote a piece right now.");
        }
        this.promotedPiece = piece;
        this.board[this.promotionCell.getX()][this.promotionCell.getY()] = new CellState(
                this.turn == Player.PLAYER1 ? State.PLAYER1 : State.PLAYER2, piece);
        this.promotion = false;
        this.turn = this.turn == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        this.winner = this.endOfGame();
    }

    private List<Cell> possibleMoves(Cell cell) {
        int x = cell.getX(), y = cell.getY();
        final List<Cell> moves = new ArrayList<>();
        Cell tempCell;
        Consumer<List<Cell>> addMoves = positions -> moves.addAll(
                positions.stream().filter(temp -> this.onBoard(temp) && this.getState(temp) != this.getState(cell))
                        .collect(Collectors.toList()));
        BiFunction<Cell, List<Cell>, List<Cell>> selectPositions = (c, directions) -> {
            List<Cell> positions = new ArrayList<>();
            int row = c.getX(), col = c.getY();
            State piece = this.getState(c);
            for (Cell dir : directions) {
                int xDir = dir.getX(), yDir = dir.getY();
                Cell temp;
                for (int k = row + xDir, l = col + yDir; this.onBoard(temp = new Cell(k, l)); k += xDir, l += yDir) {
                    State tempPiece = this.getState(temp);
                    if (tempPiece == State.EMPTY || piece != tempPiece) {
                        positions.add(temp);
                    }
                    if (tempPiece != State.EMPTY) {
                        break;
                    }
                }
            }
            return positions;
        };
        Function<Cell, List<Cell>> rookPositions = c -> selectPositions.apply(c, Arrays.asList(new Cell(-1, 0), new Cell(1, 0), new Cell(0, -1), new Cell(0, 1)));
        Function<Cell, List<Cell>> bishopPositions = c -> selectPositions.apply(c, Arrays.asList(new Cell(-1, -1), new Cell(-1, 1), new Cell(1, -1), new Cell(1, 1)));
        switch (this.getPiece(cell)) {
            case PAWN:
                int a, b, c;
                State d;
                if (this.board[x][y].getState() == State.PLAYER1) {
                    a = x - 1;
                    b = x - 2;
                    c = 6;
                    d = State.PLAYER2;
                } else {
                    a = x + 1;
                    b = x + 2;
                    c = 1;
                    d = State.PLAYER1;
                }
                tempCell = new Cell(a, y);
                if (this.onBoard(tempCell) && this.isEmpty(tempCell)) {
                    moves.add(tempCell);
                }
                tempCell = new Cell(b, y);
                if (x == c && this.isEmpty(new Cell(a, y)) && this.isEmpty(tempCell)) {
                    moves.add(tempCell);
                }
                tempCell = new Cell(a, y - 1);
                if (this.onBoard(tempCell) && this.getState(tempCell) == d) {
                    moves.add(tempCell);
                }
                tempCell = new Cell(a, y + 1);
                if (this.onBoard(tempCell) && this.getState(tempCell) == d) {
                    moves.add(tempCell);
                }
                tempCell = new Cell(x, y - 1);
                if (this.enPassant != null && this.onBoard(tempCell) && this.board[x][y - 1].getPiece() == Piece.PAWN
                        && tempCell.equals(enPassant)) {
                    moves.add(new Cell(a, y - 1));
                }
                tempCell = new Cell(x, y + 1);
                if (this.enPassant != null && this.onBoard(tempCell) && this.board[x][y + 1].getPiece() == Piece.PAWN
                        && tempCell.equals(enPassant)) {
                    moves.add(new Cell(a, y + 1));
                }
                break;
            case KNIGHT:
                List<Cell> positions = Arrays.asList(new Cell(x - 2, y - 1), new Cell(x - 2, y + 1),
                        new Cell(x + 2, y - 1), new Cell(x + 2, y + 1), new Cell(x - 1, y - 2), new Cell(x - 1, y + 2),
                        new Cell(x + 1, y - 2), new Cell(x + 1, y + 2));
                addMoves.accept(positions);
                break;
            case KING:
                List<Cell> positions2 = Arrays.asList(
                        new Cell(x - 1, y - 1), new Cell(x - 1, y), new Cell(x - 1, y + 1), new Cell(x, y - 1),
                        new Cell(x, y + 1), new Cell(x + 1, y - 1), new Cell(x + 1, y), new Cell(x + 1, y + 1));
                addMoves.accept(positions2);
                boolean castlingKing, castlingQueen;
                int row;
                CellState rook;
                if (this.board[x][y].getState() == State.PLAYER1) {
                    castlingKing = castlingKingsidePlayer1;
                    castlingQueen = castlingQueensidePlayer1;
                    row = 7;
                    rook = new CellState(State.PLAYER1, Piece.ROOK);
                } else {
                    castlingKing = castlingKingsidePlayer2;
                    castlingQueen = castlingQueensidePlayer2;
                    row = 0;
                    rook = new CellState(State.PLAYER2, Piece.ROOK);
                }
                if (castlingKing && this.board[row][4].equals(this.board[x][y])
                        && this.isEmpty(new Cell(row, 5))
                        && this.isEmpty(new Cell(row, 6)) && this.board[row][7].equals(rook)) {
                    moves.add(new Cell(row, 6));
                }
                if (castlingQueen && this.board[row][4].equals(this.board[x][y])
                        && this.isEmpty(new Cell(row, 3))
                        && this.isEmpty(new Cell(row, 2)) && this.isEmpty(new Cell(row, 1))
                        && this.board[row][0].equals(rook)) {
                    moves.add(new Cell(row, 2));
                }
                break;
            case ROOK:
                moves.addAll(rookPositions.apply(cell));
                break;
            case BISHOP:
                moves.addAll(bishopPositions.apply(cell));
                break;
            case QUEEN:
                List<Cell> dg = bishopPositions.apply(cell);
                List<Cell> hv = rookPositions.apply(cell);
                moves.addAll(dg);
                moves.addAll(hv);
        }
        return moves;
    }

    public void printBoard() {
        for (int i = 0; i <= this.board.length * 5; i++) {
            System.out.print("-");
        }
        System.out.println("");
        for (CellState[] b : this.board) {
            for (CellState c : b) {
                System.out.print("| ");
                if (c.getState() == State.EMPTY) {
                    System.out.print("   ");
                    continue;
                }
                switch (c.getPiece()) {
                    case KING:
                        System.out.print("R");
                        // System.out.print("\u2654");
                        break;
                    case QUEEN:
                        System.out.print("D");
                        // System.out.print("\u2655");
                        break;
                    case ROOK:
                        System.out.print("T");
                        // System.out.print("\u2656");
                        break;
                    case BISHOP:
                        System.out.print("B");
                        // System.out.print("\u2657");
                        break;
                    case KNIGHT:
                        System.out.print("C");
                        // System.out.print("\u2658");
                        break;
                    case PAWN:
                        System.out.print("P");
                        // System.out.print("\u2659");
                        break;
                }
                System.out.print(c.getState() == State.PLAYER1 ? "1" : "2");
                System.out.print(" ");
            }
            System.out.println("|");
            for (int i = 0; i <= this.board.length * 5; i++) {
                System.out.print("-");
            }
            System.out.println("");
        }
    }
}