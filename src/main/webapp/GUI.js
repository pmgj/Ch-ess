class GUI {
    constructor() {
        this.ws = null;
        this.turn = null;
        this.currentTurn = null;
        this.table = null;
        this.origin = null;
    }
    writeResponse(text) {
        let message = document.getElementById("message");
        message.innerHTML = text;
    }
    turnMessage() {
        conn.writeResponse(this.turn === this.currentTurn ? "It's your turn." : "Wait for your turn.");
    }
    setEvents() {
        this.table = document.querySelector("table");
        let cells = document.querySelectorAll("td");
        cells.forEach(cell => {
            cell.onclick = this.play.bind(this);
            let x = cell.firstChild;
            if (x) {
                x.draggable = true;
                x.ondragstart = drag;
            }
            cell.ondragover = allowDrop;
            cell.ondrop = drop;
        });
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
                return "PLAYER1";
            } else {
                return "PLAYER2";
            }
        } else {
            return "EMPTY";
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
    play(evt) {
        let td = evt.currentTarget;
        let piece = this.piecePlayer(td);
        if (this.origin === null) {
            if (piece === this.currentTurn && this.turn === this.currentTurn && td.firstChild !== null) {
                td.className = "selected";
                // posicoesPossiveis(td);
                this.origin = td;
            }
        } else {
            if (td.className === "selected") {
                this.unselectCells();
                if (this.origin !== td) {
                    this.movePiece2(this.origin, td);
                }
                this.origin = null;
            }
        }
    }
    onOpen() {
        this.writeResponse("Waiting opponent...");
    }
    onMessage(evt) {
        let data = JSON.parse(evt.data);
        switch (data.type) {
            case "OPEN":
                this.turn = data.color;
                this.currentTurn = data.turn;
                this.turnMessage();
                break;
            case "MESSAGE":
                this.currentTurn = data.turn;
                let begin = data.begin;
                let end = data.end;
                this.movePiece(begin, end);
                this.turnMessage();
                break;
            case "ERROR":
                this.writeResponse(data.message);
                break;
            case "ENDGAME":
                this.writeResponse("Connection closed.");
                this.ws.close();
                break;
        }
        this.setEvents();
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
        this.ws.onmessage = this.onMessage;
        this.ws.onerror = this.onError;
        this.ws.onclose = this.onClose;
    }
}
let gui = new GUI();
gui.init();