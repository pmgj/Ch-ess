import ConnectionType from "./ConnectionType.js";
import CellState from "./CellState.js";
import Cell from "./Cell.js";

class GUI {
    constructor() {
        this.ws = null;
        this.turn = null;
        this.currentTurn = null;
        this.table = null;
        this.origin = null;
        this.images = { PAWN_PLAYER1: "Peao-Branco.svg", ROOK_PLAYER1: "Torre-Branca.svg", KNIGHT_PLAYER1: "Cavalo-Branco.svg", BISHOP_PLAYER1: "Bispo-Branco.svg", QUEEN_PLAYER1: "Rainha-Branca.svg", KING_PLAYER1: "Rei-Branco.svg", PAWN_PLAYER2: "Peao-Preto.svg", ROOK_PLAYER2: "Torre-Preta.svg", KNIGHT_PLAYER2: "Cavalo-Preto.svg", BISHOP_PLAYER2: "Bispo-Preto.svg", QUEEN_PLAYER2: "Rainha-Preta.svg", KING_PLAYER2: "Rei-Preto.svg" };
    }
    writeResponse(text) {
        let message = document.getElementById("message");
        message.innerHTML = text;
    }
    turnMessage() {
        this.writeResponse(this.turn === this.currentTurn ? "It's your turn." : "Wait for your turn.");
    }
    printBoard(board) {
        let tbody = document.querySelector("tbody");
        for (let i = 0; i < board.length; i++) {
            let tr = document.createElement("tr");
            for (let j = 0; j < board[i].length; j++) {
                let td = document.createElement("td");
                if (board[i][j] !== CellState.EMPTY) {
                    let img = document.createElement("img");
                    img.src = `images/${this.images[board[i][j]]}`;
                    // img.ondragstart = drag;
                    td.appendChild(img);
                }
                td.onclick = this.play.bind(this);
                // td.ondragover = allowDrop;
                // td.ondrop = drop;
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
        // changeMessage();
    }
    piecePlayer(row, col) {
        if (col === undefined) {
            col = row.cellIndex;
            row = row.parentNode.rowIndex;
        }
        let cell = this.table.rows[row].cells[col];
        let img = cell.firstChild;
        if (img) {
            if (img.src.indexOf("Branc") !== -1) {
                return CellState.PLAYER1;
            } else {
                return CellState.PLAYER2;
            }
        } else {
            return CellState.EMPTY;
        }
    }
    unselectCells() {
        let rows = this.table.rows;
        for (let i = 0; i < rows.length; i++) {
            let row = rows[i];
            for (let j = 0; j < row.cells.length; j++) {
                let cell = row.cells[j];
                cell.className = "";
            }
        }
    }
    movePiece2(begin, end) {
        let msg = { type: 1, turn: this.turn, begin: { row: begin.parentNode.rowIndex, col: begin.cellIndex }, end: { row: end.parentNode.rowIndex, col: end.cellIndex } };
        this.ws.send(JSON.stringify(msg));
        this.turn = (this.turn + 1) % 2;
        let tmp = begin.firstChild.cloneNode(true);
        if (end.firstChild) {
            end.removeChild(end.firstChild);
        }
        end.appendChild(tmp);
        begin.removeChild(begin.firstChild);
        this.turnMessage();
    }
    coordinates(tableData) {
        return new Cell(tableData.parentNode.rowIndex, tableData.cellIndex);
    }
    play(evt) {
        let td = evt.currentTarget;
        let piece = this.piecePlayer(td);
        if (this.origin) {
            let msg = { beginCell: this.coordinates(this.origin), endCell: this.coordinates(td) };
            this.ws.send(JSON.stringify(msg));
            return;
        }
        if (piece !== this.turn) {
            return;
        }
        this.origin = td;

        // if (this.origin === null) {
        //     if (piece === this.currentTurn && this.turn === this.currentTurn && td.firstChild !== null) {
        //         td.className = "selected";
        //         posicoesPossiveis(td);
        //         this.origin = td;
        //     }
        // } else {
        //     if (td.className === "selected") {
        //         this.unselectCells();
        //         if (this.origin !== td) {
        //             this.movePiece2(this.origin, td);
        //         }
        //         this.origin = null;
        //     }
        // }
    }
    onOpen() {
        this.writeResponse("Waiting opponent...");
    }
    onMessage(evt) {
        let data = JSON.parse(evt.data);
        switch (data.type) {
            case ConnectionType.OPEN:
                this.turn = data.turn;
                break;
            case ConnectionType.MESSAGE:
                this.currentTurn = data.turn;
                if (data.board) {
                    this.printBoard(data.board);
                } else {
                    let begin = data.begin;
                    let end = data.end;
                    this.movePiece(begin, end);
                }
                this.turnMessage();
                break;
            case ConnectionType.ERROR:
                this.writeResponse(data.message);
                break;
            case ConnectionType.ENDGAME:
                this.writeResponse("Connection closed.");
                this.ws.close();
                break;
        }
        // this.setEvents();
    }
    onClose() {
        this.writeResponse("Connection closed.");
    }
    onError(evt) {
        console.log(`Error: ${evt.data}`);
    }
    init() {
        let wsUri = `ws://${document.location.host}${document.location.pathname}chess`;
        this.ws = new WebSocket(wsUri);
        this.ws.onopen = this.onOpen.bind(this);
        this.ws.onmessage = this.onMessage.bind(this);
        this.ws.onerror = this.onError;
        this.ws.onclose = this.onClose.bind(this);
        this.table = document.querySelector("table");
    }
}
let gui = new GUI();
gui.init();