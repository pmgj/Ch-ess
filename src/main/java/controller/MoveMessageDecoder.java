package controller;

import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

public class MoveMessageDecoder implements Decoder.Text<MoveMessage> {
    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public MoveMessage decode(final String textMessage) throws DecodeException {
        return JsonbBuilder.create().fromJson(textMessage, MoveMessage.class);
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }    
}
