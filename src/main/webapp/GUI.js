import ConnectionType from "./ConnectionType.js";
import CellState from "./CellState.js";
import Cell from "./Cell.js";
import Winner from "./Winner.js";

class GUI {
    constructor() {
        this.ws = null;
        this.turn = null;
        this.table = null;
        this.origin = null;
        this.images = { PAWN_PLAYER1: "Peao-Branco.svg", ROOK_PLAYER1: "Torre-Branca.svg", KNIGHT_PLAYER1: "Cavalo-Branco.svg", BISHOP_PLAYER1: "Bispo-Branco.svg", QUEEN_PLAYER1: "Rainha-Branca.svg", KING_PLAYER1: "Rei-Branco.svg", PAWN_PLAYER2: "Peao-Preto.svg", ROOK_PLAYER2: "Torre-Preta.svg", KNIGHT_PLAYER2: "Cavalo-Preto.svg", BISHOP_PLAYER2: "Bispo-Preto.svg", QUEEN_PLAYER2: "Rainha-Preta.svg", KING_PLAYER2: "Rei-Preto.svg" };
        this.closeCodes = { ENDGAME: { code: 4000, description: "End of game." }, ADVERSARY_QUIT: { code: 4001, description: "The opponent quit the game" } };
    }
    writeResponse(text) {
        let message = document.getElementById("message");
        message.innerHTML = text;
    }
    printBoard(board) {
        let tbody = document.querySelector("tbody");
        tbody.innerHTML = "";
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
    getTableData({ x, y }) {
        return this.table.rows[x].cells[y];
    }
    movePiece(begin, end) {
        if (!begin || !end) {
            return;
        }
        let bTD = this.getTableData(begin);
        let eTD = this.getTableData(end);
        if (eTD.firstChild) {
            eTD.removeChild(eTD.firstChild);
        }
        let img = bTD.firstChild;
        eTD.appendChild(img);
    }
    coordinates(tableData) {
        return new Cell(tableData.parentNode.rowIndex, tableData.cellIndex);
    }
    play(evt) {
        let td = evt.currentTarget;
        if (this.origin === null) {
            this.origin = td;
        } else {
            this.ws.send(JSON.stringify({ beginCell: this.coordinates(this.origin), endCell: this.coordinates(td) }));
            this.origin = null;
        }
    }
    unsetEvents() {
        let cells = document.querySelectorAll("td");
        cells.forEach(td => td.onclick = undefined);
    }
    endGame(type) {
        this.unsetEvents();
        this.ws = null;
        this.setButtonText(true);
        this.writeResponse(`Game Over! ${(type === "DRAW") ? "Draw!" : (type === this.turn ? "You win!" : "You lose!")}`);
    }
    onMessage(evt) {
        let data = JSON.parse(evt.data);
        console.log(data);
        switch (data.type) {
            case ConnectionType.OPEN:
                this.turn = data.turn;
                this.writeResponse("Waiting for opponent.");
                break;
            case ConnectionType.CREATE_BOARD:
                this.printBoard(data.board);
                this.writeResponse(this.turn === data.turn ? "It's your turn." : "Wait for your turn.");
                break;
            case ConnectionType.MESSAGE:
                let mr = data.moveResult;
                if (mr.winner === Winner.NONE) {
                    this.movePiece(data.beginCell, data.endCell);
                    if (mr.enPassant) {
                        let cell = this.getTableData(mr.enPassant);
                        cell.removeChild(cell.firstChild);
                    }
                    if (mr.castling) {
                        this.movePiece(mr.castling[0], mr.castling[1]);
                    }
                    this.writeResponse(this.turn === data.turn ? "It's your turn." : "Wait for your turn.");
                } else {
                    this.ws.close(this.closeCodes.ENDGAME.code, this.closeCodes.ENDGAME.description);
                    this.endGame(mr.winner);
                    this.movePiece(data.beginCell, data.endCell);
                }
                break;
        }
    }
    startGame() {
        if (this.ws) {
            this.ws.close(this.closeCodes.ADVERSARY_QUIT.code, this.closeCodes.ADVERSARY_QUIT.description);
            this.endGame();
        } else {
            this.ws = new WebSocket(`ws://${document.location.host}${document.location.pathname}chess`);
            this.ws.onmessage = this.onMessage.bind(this);
            this.table = document.querySelector("table");
            this.setButtonText(false);
        }
    }
    setButtonText(on) {
        let button = document.querySelector("input[type='button']");
        button.value = on ? "Start" : "Quit";
    }
    init() {
        let button = document.querySelector("input[type='button']");
        button.onclick = this.startGame.bind(this);
        this.setButtonText(true);
    }
}
let gui = new GUI();
gui.init();