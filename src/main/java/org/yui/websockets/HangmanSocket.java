package org.yui.websockets;

import io.quarkus.websockets.next.*;
import jakarta.inject.Inject;
import org.yui.service.HangmanService;

@WebSocket(path = "/ws/hangman/{room}/{username}")
public class HangmanSocket {
    public enum MessageType {USER_JOINED, USER_LEFT, CHAT_MESSAGE, GUESS_LETTER, GAME_STATE, START_GAME}
    public record ChatMessage(MessageType type, String room, String from, String message) {
    }

    @Inject
    WebSocketConnection connection;

    @Inject
    HangmanService hangmanService;

    @OnOpen(broadcast = true)
    public ChatMessage onOpen() {
        sendGameState();
        return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("room"), connection.pathParam("username"), null);
    }

    @OnClose
    public void onClose() {
        ChatMessage departure = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("room"), connection.pathParam("username"), null);
        connection.broadcast().sendTextAndAwait(departure);
    }

    @OnTextMessage(broadcast = true)
    public ChatMessage onMessage(ChatMessage message) {
        if (message.type == MessageType.GUESS_LETTER) {
            hangmanService.addGuess(message.message);
            sendGameState();
        } else if (message.type == MessageType.START_GAME) {
            hangmanService.startGame();
            sendGameState();
        }
        return message;
    }

    public void sendGameState() {
        String state = hangmanService.printGameState();
        ChatMessage message = new ChatMessage(
            MessageType.GAME_STATE,
            connection.pathParam("room"),
            "SYSTEM",
            state
        );
        connection.broadcast().sendTextAndAwait(message);
    }
}
