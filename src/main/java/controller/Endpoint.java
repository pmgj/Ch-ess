package controller;

import java.io.IOException;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Cell;
import model.CellState;
import model.Chess;
import model.MoveResult;
import model.Player;
import model.Winner;

@ServerEndpoint(value = "/chess", encoders = MessageEncoder.class, decoders = MoveMessageDecoder.class)
public class Endpoint {

    private static Session s1;
    private static Session s2;
    private static Chess game;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        if (s1 == null) {
            s1 = session;
            s1.getBasicRemote().sendObject(new Message(ConnectionType.OPEN, Player.PLAYER1));
        } else if (s2 == null) {
            game = new Chess();
            s2 = session;
            s2.getBasicRemote().sendObject(new Message(ConnectionType.OPEN, Player.PLAYER2));
            sendMessage(new Message(ConnectionType.MESSAGE, game.getTurn(), game.getBoard()));
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, MoveMessage message) throws IOException, EncodeException {
        Player p;
        Cell beginCell = message.getBeginCell(), endCell = message.getEndCell();
        if (session == s1) {
            p = Player.PLAYER1;
        } else {
            p = Player.PLAYER2;
            CellState[][] board = game.getBoard();
            int rows = board.length, cols = board[0].length;
            int or = rows - beginCell.getX() - 1;
            int oc = cols - beginCell.getY() - 1;
            int dr = rows - endCell.getX() - 1;
            int dc = cols - endCell.getY() - 1;
            beginCell.setX(or);
            beginCell.setY(oc);
            endCell.setX(dr);
            endCell.setY(dc);
        }
        try {
            MoveResult ret = game.move(p, beginCell, endCell);
            if (ret.getWinner() == Winner.NONE) {
                sendMessage(new Message(ConnectionType.MESSAGE, game.getTurn(), game.getBoard()));
            } else {
                sendMessage(new Message(ConnectionType.ENDGAME, ret.getWinner(), game.getBoard()));
            }
        } catch (Exception ex) {
//            session.getBasicRemote().sendObject(new Message(ConnectionType.ERROR, ex.getMessage()));
            System.out.println(ex.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException, EncodeException {
        switch (reason.getCloseCode().getCode()) {
            case 4000:
                if (session == s1) {
                    s1 = null;
                } else {
                    s2 = null;
                }
                break;
            case 4001:
                if (session == s1) {
                    s2.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, Winner.PLAYER2, game.rotateBoard()));
                    s1 = null;
                } else {
                    s1.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, Winner.PLAYER1, game.getBoard()));
                    s2 = null;
                }
                break;
            default:
                System.out.println(String.format("Close code %d incorrect", reason.getCloseCode().getCode()));
                s1 = null;
                s2 = null;
        }
    }

    private void sendMessage(Message msg) throws EncodeException, IOException {
        s1.getBasicRemote().sendObject(msg);
        msg.setBoard(game.rotateBoard());
        s2.getBasicRemote().sendObject(msg);
    }
}
