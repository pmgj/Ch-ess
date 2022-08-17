var conn, table, VAZIA = -1, SIZE = 8, BRANCA = 0, PRETA = 1, origem = null, corDaVez = BRANCA, cor = null;
function isCell(row, col) {
    if (row < SIZE && row >= 0 && col < SIZE && col >= 0) {
        return true;
    } else {
        return false;
    }
}
function setClass(row, col) {
    if (isCell(row, col)) {
        var celula = table.rows[row].cells[col];
        celula.className = "selecionado";
    }
}
function corDaPeca(row, col) {
    if (col === undefined) {
        col = row.cellIndex;
        row = row.parentNode.rowIndex;
    }
    var cell = table.rows[row].cells[col];
    var x = cell.firstChild;
    if (x) {
        if (x.src.indexOf("Branc") !== -1) {
            return BRANCA;
        } else {
            return PRETA;
        }
    } else {
        return VAZIA;
    }
}
function posicaoValida(row, col, cor) {
    if (isCell(row, col)) {
        var color = corDaPeca(row, col);
        if (color === VAZIA) {
            setClass(row, col);
            return true;
        } else if (color !== cor) {
            setClass(row, col);
            return false;
        } else {
            return false;
        }
    } else {
        return false;
    }
}
function posicoesHV(row, col) {
    var cor = corDaPeca(row, col);
    for (var j = -1; j <= 1; j += 2) {
        // Vertical
        for (var i = row + j; i >= 0; i += j) {
            if (!posicaoValida(i, col, cor)) {
                break;
            }
        }
        // Horizontal
        for (var i = col + j; i >= 0; i += j) {
            if (!posicaoValida(row, i, cor)) {
                break;
            }
        }
    }
}
function posicoesDG(row, col) {
    var cor = corDaPeca(row, col);
    var lin = [-1, 1];
    for (var i = 0; i < lin.length; i++) {
        for (var j = 0; j < lin.length; j++) {
            for (var k = row + lin[i], l = col + lin[j]; k >= 0 && k < SIZE && l >= 0 && l < SIZE; k += lin[i], l += lin[j]) {
                if (!posicaoValida(k, l, cor)) {
                    break;
                }
            }
        }
    }
}
function posicoesPossiveis(cell) {
    var x = cell.firstChild;
    var row = cell.parentNode.rowIndex;
    var col = cell.cellIndex;
    if (x) {
        if (x.src.indexOf("Peao") !== -1) {
            if (x.src.indexOf("Branc") !== -1) {
                if (corDaPeca(row - 1, col) === VAZIA) {
                    setClass(row - 1, col);
                }
                if (row === SIZE - 2 && corDaPeca(row - 2, col) === VAZIA) {
                    setClass(row - 2, col);
                }
                if (isCell(row - 1, col - 1) && corDaPeca(row - 1, col - 1) === PRETA) {
                    setClass(row - 1, col - 1);
                }
                if (isCell(row - 1, col + 1) && corDaPeca(row - 1, col + 1) === PRETA) {
                    setClass(row - 1, col + 1);
                }
            } else if (x.src.indexOf("Pret") !== -1) {
                if (corDaPeca(row + 1, col) === VAZIA) {
                    setClass(row + 1, col);
                }
                if (row === 1 && corDaPeca(row + 2, col) === VAZIA) {
                    setClass(row + 2, col);
                }
                if (isCell(row + 1, col - 1) && corDaPeca(row + 1, col - 1) === BRANCA) {
                    setClass(row + 1, col - 1);
                }
                if (isCell(row + 1, col + 1) && corDaPeca(row + 1, col + 1) === BRANCA) {
                    setClass(row + 1, col + 1);
                }
            }
        } else if (x.src.indexOf("Torre") !== -1) {
            posicoesHV(row, col);
        } else if (x.src.indexOf("Cavalo") !== -1) {
            var lin = [-2, 2, -1, 1], inc = [[-1, 1], [-1, 1], [-2, 2], [-2, 2]];
            for (var i = 0; i < lin.length; i++) {
                for (var j = 0; j < inc[i].length; j++) {
                    var a = row + lin[i], b = col + inc[i][j];
                    if (isCell(a, b) && corDaPeca(a, b) !== corDaPeca(row, col)) {
                        setClass(a, b);
                    }
                }
            }
        } else if (x.src.indexOf("Bispo") !== -1) {
            posicoesDG(row, col);
        } else if (x.src.indexOf("Rainha") !== -1) {
            posicoesDG(row, col);
            posicoesHV(row, col);
        } else if (x.src.indexOf("Rei") !== -1) {
            for (var i = -1; i < 2; i++) {
                for (var j = -1; j < 2; j++) {
                    var a = row + i, b = col + j;
                    if (isCell(a, b) && corDaPeca(a, b) !== corDaPeca(row, col)) {
                        setClass(a, b);
                    }
                }
            }
        }
    }
}
function limparPosicoes() {
    var rows = table.rows;
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        for (var j = 0; j < row.cells.length; j++) {
            var cell = row.cells[j];
            cell.className = "";
        }
    }
}
function jogar() {
    var corPeca = corDaPeca(this);
    if (origem === null) {
        if (corPeca === corDaVez && cor === corDaVez && this.firstChild !== null) {
            this.className = "selecionado";
            posicoesPossiveis(this);
            origem = this;
        }
    } else {
        if (this.className === "selecionado") {
            limparPosicoes();
            if (origem !== this) {
                movePiece2(origem, this);
            }
            origem = null;
        }
    }
}
function movePiece(begin, end) {
    corDaVez = (corDaVez + 1) % 2;
    var x = table.rows[begin.row].cells[begin.col];
    var y = table.rows[end.row].cells[end.col];
    var tmp = x.firstChild.cloneNode(true);
    if (y.firstChild)
        y.removeChild(y.firstChild);
    y.appendChild(tmp);
    x.removeChild(x.firstChild);
}
function movePiece2(begin, end) {
    var msg = {type: 1, turn: corDaVez, begin: {row: begin.parentNode.rowIndex, col: begin.cellIndex}, end: {row: end.parentNode.rowIndex, col: end.cellIndex}};
    conn.ws.send(JSON.stringify(msg));
    corDaVez = (corDaVez + 1) % 2;
    var tmp = begin.firstChild.cloneNode(true);
    if (end.firstChild)
        end.removeChild(end.firstChild);
    end.appendChild(tmp);
    begin.removeChild(begin.firstChild);
    turnMessage();
}
function drag(ev) {
    var corPeca = corDaPeca(this.parentNode);
    if (corPeca === corDaVez && cor === corDaVez) {
        posicoesPossiveis(this.parentNode);
        origem = this;
        ev.dataTransfer.effectAllowed = 'move';
        ev.dataTransfer.setData("text/html", ev.target.outerHTML);
    } else {
        ev.preventDefault();
    }
}
function allowDrop(ev) {
    ev.preventDefault();
}
function drop(ev) {
    ev.preventDefault();
    if (this.className === "selecionado") {
        if ((this.firstChild === null) || (this.firstChild && origem !== this.firstChild)) {
            movePiece2(origem.parentNode, this);
        }
    }
    limparPosicoes();
    origem = null;
}
function setEvents() {
    table = document.getElementsByTagName("table")[0];
    var cells = document.getElementsByTagName("td");
    Array.prototype.forEach.call(cells, function (cell) {
        cell.onclick = jogar;
        var x = cell.firstChild;
        if (x) {
            x.draggable = true;
            x.ondragstart = drag;
        }
        cell.ondragover = allowDrop;
        cell.ondrop = drop;
    });
}
function turnMessage() {
    if (cor === corDaVez) {
        conn.writeResponse("Its your turn.");
    } else {
        conn.writeResponse("Wait for your turn.");
    }
}