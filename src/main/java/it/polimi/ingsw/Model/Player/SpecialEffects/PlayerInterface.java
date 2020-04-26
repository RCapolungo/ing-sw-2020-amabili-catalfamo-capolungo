package it.polimi.ingsw.Model.Player.SpecialEffects;

import it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.BoardCell;
import it.polimi.ingsw.Model.God.God;
import it.polimi.ingsw.Model.PlayerFSA.*;
import it.polimi.ingsw.Model.Worker;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlayerInterface {


    PlayerFSA playerState = null;

    String nickname = null;
    List<Worker> workerRef = null;

    God activeCard = null;
    Board board = null;

    void setWorkerRef(List<Worker> list);

    List<Worker> getWorkerRef();

    void setActiveCard(God ActiveCard);

    God getActiveCard();

    Board getBoard();

    void setBoard(Board board);

    void setNickname(String nickname);

    String getNickname();

    void setChosenGods(List<God> gods);

    List<God> getChosenGods();

    boolean isMoveUp();

    void setMoveUp(boolean moveUp);

    boolean addWorker(int row, int col, Worker worker);

    List<BoardCell> availableCellsToMove(@NotNull Worker worker);

    List<BoardCell> availableCellsToMove(@NotNull Worker worker, boolean specialEffect);

    boolean move(int row, int col, @NotNull Worker worker);

    List<BoardCell> availableCellsToBuild(@NotNull Worker worker);

    List<BoardCell> availableCellsToBuild(@NotNull Worker worker, boolean specialEffect);

    boolean build(int row, int col, @NotNull Worker worker);

    boolean build(int row, int col, @NotNull Worker worker, boolean specialEffect);

    boolean build(int row1, int col1, @NotNull Worker worker, int row2, int col2);

    boolean checkWin(@NotNull Worker worker);

    boolean move(int row, int col, @NotNull Worker worker, boolean specialEffect, int rowBuild, int colBuild);

    PlayerFSA getPlayerState();

    void setPlayerState(PlayerFSA playerState);

    void StateMove(int row, int col, Worker worker);

    void StateBuild(int row, int col, Worker worker);

    void addNickname(String nickname);

    void setCard(God godName);

    void PlaceWorker(int row, int col, Worker worker);

}
