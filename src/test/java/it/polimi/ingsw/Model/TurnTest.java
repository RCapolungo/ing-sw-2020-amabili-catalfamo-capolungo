package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Player.SpecialEffects.PlayerInterface;
import it.polimi.ingsw.Model.PlayerFSA.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TurnTest {

    final Game game = new Game();
    Board board = new Board();
    List<Worker> list = new ArrayList<>();

    @Before
    public void setUp() {
        game.getOnlinePlayers().clear();
    }


    @Test
    public void TurnTest()  {


        //NextTurn() when added nickname
        setUp();
        int i = 0;
        Turn turn2 = new Turn(game.getOnlinePlayers());
        game.setCurrentTurn(turn2);
        game.initialiseMatch(3);
        game.getStateList().get(0).addNickname("Rexo");
        assertEquals(new Idle(game.getOnlinePlayers().get(0), new AddNickname(game.getOnlinePlayers().get(0), game), game), game.getStateList().get(0));
        game.getStateList().get(1).addNickname("Rexo");
        assertEquals(new AddNickname(game.getOnlinePlayers().get(1), game), game.getStateList().get(1));
        game.getStateList().get(1).addNickname("NotATeen");
        assertEquals(new Idle(game.getOnlinePlayers().get(1), new AddNickname(game.getOnlinePlayers().get(1), game), game), game.getStateList().get(0));
        game.getStateList().get(2).addNickname("Walter");
        for (PlayerInterface p : turn2.getActivePlayers()) {
            if (turn2.getCurrentPlayer() == p) {
                i = turn2.getActivePlayers().indexOf(p);
            }
        }
        assertTrue(game.getStateList().contains(new Initialized(game.getOnlinePlayers().get(i), game)));

        //NextTurn() when choose cards

        game.getStateList().get(i).chosenCard("Apollo");
        game.getStateList().get(i).chosenCard("Artemis");
        game.getStateList().get(i).chosenCard("Artemis");
        assertEquals(new Initialized(game.getOnlinePlayers().get(i), game), game.getStateList().get(i));
        game.getStateList().get(i).chosenCard("artemis");
        assertEquals(new Initialized(game.getOnlinePlayers().get(i), game), game.getStateList().get(i));
        game.getStateList().get(i).chosenCard("Athena");
        game.getCurrentTurn().nextTurn(game);
        if (i == 2) {
            assertEquals(new SetCard(turn2.getActivePlayers().get(0), game), game.getStateList().get(0));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(0));
            assertEquals(new Idle(turn2.getActivePlayers().get(2), new AddNickname(turn2.getActivePlayers().get(2), game), game), game.getStateList().get(2));
        } else {
            assertEquals(new SetCard(turn2.getActivePlayers().get(i + 1), game), game.getStateList().get(i + 1));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(i + 1));
            assertEquals(new Idle(turn2.getActivePlayers().get(i), new AddNickname(turn2.getActivePlayers().get(i), game), game), game.getStateList().get(i));
        }

        //NextTurn() when set card
        for (PlayerInterface p : turn2.getActivePlayers()) {
            if (turn2.getCurrentPlayer() == p) {
                i = turn2.getActivePlayers().indexOf(p);
            }
        }
        game.getStateList().get(i).setCard("Apollo");
        if (i == 2) {
            assertEquals(new SetCard(turn2.getActivePlayers().get(0), game), game.getStateList().get(0));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(0));
            assertEquals(new Idle(turn2.getActivePlayers().get(2), new SetCard(turn2.getActivePlayers().get(2), game), game), game.getStateList().get(2));
        } else {
            assertEquals(new SetCard(turn2.getActivePlayers().get(i + 1), game), game.getStateList().get(i + 1));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(i + 1));
            assertEquals(new Idle(turn2.getActivePlayers().get(i), new SetCard(turn2.getActivePlayers().get(i), game), game), game.getStateList().get(i));
        }

        //A full turn passes
        if (i == 2) {
            game.getStateList().get(0).setCard("artemis");
            assertEquals(new SetCard(game.getOnlinePlayers().get(0), game), game.getStateList().get(0));
            game.getStateList().get(0).setCard("Artemis");
            game.getStateList().get(1).setCard("Athena");
        }
        if (i == 1) {
            game.getStateList().get(2).setCard("artemis");
            assertEquals(new SetCard(game.getOnlinePlayers().get(2), game), game.getStateList().get(2));
            game.getStateList().get(2).setCard("Artemis");
            game.getStateList().get(0).setCard("Athena");
        }
        if (i == 0) {
            game.getStateList().get(1).setCard("artemis");
            assertEquals(new SetCard(game.getOnlinePlayers().get(1), game), game.getStateList().get(1));
            game.getStateList().get(1).setCard("Artemis");
            game.getStateList().get(2).setCard("Athena");
        }

        //NextTurn() when added worker on board
        for (PlayerInterface p : turn2.getActivePlayers()) {
            if (turn2.getCurrentPlayer() == p) {
                i = turn2.getActivePlayers().indexOf(p);
            }
        }
        assertEquals(new PlaceWorker(turn2.getCurrentPlayer(), game), game.getStateList().get(i));
        game.getStateList().get(i).placeWorker(1, 1, 0);
        game.getStateList().get(i).placeWorker(1, 1, 1);
        assertEquals(new PlaceWorker(game.getOnlinePlayers().get(i), game), game.getStateList().get(i));
        game.getStateList().get(i).placeWorker(2, 2, 1);
        assertEquals(new Idle(turn2.getActivePlayers().get(i), new PlaceWorker(turn2.getActivePlayers().get(i), game), game), game.getStateList().get(i));
        if (i == 2) {
            assertEquals(new PlaceWorker(turn2.getActivePlayers().get(0), game), game.getStateList().get(0));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(0));
            assertEquals(new Idle(turn2.getActivePlayers().get(2), new PlaceWorker(turn2.getActivePlayers().get(2), game), game), game.getStateList().get(2));
        } else {
            assertEquals(new PlaceWorker(turn2.getActivePlayers().get(i + 1), game), game.getStateList().get(i + 1));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(i + 1));
            assertEquals(new Idle(turn2.getActivePlayers().get(i), new PlaceWorker(turn2.getActivePlayers().get(i), game), game), game.getStateList().get(i));
        }

        //A full turn passes
        if (i == 2) {
            game.getStateList().get(0).placeWorker(3, 3, 0);
            game.getStateList().get(0).placeWorker(4, 4, 1);
            game.getStateList().get(1).placeWorker(5, 5, 0);
            game.getStateList().get(1).placeWorker(1, 5, 1);
        }
        if (i == 1) {
            game.getStateList().get(2).placeWorker(3, 3, 0);
            game.getStateList().get(2).placeWorker(4, 4, 1);
            game.getStateList().get(0).placeWorker(5, 5, 0);
            game.getStateList().get(0).placeWorker(1, 5, 1);
        }
        if (i == 0) {
            game.getStateList().get(1).placeWorker(3, 3, 0);
            game.getStateList().get(1).placeWorker(4, 4, 1);
            game.getStateList().get(2).placeWorker(5, 5, 0);
            game.getStateList().get(2).placeWorker(1, 5, 1);
        }


        game.getBoard().printGrid();
        game.getBoard().printAvailableGrid(game.getCurrentTurn().getCurrentPlayer().availableCellsToMove(game.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(1)));

        //NextTurn() when moved worker and built building
        for (PlayerInterface p : turn2.getActivePlayers()) {
            if (turn2.getCurrentPlayer() == p) {
                i = turn2.getActivePlayers().indexOf(p);
            }
        }
        assertEquals(new Moving(turn2.getCurrentPlayer(), game), game.getStateList().get(i));
        game.getStateList().get(i).move(3, 3, 1);
        assertEquals(new Moving(turn2.getCurrentPlayer(), game), game.getStateList().get(i));
        game.getStateList().get(i).move(1, 2, 1);
        assertEquals(new Building(turn2.getCurrentPlayer(), game), game.getStateList().get(i));
        game.getStateList().get(i).build(2, 2, 1);
        assertEquals(new Building(turn2.getCurrentPlayer(), game), game.getStateList().get(i));
        game.getStateList().get(i).build(1, 3, 1);
        if (i == 2) {
            assertEquals(new Moving(turn2.getActivePlayers().get(0), game), game.getStateList().get(0));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(0));
            assertEquals(new Idle(turn2.getActivePlayers().get(2), new Building(turn2.getActivePlayers().get(2), game), game), game.getStateList().get(2));
        } else {
            assertEquals(new Moving(turn2.getActivePlayers().get(i + 1), game), game.getStateList().get(i + 1));
            assertEquals(turn2.getCurrentPlayer(), turn2.getActivePlayers().get(i + 1));
            assertEquals(new Idle(turn2.getActivePlayers().get(i), new Building(turn2.getActivePlayers().get(i), game), game), game.getStateList().get(i));
        }

    }

    @Test
    public void test() {
        Game game = new Game();
        Turn turn = new Turn(game.getOnlinePlayers());
        game.setCurrentTurn(turn);
        game.initialiseMatch(2);
        turn.setCurrentPlayer(game.getOnlinePlayers().get(0));
        game.getStateList().get(0).addNickname("Rexo");
        assertEquals(new Idle(game.getOnlinePlayers().get(0), new AddNickname(game.getOnlinePlayers().get(0), game), game), game.getStateList().get(0));
        assertEquals(new AddNickname(game.getOnlinePlayers().get(1), game), game.getStateList().get(1));
        game.getStateList().get(1).addNickname("NotATeen");
        for (int i = 0; i < game.getStateList().size(); i++) {
            game.getStateList().set(i, new Idle(game.getOnlinePlayers().get(i), new AddNickname(game.getOnlinePlayers().get(i), game), game));
        }
        turn.firstTurn(1, game);
        Assert.assertEquals("Rexo", turn.getCurrentPlayer().getNickname());
        game.getStateList().set(0, new Initialized(game.getOnlinePlayers().get(0), game));
        assertEquals(new Initialized(game.getOnlinePlayers().get(0), game), game.getStateList().get(0));
        game.getStateList().get(0).chosenCard("Artemis");
        game.getStateList().get(0).chosenCard("Demeter");
        game.getStateList().set(0, new SetCard(game.getOnlinePlayers().get(0), game));
        assertEquals(new SetCard(turn.getCurrentPlayer(), game), game.getStateList().get(0));
        game.getStateList().get(0).setCard("Artemis");
        game.getStateList().set(0, new SetCard(game.getOnlinePlayers().get(0), game));
        game.getStateList().get(1).setCard("Demeter");
        assertEquals(new PlaceWorker(turn.getCurrentPlayer(), game), game.getStateList().get(0));
        game.getStateList().get(0).placeWorker(3, 3, 0);
        game.getStateList().get(0).placeWorker(4, 4, 1);
        assertEquals(new PlaceWorker(turn.getCurrentPlayer(), game), game.getStateList().get(1));
        game.getStateList().get(1).placeWorker(5, 5, 0);
        game.getStateList().get(1).placeWorker(1, 5, 1);
        game.getOnlinePlayers().get(0).setEnableSpecialMove(true);
        assertEquals(new Moving(turn.getCurrentPlayer(), game), game.getStateList().get(0));
        game.getStateList().get(0).move(3, 4, 3,5, 1);
        assertEquals(new Building(turn.getCurrentPlayer(), game), game.getStateList().get(0));
        game.getStateList().get(0).build(2, 5, 1);
        assertEquals(new Moving(turn.getCurrentPlayer(), game), game.getStateList().get(1));
        game.getStateList().get(1).move(4,5, 1);
        game.getOnlinePlayers().get(1).setEnableSpecialBuild(true);
        assertEquals(new Building(turn.getCurrentPlayer(), game), game.getStateList().get(1));
        game.getStateList().get(1).build(5,5,5,4,1);

    }







}