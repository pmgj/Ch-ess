package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import controller.ConnectionType;
import controller.Message;
import jakarta.json.bind.JsonbBuilder;

public class Chess {

    private final int rows = 8;
    private final int cols = 8;
    private CellState[][] board = {
            { CellState.ROOK_PLAYER2, CellState.KNIGHT_PLAYER2, CellState.BISHOP_PLAYER2, CellState.QUEEN_PLAYER2,
                    CellState.KING_PLAYER2, CellState.BISHOP_PLAYER2, CellState.KNIGHT_PLAYER2,
                    CellState.ROOK_PLAYER2 },
            { CellState.PAWN_PLAYER2, CellState.PAWN_PLAYER2, CellState.PAWN_PLAYER2, CellState.PAWN_PLAYER2,
                    CellState.PAWN_PLAYER2, CellState.PAWN_PLAYER2, CellState.PAWN_PLAYER2, CellState.PAWN_PLAYER2 },
            { CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY,
                    CellState.EMPTY, CellState.EMPTY },
            { CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY,
                    CellState.EMPTY, CellState.EMPTY },
            { CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY,
                    CellState.EMPTY, CellState.EMPTY },
            { CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY, CellState.EMPTY,
                    CellState.EMPTY, CellState.EMPTY },
            { CellState.PAWN_PLAYER1, CellState.PAWN_PLAYER1, CellState.PAWN_PLAYER1, CellState.PAWN_PLAYER1,
                    CellState.PAWN_PLAYER1, CellState.PAWN_PLAYER1, CellState.PAWN_PLAYER1, CellState.PAWN_PLAYER1 },
            { CellState.ROOK_PLAYER1, CellState.KNIGHT_PLAYER1, CellState.BISHOP_PLAYER1, CellState.QUEEN_PLAYER1,
                    CellState.KING_PLAYER1, CellState.BISHOP_PLAYER1, CellState.KNIGHT_PLAYER1, CellState.ROOK_PLAYER1 }
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
    private CellState promotedPiece = null;

    public Player getTurn() {
        return this.turn;
    }

    public CellState[][] getBoard() {
        return this.board;
    }

    public Cell getEnPassant() {
        return enPassant != null && this.getPiece(enPassant) == CellState.EMPTY ? enPassant : null;
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

    public CellState getPromotedPiece() {
        return promotedPiece;
    }

    private CellState getPiece(Cell cell) {
        switch (this.board[cell.getX()][cell.getY()]) {
            case PAWN_PLAYER1:
            case ROOK_PLAYER1:
            case KNIGHT_PLAYER1:
            case BISHOP_PLAYER1:
            case QUEEN_PLAYER1:
            case KING_PLAYER1:
                return CellState.PLAYER1;
            case PAWN_PLAYER2:
            case ROOK_PLAYER2:
            case KNIGHT_PLAYER2:
            case BISHOP_PLAYER2:
            case QUEEN_PLAYER2:
            case KING_PLAYER2:
                return CellState.PLAYER2;
            default:
                return CellState.EMPTY;
        }
    }

    private boolean onBoard(Cell cell) {
        BiFunction<Integer, Integer, Boolean> inLimit = (value, limit) -> value >= 0 && value < limit;
        return (inLimit.apply(cell.getX(), rows) && inLimit.apply(cell.getY(), cols));
    }

    private boolean isKing(Cell cell) {
        int x = cell.getX(), y = cell.getY();
        return this.board[x][y] == CellState.KING_PLAYER1 || this.board[x][y] == CellState.KING_PLAYER2;
    }

    private List<Cell> performCastling(Cell beginCell, Cell endCell) {
        int or = beginCell.getX(), oc = beginCell.getY();
        int dr = endCell.getX(), dc = endCell.getY();
        if (Math.abs(dc - oc) == 2) {
            int a = this.board[or][oc] == CellState.KING_PLAYER1 ? dr : 0;
            int b = dc - oc == 2 ? dc - 1 : dc + 1;
            int c = dc - oc == 2 ? dc + 1 : dc - 2;
            if (this.isKing(beginCell)) {
                this.board[a][b] = this.board[a][c];
                this.board[a][c] = CellState.EMPTY;
                return Arrays.asList(new Cell(a, c), new Cell(a, b));
            }
        }
        return null;
    }

    private void setCastling(Cell beginCell) {
        int or = beginCell.getX(), oc = beginCell.getY();
        if (this.board[or][oc] == CellState.KING_PLAYER1
                || (this.board[or][oc] == CellState.ROOK_PLAYER1 && or == rows - 1 && oc == cols - 1)) {
            castlingKingsidePlayer1 = false;
        }
        if (this.board[or][oc] == CellState.KING_PLAYER1
                || (this.board[or][oc] == CellState.ROOK_PLAYER1 && or == rows - 1 && oc == 0)) {
            castlingQueensidePlayer1 = false;
        }
        if (this.board[or][oc] == CellState.KING_PLAYER2
                || (this.board[or][oc] == CellState.ROOK_PLAYER2 && or == 0 && oc == cols - 1)) {
            castlingKingsidePlayer2 = false;
        }
        if (this.board[or][oc] == CellState.KING_PLAYER2
                || (this.board[or][oc] == CellState.ROOK_PLAYER2 && or == 0 && oc == 0)) {
            castlingQueensidePlayer2 = false;
        }
    }

    private boolean isPawn(Cell cell) {
        int x = cell.getX(), y = cell.getY();
        return this.board[x][y] == CellState.PAWN_PLAYER1 || this.board[x][y] == CellState.PAWN_PLAYER2;
    }

    private Cell getKing(CellState king) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.board[i][j] == king) {
                    return new Cell(i, j);
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
        CellState currentPiece = getPiece(beginCell);
        /* É a sua vez de jogar */
        if (this.turn != player) {
            throw new Exception("It's not your turn.");
        }
        /* Você está tentando jogar uma peça sua? */
        if ((currentPiece == CellState.PLAYER1 && this.turn == Player.PLAYER2)
                || (currentPiece == CellState.PLAYER2 && this.turn == Player.PLAYER1)) {
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
        if (currentPiece == CellState.EMPTY) {
            throw new Exception("Origin cell is empty.");
        }
        /* A jogada é possível? */
        List<Cell> moves = this.showPossibleMoves(beginCell);
        if (!moves.stream().anyMatch(z -> z.equals(endCell))) {
            throw new Exception("This move is invalid.");
        }
        /* Armazenar peça para possível en passant */
        if (this.isPawn(beginCell) && Math.abs(or - dr) == 1 && Math.abs(oc - dc) == 1
                && this.board[dr][dc] == CellState.EMPTY) {
            int tx = enPassant.getX(), ty = enPassant.getY();
            this.board[tx][ty] = CellState.EMPTY;
        } else {
            this.enPassant = null;
        }
        if (this.board[or][oc] == CellState.PAWN_PLAYER1 && or == 6 && dr == 4) {
            this.enPassant = endCell;
        }
        if (this.board[or][oc] == CellState.PAWN_PLAYER2 && or == 1 && dr == 3) {
            this.enPassant = endCell;
        }
        /* Castling */
        this.castling = this.performCastling(beginCell, endCell);
        this.setCastling(beginCell);
        /* Realizar movimento */
        this.board[dr][dc] = this.board[or][oc];
        this.board[or][oc] = CellState.EMPTY;
        /* Promotion */
        if (this.isPawn(endCell) && (dr == 0 || dr == rows - 1)) {
            this.promotion = true;
            this.promotionCell = endCell;
            return;
        }
        this.turn = (this.turn == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
        this.winner = this.endOfGame();
    }

    /* Verify end of game condition */
    private Winner endOfGame() {
        /* If is a check mate, the game ends */
        boolean noMove = this.cannotMoveAnyPiece();
        if (noMove) {
            boolean check1 = this.isCheck(Player.PLAYER1);
            if (check1) {
                return Winner.PLAYER2;
            }
            boolean check2 = this.isCheck(Player.PLAYER2);
            if (check2) {
                return Winner.PLAYER1;
            }
            return Winner.DRAW;
        }
        return Winner.NONE;
    }

    private boolean cannotMoveAnyPiece() {
        CellState cs = turn == Player.PLAYER1 ? CellState.PLAYER1 : CellState.PLAYER2;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                Cell cell = new Cell(i, j);
                if (this.getPiece(cell) == cs) {
                    List<Cell> moves = this.showPossibleMoves(cell);
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isCheck() {
        return this.isCheck(this.turn);
    }

    private boolean isCheck(Player player) {
        CellState cs = player == Player.PLAYER1 ? CellState.KING_PLAYER1 : CellState.KING_PLAYER2;
        Cell king = this.getKing(cs);
        CellState op = player == Player.PLAYER1 ? CellState.PLAYER2 : CellState.PLAYER1;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                Cell cell = new Cell(i, j);
                if (this.getPiece(cell) == op) {
                    List<Cell> moves = this.possibleMoves(cell);
                    if (moves.stream().filter(elem -> elem.equals(king)).count() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Cell> showPossibleMoves(Cell cell) {
        List<Cell> ret = new ArrayList<>();
        CellState piece = this.getPiece(cell);
        if ((piece == CellState.PLAYER1 && this.turn == Player.PLAYER1)
                || (piece == CellState.PLAYER2 && this.turn == Player.PLAYER2)) {
            List<Cell> pm = this.possibleMoves(cell);
            int or = cell.getX(), oc = cell.getY();
            boolean check = this.isCheck(turn);
            boolean bIsKing = this.isKing(cell);
            Chess chess = new Chess();
            pm.forEach(m -> {
                int dr = m.getX(), dc = m.getY();

                CellState[][] boardCopy = new CellState[this.rows][this.cols];
                for (int i = 0; i < this.board.length; i++) {
                    CellState[] row = this.board[i];
                    System.arraycopy(row, 0, boardCopy[i], 0, this.board.length);
                }

                boardCopy[dr][dc] = boardCopy[or][oc];
                boardCopy[or][oc] = CellState.EMPTY;
                chess.setBoard(boardCopy);
                // The new movement does not create a check
                boolean notInCheckCondition = !chess.isCheck(turn);
                boolean isTwoStepKing = bIsKing && Math.abs(oc - dc) == 2;
                // Castling is allowed if king is not in check
                boolean kingNotInCheckCondition = !(isTwoStepKing && check);
                boolean middleSquareAllowed = ret.stream().filter(elem -> elem.equals(new Cell(or, (oc + dc) / 2)))
                        .count() > 0;
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

    private void setBoard(CellState[][] matrix) {
        this.board = matrix;
    }

    public void promote(int newPiece) {
        CellState[] player1 = new CellState[] { CellState.ROOK_PLAYER1, CellState.KNIGHT_PLAYER1,
                CellState.BISHOP_PLAYER1, CellState.QUEEN_PLAYER1 };
        CellState[] player2 = new CellState[] { CellState.ROOK_PLAYER2, CellState.KNIGHT_PLAYER2,
                CellState.BISHOP_PLAYER2, CellState.QUEEN_PLAYER2 };
        CellState piece = this.turn == Player.PLAYER1 ? player1[newPiece] : player2[newPiece];
        this.promotedPiece = piece;
        this.board[this.promotionCell.getX()][this.promotionCell.getY()] = piece;
        this.promotion = false;
        this.turn = this.turn == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        this.winner = this.endOfGame();
    }

    private List<Cell> possibleMoves(Cell cell) {
        int x = cell.getX(), y = cell.getY();
        final List<Cell> moves = new ArrayList<>();
        Cell tempCell;
        Function<List<Cell>, List<Cell>> addMoves = positions -> {
            positions.forEach(temp -> {
                if (this.onBoard(temp) && this.getPiece(temp) != this.getPiece(cell)) {
                    moves.add(temp);
                }
            });
            return moves;
        };
        if (this.isPawn(cell)) {
            int a, b, c;
            CellState d, e;
            if (this.board[x][y] == CellState.PAWN_PLAYER1) {
                a = x - 1;
                b = x - 2;
                c = 6;
                d = CellState.PLAYER2;
                e = CellState.PAWN_PLAYER2;
            } else {
                a = x + 1;
                b = x + 2;
                c = 1;
                d = CellState.PLAYER1;
                e = CellState.PAWN_PLAYER1;
            }
            tempCell = new Cell(a, y);
            if (this.onBoard(tempCell) && this.getPiece(tempCell) == CellState.EMPTY) {
                moves.add(tempCell);
            }
            tempCell = new Cell(b, y);
            if (x == c && this.getPiece(new Cell(a, y)) == CellState.EMPTY
                    && this.getPiece(tempCell) == CellState.EMPTY) {
                moves.add(tempCell);
            }
            tempCell = new Cell(a, y - 1);
            if (this.onBoard(tempCell) && this.getPiece(tempCell) == d) {
                moves.add(tempCell);
            }
            tempCell = new Cell(a, y + 1);
            if (this.onBoard(tempCell) && this.getPiece(tempCell) == d) {
                moves.add(tempCell);
            }
            tempCell = new Cell(x, y - 1);
            if (this.enPassant != null && this.onBoard(tempCell) && this.board[x][y - 1] == e
                    && tempCell.equals(enPassant)) {
                moves.add(new Cell(a, y - 1));
            }
            tempCell = new Cell(x, y + 1);
            if (this.enPassant != null && this.onBoard(tempCell) && this.board[x][y + 1] == e
                    && tempCell.equals(enPassant)) {
                moves.add(new Cell(a, y + 1));
            }
        } else if (this.board[x][y] == CellState.KNIGHT_PLAYER1 || this.board[x][y] == CellState.KNIGHT_PLAYER2) {
            List<Cell> positions = Arrays.asList(new Cell(x - 2, y - 1), new Cell(x - 2, y + 1),
                    new Cell(x + 2, y - 1), new Cell(x + 2, y + 1), new Cell(x - 1, y - 2), new Cell(x - 1, y + 2),
                    new Cell(x + 1, y - 2), new Cell(x + 1, y + 2));
            addMoves.apply(positions);
        } else if (this.isKing(cell)) {
            List<Cell> positions = Arrays.asList(
                    new Cell(x - 1, y - 1), new Cell(x - 1, y), new Cell(x - 1, y + 1), new Cell(x, y - 1),
                    new Cell(x, y + 1), new Cell(x + 1, y - 1), new Cell(x + 1, y), new Cell(x + 1, y + 1));
            addMoves.apply(positions);
            boolean castlingKing, castlingQueen;
            int row;
            CellState rook;
            if (this.board[x][y] == CellState.KING_PLAYER1) {
                castlingKing = castlingKingsidePlayer1;
                castlingQueen = castlingQueensidePlayer1;
                row = 7;
                rook = CellState.ROOK_PLAYER1;
            } else {
                castlingKing = castlingKingsidePlayer2;
                castlingQueen = castlingQueensidePlayer2;
                row = 0;
                rook = CellState.ROOK_PLAYER2;
            }
            if (castlingKing && this.board[row][4] == this.board[x][y] && this.board[row][5] == CellState.EMPTY
                    && this.board[row][6] == CellState.EMPTY && this.board[row][7] == rook) {
                moves.add(new Cell(row, 6));
            }
            if (castlingQueen && this.board[row][4] == this.board[x][y] && this.board[row][3] == CellState.EMPTY
                    && this.board[row][2] == CellState.EMPTY && this.board[row][1] == CellState.EMPTY
                    && this.board[row][0] == rook) {
                moves.add(new Cell(row, 2));
            }
        } else if (this.board[x][y] == CellState.ROOK_PLAYER1 || this.board[x][y] == CellState.ROOK_PLAYER2) {
            moves.addAll(rookPositions(cell));
        } else if (this.board[x][y] == CellState.BISHOP_PLAYER1 || this.board[x][y] == CellState.BISHOP_PLAYER2) {
            moves.addAll(bishopPositions(cell));
        } else if (this.board[x][y] == CellState.QUEEN_PLAYER1 || this.board[x][y] == CellState.QUEEN_PLAYER2) {
            List<Cell> dg = this.bishopPositions(cell);
            List<Cell> hv = this.rookPositions(cell);
            moves.addAll(dg);
            moves.addAll(hv);
        }
        return moves;
    }

    private List<Cell> selectPositions(Cell cell, List<Cell> directions) {
        List<Cell> moves = new ArrayList<>();
        int row = cell.getX(), col = cell.getY();
        CellState piece = this.getPiece(cell);
        for (Cell dir : directions) {
            int x = dir.getX(), y = dir.getY();
            Cell c;
            for (int k = row + x, l = col + y; this.onBoard(c = new Cell(k, l)); k += x, l += y) {
                CellState tempPiece = this.getPiece(c);
                if (tempPiece == CellState.EMPTY || piece != tempPiece) {
                    moves.add(c);
                }
                if (tempPiece != CellState.EMPTY) {
                    break;
                }
            }
        }
        return moves;
    }

    private List<Cell> rookPositions(Cell cell) {
        List<Cell> lin = Arrays.asList(new Cell(-1, 0), new Cell(1, 0), new Cell(0, -1), new Cell(0, 1));
        return this.selectPositions(cell, lin);
    }

    private List<Cell> bishopPositions(Cell cell) {
        List<Cell> lin = Arrays.asList(new Cell(-1, -1), new Cell(-1, 1), new Cell(1, -1), new Cell(1, 1));
        return this.selectPositions(cell, lin);
    }

    public void printBoard() {
        for (int i = 0; i <= this.board.length * 5; i++) {
            System.out.print("-");
        }
        System.out.println("");
        for (CellState[] b : this.board) {
            for (CellState c : b) {
                System.out.print("| ");
                switch (c) {
                    case KING_PLAYER1:
                        System.out.print("R1");
                        // System.out.print("\u2654");
                        break;
                    case QUEEN_PLAYER1:
                        System.out.print("D1");
                        // System.out.print("\u2655");
                        break;
                    case ROOK_PLAYER1:
                        System.out.print("T1");
                        // System.out.print("\u2656");
                        break;
                    case BISHOP_PLAYER1:
                        System.out.print("B1");
                        // System.out.print("\u2657");
                        break;
                    case KNIGHT_PLAYER1:
                        System.out.print("C1");
                        // System.out.print("\u2658");
                        break;
                    case PAWN_PLAYER1:
                        System.out.print("P1");
                        // System.out.print("\u2659");
                        break;
                    case KING_PLAYER2:
                        System.out.print("R2");
                        // System.out.print("\u265A");
                        break;
                    case QUEEN_PLAYER2:
                        System.out.print("D2");
                        // System.out.print("\u265B");
                        break;
                    case ROOK_PLAYER2:
                        System.out.print("T2");
                        // System.out.print("\u265C");
                        break;
                    case BISHOP_PLAYER2:
                        System.out.print("B2");
                        // System.out.print("\u265D");
                        break;
                    case KNIGHT_PLAYER2:
                        System.out.print("C2");
                        // System.out.print("\u265E");
                        break;
                    case PAWN_PLAYER2:
                        System.out.print("P2");
                        // System.out.print("\u265F");
                        break;
                    case EMPTY:
                        System.out.print("  ");
                        break;
                    default:
                        break;
                }
                System.out.print(" ");
            }
            System.out.println("|");
            for (int i = 0; i <= this.board.length * 5; i++) {
                System.out.print("-");
            }
            System.out.println("");
        }
    }

    public Cell getRotatedCell(Cell cell) {
        int or = rows - cell.getX() - 1;
        int oc = cols - cell.getY() - 1;
        return new Cell(or, oc);
    }

    public static void main(String[] args) {
        Chess game = new Chess();
        Message message = new Message(ConnectionType.OPEN, game);
        System.out.println(JsonbBuilder.create().toJson(message));
    }
}
