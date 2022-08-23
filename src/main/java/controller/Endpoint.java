package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Cell;
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
            Message msg = new Message(ConnectionType.CREATE_BOARD, game.getTurn(), game.getBoard());
            s1.getBasicRemote().sendObject(msg);
            msg.setBoard(game.rotateBoard());
            s2.getBasicRemote().sendObject(msg);
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, MoveMessage message) throws IOException, EncodeException {
        Cell beginCell = message.getBeginCell(), endCell = message.getEndCell();
        if(endCell == null) {
            Cell bCell = null;
            List<Cell> pm = null;
            if (session == s1 && game.getTurn() == Player.PLAYER1) {
                bCell = beginCell;
                pm = game.showPossibleMoves(bCell);
            }
            if (session == s2 && game.getTurn() == Player.PLAYER2) {
                bCell = game.getRotatedCell(beginCell);
                pm = game.showPossibleMoves(bCell);
                List<Cell> rotatedCells = new ArrayList<>();
                for(Cell temp : pm) {
                    rotatedCells.add(game.getRotatedCell(temp));
                }
                pm = rotatedCells;
            }
            if(bCell != null) {
                session.getBasicRemote().sendObject(new Message(ConnectionType.SHOW_MOVES, pm));    
            } 
            return;
        }
        Cell bc1, bc2, ec1, ec2;
        Player p;
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
            s1.getBasicRemote().sendObject(new Message(ConnectionType.MESSAGE, game.getTurn(), bc1, ec1, ret));
            if (ret.getEnPassant() != null) {
                ret.setEnPassant(game.getRotatedCell(ret.getEnPassant()));
            }
            if (ret.getCastling() != null) {
                Stream<Cell> rotatedCells = ret.getCastling().stream().map(c -> game.getRotatedCell(c));
                ret.setCastling(rotatedCells.collect(Collectors.toList()));
            }
            s2.getBasicRemote().sendObject(new Message(ConnectionType.MESSAGE, game.getTurn(), bc2, ec2, ret));
        } catch (Exception ex) {
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
                    s2.getBasicRemote().sendObject(new Message(ConnectionType.MESSAGE, Winner.PLAYER2));
                    s1 = null;
                } else {
                    s1.getBasicRemote().sendObject(new Message(ConnectionType.MESSAGE, Winner.PLAYER1));
                    s2 = null;
                }
                break;
            default:
                System.out.println(String.format("Close code %d incorrect", reason.getCloseCode().getCode()));
                s1 = null;
                s2 = null;
        }
    }
}
