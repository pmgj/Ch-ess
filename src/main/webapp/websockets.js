function Connection() {
    this.ws = null;
    var _self = this;
    this.onOpen = function () {
        _self.writeResponse("Aguardando jogador...");
    };
    this.onMessage = function (evt) {
        var data = JSON.parse(evt.data);
        switch (data.type) {
            case "OPEN":
                cor = data.color;
                corDaVez = data.turn;
                turnMessage();
                break;
            case "MESSAGE":
                corDaVez = data.turn;
                var begin = data.begin;
                var end = data.end;
                movePiece(begin, end);
                turnMessage();
                break;
            case "ERROR":
                _self.writeResponse(data.message);
                break;
            case "ENDGAME":
                _self.writeResponse("Connection closed.");
                this.ws.close();
                break;
        }
        // if (data.type === 0) {
        //     cor = data.color;
        //     corDaVez = data.turn;
        //     turnMessage();
        // } else if (data.type === 1) {
        //     corDaVez = data.turn;
        //     var begin = data.begin;
        //     var end = data.end;
        //     movePiece(begin, end);
        //     turnMessage();
        // } else if (data.type === 2) {
        //     _self.writeResponse(data.message);
        // } else if (data.type === 3) {
        //     _self.writeResponse("Connection closed.");
        //     this.ws.close();
        // }
        setEvents();
    };
    this.onClose = function () {
        _self.writeResponse("Connection closed.");
    };
    this.onError = function (evt) {
        _self.writeResponse("error: " + evt.data);
    };
    this.writeResponse = function (text) {
        var messages = document.getElementById("results");
        messages.innerHTML = text;
    };
    this.openConnection = function () {
        var wsUri = "ws://" + document.location.host + document.location.pathname + "chess";
        this.ws = new WebSocket(wsUri);
        this.ws.binaryType = "arraybuffer";
        this.ws.onopen = this.onOpen;
        this.ws.onmessage = this.onMessage;
        this.ws.onerror = this.onError;
        this.ws.onclose = this.onClose;
    };
}
onload = function () {
    conn = new Connection();
    conn.openConnection();
};