package org.yui;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.yui.service.HangmanService;

@Path("/hangman")
public class HangmanResource {
    @Inject
    HangmanService hangmanService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hangman() {
        return hangmanService.printGameState();
    }

    @POST
    @Path("/start")
    public Response startGame() {
        hangmanService.startGame();
        return Response.ok().build();
    }

    @POST
    @Path("/{guess}")
    public Response guessWord(@PathParam("guess") String guess) {
        if (guess.length() != 1) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Can only guess a letter").build();
        }
        if (hangmanService.isGameOver()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Game is over").build();
        }
        if (hangmanService.isGuessed(guess)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This was already guessed").build();
        }
        hangmanService.addGuess(guess);
        return Response.ok().build();
    }
}
