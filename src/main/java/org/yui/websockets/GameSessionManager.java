package org.yui.websockets;

import jakarta.enterprise.context.ApplicationScoped;
import org.yui.service.HangmanService;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class GameSessionManager {
    private final Map<String, HangmanService> activeGames = new HashMap<>();

    public HangmanService getGame(String roomName) {
        return activeGames.computeIfAbsent(roomName, room -> new HangmanService());
    }
}
