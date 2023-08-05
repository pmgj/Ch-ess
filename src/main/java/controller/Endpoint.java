package controller;

import java.io.IOException;
import java.util.List;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Cell;
import model.Chess;
import model.Player;

@ServerEndpoint(value = "/chess", encoders = MessageEncoder.class, decoders = MoveMessageDecoder.class)
public class Endpoint {

    private static Session s1;
    private static Session s2;
    private static Chess game;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        if (s1 == null) {
            s1 = session;
            s1.getBasicRemote().sendObject(new Message(ConnectionType.OPEN, Player.PLAYER1, null, null, null, null));
        } else if (s2 == null) {
            game = new Chess();
            s2 = session;
            s2.getBasicRemote().sendObject(new Message(ConnectionType.OPEN, Player.PLAYER2, null, null, null, null));
            Message msg = new Message(ConnectionType.CREATE_BOARD, null, null, null, null, game);
            s1.getBasicRemote().sendObject(msg);
            s2.getBasicRemote().sendObject(msg);
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, MoveMessage message) throws IOException, EncodeException {
        Cell beginCell = message.beginCell(), endCell = message.endCell();
        try {
            if (message.promote() != null) {
                game.promote(message.promote());
                s1.getBasicRemote().sendObject(new Message(ConnectionType.PROMOTED_PIECE, null, null, null, null, game));
                s2.getBasicRemote().sendObject(new Message(ConnectionType.PROMOTED_PIECE, null, null, null, null, game));
                return;
            }
            if (endCell == null) {
                Cell bCell = null;
                List<Cell> pm = null;
                if (session == s1 && game.getTurn() == Player.PLAYER1) {
                    bCell = beginCell;
                    pm = game.showPossibleMoves(bCell);
                }
                if (session == s2 && game.getTurn() == Player.PLAYER2) {
                    bCell = beginCell;
                    pm = game.showPossibleMoves(bCell);
                }
                if (bCell != null) {
                    session.getBasicRemote().sendObject(new Message(ConnectionType.SHOW_MOVES, null, null, null, pm, null));
                }
                return;
            }
            game.move(session == s1 ? Player.PLAYER1 : Player.PLAYER2, beginCell, endCell);
            s1.getBasicRemote().sendObject(new Message(ConnectionType.MOVE_PIECE, null, beginCell, endCell, null, game));
            s2.getBasicRemote().sendObject(new Message(ConnectionType.MOVE_PIECE, null, beginCell, endCell, null, game));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException, EncodeException {
        switch (reason.getCloseCode().getCode()) {
            case 1000, 4000 -> {
                if (session == s1) {
                    s1 = null;
                } else {
                    s2 = null;
                }
            }
            case 1001, 4001 -> {
                if (session == s1) {
                    s2.getBasicRemote().sendObject(new Message(ConnectionType.QUIT_GAME, Player.PLAYER2, null, null, null, null));
                    s1 = null;
                } else {
                    s1.getBasicRemote().sendObject(new Message(ConnectionType.QUIT_GAME, Player.PLAYER1, null, null, null, null));
                    s2 = null;
                }
            }
            default -> {
                System.out.println(String.format("Close code %d incorrect", reason.getCloseCode().getCode()));
                s1 = null;
                s2 = null;
            }
        }
    }
}
