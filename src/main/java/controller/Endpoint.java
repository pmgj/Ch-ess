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
            Message msg = new Message(ConnectionType.MESSAGE, game.getTurn(), game.getBoard());
            s1.getBasicRemote().sendObject(msg);
            msg.setBoard(game.rotateBoard());
            s2.getBasicRemote().sendObject(msg);
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, MoveMessage message) throws IOException, EncodeException {
        Cell bc1, bc2, ec1, ec2;
        Player p;
        Cell beginCell = message.getBeginCell(), endCell = message.getEndCell();
        if (session == s1) {
            p = Player.PLAYER1;
            bc1 = beginCell;
            ec1 = endCell;
            bc2 = game.getRotatedCell(beginCell);
            ec2 = game.getRotatedCell(endCell);
        } else {
            p = Player.PLAYER2;
            bc2 = beginCell;
            ec2 = endCell;
            beginCell = game.getRotatedCell(beginCell);
            endCell = game.getRotatedCell(endCell);
            bc1 = beginCell;
            ec1 = endCell;
        }
        try {
            MoveResult ret = game.move(p, beginCell, endCell);
            if (ret.getWinner() == Winner.NONE) {
                sendMessage(new Message(ConnectionType.MESSAGE, game.getTurn(), beginCell, endCell));
            } else {
                sendMessage(new Message(ConnectionType.ENDGAME, ret.getWinner(), beginCell, endCell));
            }
        } catch (Exception ex) {
            // session.getBasicRemote().sendObject(new Message(ConnectionType.ERROR,
            // ex.getMessage()));
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
                    s2.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, Winner.PLAYER2));
                    s1 = null;
                } else {
                    s1.getBasicRemote().sendObject(new Message(ConnectionType.ENDGAME, Winner.PLAYER1));
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
        s2.getBasicRemote().sendObject(msg);
    }
}
