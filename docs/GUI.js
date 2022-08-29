import State from './State.js';
import Chess from './Chess.js';
import Cell from './Cell.js';

class GUI {
    constructor() {
        this.game = new Chess();
        this.origin = null;
    }
    init() {
        let tab = this.game.getBoard();
        let tbody = document.querySelector("tbody");
        for (let i = 0; i < tab.length; i++) {
            let tr = document.createElement("tr");
            for (let j = 0; j < tab[i].length; j++) {
                let td = document.createElement("td");
                if (tab[i][j].state !== State.EMPTY) {
                    let img = document.createElement("img");
                    img.src = `images/${tab[i][j].piece}-${tab[i][j].state}.svg`;
                    img.ondragstart = this.drag.bind(this);
                    td.appendChild(img);
                }
                td.onclick = this.play.bind(this);
                td.ondragover = this.allowDrop;
                td.ondrop = this.drop.bind(this);
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
        this.changeMessage();
    }
    coordinates(cell) {
        return new Cell(cell.parentNode.rowIndex, cell.cellIndex);
    }
    setMessage(message) {
        let msg = document.getElementById("message");
        msg.textContent = message;
    }
    changeMessage() {
        let msgs = { PLAYER1: "White's turn.", PLAYER2: "Black's turn." };
        this.setMessage(msgs[this.game.getTurn()]);
    }
    play(evt) {
        let td = evt.currentTarget;
        if (this.origin === null) {
            this.origin = td;
            this.showPossibleMoves(this.origin);
        } else {
            this.innerPlay(this.origin, td);
        }
    }
    drag(evt) {
        this.origin = evt.currentTarget.parentNode;
        this.showPossibleMoves(this.origin);
    }
    allowDrop(evt) {
        evt.preventDefault();
    }
    drop(evt) {
        evt.preventDefault();
        this.innerPlay(this.origin, evt.currentTarget);
    }
    innerPlay(beginCell, endCell) {
        this.hidePossibleMoves();
        let begin = this.coordinates(beginCell);
        let end = this.coordinates(endCell);
        try {
            this.game.move(begin, end);
            let image = beginCell.firstChild;
            endCell.innerHTML = "";
            endCell.appendChild(image);
            this.changeMessage();
        } catch(ex) {
            this.setMessage(ex.message);
        }
        this.origin = null;
    }
    showPossibleMoves(cell) {
        let coords = this.coordinates(cell);
        let moves = this.game.possibleMoves(coords);
        moves.push(coords);
        for (let {x, y} of moves) {
            let tempCell = document.querySelector(`tr:nth-child(${x + 1}) td:nth-child(${y + 1})`);
            tempCell.className = 'selected';
        }
        if (moves.length === 1) {
            this.setMessage("No possible moves for this piece. ");
        }
    }
    hidePossibleMoves() {
        let cells = document.querySelectorAll("td");
        cells.forEach(c => c.className = '');
    }
}
let gui = new GUI();
gui.init();