package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChessTest {

    @Test
    public void testMove() {
        System.out.println("testMove");
        Chess c = new Chess();
        MoveResult m;

        c.printBoard();

        // try {
        //     m = c.move(Player.PLAYER1, new Cell(5, 0), new Cell(4, 1));
        //     Assertions.fail();
        // } catch (Exception ex) {
        // }
        // try {
        //     m = c.move(Player.PLAYER1, new Cell(6, 4), new Cell(5, 4));
        //     Assertions.assertSame(Winner.NONE, m.getWinner());
        // } catch (Exception ex) {
        // }
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 2), new Cell(4, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(3, 0), new Cell(3, 4));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(9, 0), new Cell(8, 1));
        // Assert.assertSame(Move.INVALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 1), new Cell(5, 0));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 3), new Cell(3, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 6), new Cell(4, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(3, 4), new Cell(5, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 7), new Cell(6, 7));
        // Assert.assertSame(Move.INVALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 7), new Cell(4, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 5), new Cell(3, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 3), new Cell(5, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(1, 0), new Cell(2, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(1, 1), new Cell(2, 1));
        // Assert.assertSame(Move.INVALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 2), new Cell(4, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(3, 6), new Cell(4, 7));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 5), new Cell(5, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(4, 7), new Cell(6, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(7, 4), new Cell(5, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(7, 4), new Cell(5, 5));
        // Assert.assertSame(Move.INVALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(1, 4), new Cell(2, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 6), new Cell(4, 7));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 1), new Cell(3, 0));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(4, 3), new Cell(2, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 5), new Cell(3, 4));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(4, 5), new Cell(2, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(1, 2), new Cell(3, 4));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 4), new Cell(4, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(3, 0), new Cell(1, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(4, 3), new Cell(2, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(1, 6), new Cell(3, 4));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(7, 2), new Cell(6, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(0, 5), new Cell(1, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 1), new Cell(5, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 7), new Cell(3, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(4, 7), new Cell(4, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(1, 2), new Cell(2, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(7, 6), new Cell(6, 7));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(0, 3), new Cell(1, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(7, 0), new Cell(6, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 1), new Cell(3, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(4, 3), new Cell(0, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(0, 1), new Cell(1, 2));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(0, 3), new Cell(3, 0));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(1, 6), new Cell(2, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(3, 0), new Cell(4, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(2, 5), new Cell(3, 4));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(6, 7), new Cell(5, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(0, 7), new Cell(1, 6));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 2), new Cell(4, 3));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(3, 4), new Cell(7, 0));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(4, 1), new Cell(0, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(7, 0), new Cell(3, 4));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(0, 5), new Cell(2, 7));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(3, 4), new Cell(6, 7));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(5, 0), new Cell(4, 1));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER2, new Cell(6, 7), new Cell(4, 5));
        // Assert.assertSame(Move.VALID, m.getMove());
        // m = c.move(Player.PLAYER1, new Cell(2, 7), new Cell(7, 2));
        // Assert.assertSame(Winner.PLAYER1, m.getWinner());
    }
}
