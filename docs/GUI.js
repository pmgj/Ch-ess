import State from './State.js';
import Chess from './Chess.js';

class GUI {
    constructor() {
        this.game = new Chess();
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
                    td.appendChild(img);
                }
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
    }
}
let gui = new GUI();
gui.init();