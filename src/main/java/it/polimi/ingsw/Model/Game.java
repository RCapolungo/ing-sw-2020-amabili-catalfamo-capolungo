package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.GameIsAlreadyStarted;
import it.polimi.ingsw.Model.God.God;
import it.polimi.ingsw.Model.God.GodFileCreator;
import it.polimi.ingsw.Model.Player.*;
import it.polimi.ingsw.Model.Player.SpecialEffects.PlayerInterface;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Game extends Observable {

    //Player
    private int id;
    private List<String> nickNames; //in game players
    private List<PlayerInterface> onlinePlayers;
    private Turn currentTurn;
    private int counterId = 1;
    private Board board;
    private boolean cardsChosen = false;

    private List<String> godListNames = new ArrayList<>();
    public List<String> getGodListNames() {
        return godListNames;
    }

    public void initialiseGodList(){
        PlayerCreator playerCreator = new PlayerCreator();
        for (int i=0; i<playerCreator.getArrayGods().size(); i++)
            godListNames.add(playerCreator.getArrayGods().get(i).getGodName());
    }
    private List<God> chosenGods = new ArrayList<>();



    public Game() {
        nickNames = new ArrayList<>();
        onlinePlayers = new ArrayList<>();
    }



    public List<God> getChosenGods() {
        return chosenGods;
    }
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public List<PlayerInterface> getOnlinePlayers() {
        return onlinePlayers;
    }
    public List<String> getNicknames() {
        return nickNames;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Turn getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }
    public String getPlayerNickname(int num){
        return getOnlinePlayers().get(num).getNickname();
    }



    /**
     * Delete the chosen player and all its workers
     * @param player
     */
    public void delPlayer(@NotNull PlayerInterface player){

        for (Worker x : player.getWorkerRef()){
            x.getCurCell().setWorker(null);
        }
        player.getWorkerRef().clear();
        onlinePlayers.remove(player);
        currentTurn.getActivePlayers().remove(player);

    }

    /**
     *Create a list of Worker to match the Player
     *Create a player
     *Create a Board
     */
    public void initialiseMatch() { //TODO: add player
        List<Worker> list = new ArrayList<>();
        Board board = new Board();
        setBoard(board);
        int MAXPLAYER = 3;
        for (int i = 0; i < MAXPLAYER; i++) {
            onlinePlayers.add(new Player());
        }
        for (PlayerInterface playerInterface : onlinePlayers) {
            for (int i = 0; i < 2; i++, counterId++) {
                Worker worker = new Worker(counterId);
                worker.setPlayerWorker(playerInterface);
                list.add(worker);
            }
            playerInterface.setWorkerRef(list);
            playerInterface.setBoard(board);
            list.clear();
        }
       initialiseGodList();

    }

    /**
     * Check if the Worker won
     * @param worker
     */
    public void win(@NotNull Worker worker) {
        if(worker.getPlayerWorker().checkWin(worker) || onlinePlayers.size() == 1) {
            System.out.println(worker.getPlayerWorker().getNickname() + "wins");
        }
    }

    /**
     * Add nickname chosen to the list of OnlinePlayers
     * @param nickName
     */
    public void addNickname(String nickName) {

        for(PlayerInterface p : onlinePlayers ) {
            if (p.getNickname() == null) {
                this.getCurrentTurn().getCurrentPlayer().setNickname(nickName);
                this.notifyPlayerAdded(nickName);
                break;
            } else {
                if(!p.getNickname().equals(nickName)){
                    this.getCurrentTurn().getCurrentPlayer().setNickname(nickName);
                }
                else{
                    this.notifyNicknameNotValid();
                }
            }

        }
       //if lista nomi contains nome return false, se return false devi rifare il metodo

       //this.getCurrentTurn().getCurrentPlayer().getPlayerState().addNickname(nickName);


       //this.getCurrentTurn().nextTurn();

    }

    /**
     * Create a turn with the online Players
     */
    public void createTurn() {
        Turn turn = new Turn(this.getOnlinePlayers());
        this.setCurrentTurn(turn);
        this.getCurrentTurn().setCurrentPlayer(this.getOnlinePlayers().get(0));
        this.notifyGameIsRead();
    }

    /**
     * Set the chosen god as ActiveGodCard
     * Remove the chosenGod from the list of ChosenGods
     * @param godName
     */
    public void setGod(String godName) {

        PlayerCreator playerCreator = new PlayerCreator();

        God god = new God(godName, null);
        //chosenGods.remove(god);

        this.getCurrentTurn().getCurrentPlayer().setActiveCard(god);

        PlayerInterface p1 = this.getCurrentTurn().getCurrentPlayer();
        p1 = playerCreator.createPlayer(godName, p1);

        this.getCurrentTurn().setCurrentPlayer(p1);

        //TODO: sostituisci in active player game e turn

        
        //this.getCurrentTurn().getCurrentPlayer().getPlayerState().setCard(god);
        this.notifyGodSetted(this.getCurrentTurn().getCurrentPlayer(), godName);
        //this.getCurrentTurn().nextTurn();

    }

    /**
     * This method check if someone else has already chosen cards
     */
    public void chooseCards() {

        notifyChoose(cardsChosen, this.getGodListNames());

    }

    /**
     * Check if the GodName input is correct by checking if he is written correctly
     * @param godName
     */
    public void checkAndAdd(String godName){

        God god = new God(godName, null);
        if(this.getGodListNames().contains(godName)) {
            if(chosenGods.isEmpty()) {
                chosenGods.add(god);
            } else {
                for (God g : chosenGods) {
                    if (!g.getGodName().equals(godName)) {
                        chosenGods.add(god);
                        break;
                    }
                }
            }
            if (chosenGods.size() == this.getCurrentTurn().getActivePlayers().size()) {
                cardsChosen = true;
                notifyGodAdded(this.getChosenGods(), cardsChosen);
            } else {
                notifyGodAdded(this.getChosenGods(), cardsChosen);
            }
        }
        else {
            notifyGodNotAdded();
        }



    }

    /**
     * This notify sends the view a general exception.
     */
    public void notifyExc(){

        this.notifyObservers(null, null);
    }

    public void addingWorker(int row, int col, int i){

        if(this.getCurrentTurn().getCurrentPlayer().addWorker(row-1, col-1,this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(i)))

            this.notifyBoardUpdate(this.getBoard());

        else {
            if(i == 0) {
                i = -1;
            } else {
                i = 0;
            }
            notifyCellAlreadyOccupied(i);
        }
        //this.getCurrentTurn().getCurrentPlayer().getPlayerState().placeWorker(row, col, this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(i));


    }

    public void canIMove(){


        int i = 0;
        if(this.getCurrentTurn().checkLockPlayer(this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(i)) && this.getCurrentTurn().checkLockPlayer(this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(i+1)) ){
            notifyCanMove(null);
        }
        else {
            //TODO: notifyPlayerJustLose
        }


    }

    public void checkWorker(int worker) {

        if (this.getCurrentTurn().checkLockPlayer(this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(worker - 1))) {
            if (worker == 2) {
                worker = 1;
            } else {
                worker++;
            }
            notifyCanMove(worker);
        }
    }

    public void moving(int row, int col, int worker){

        if(!this.getCurrentTurn().getCurrentPlayer().move(row - 1, col - 1, this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(worker-1))){
            notifyTryNewCoordinatesMove(false);
        }
        else    {
            notifyBoardUpdate(this.board);
        }


    }

    public void building(int row, int col, int worker){

        if(!this.getCurrentTurn().getCurrentPlayer().build(row - 1, col - 1, this.getCurrentTurn().getCurrentPlayer().getWorkerRef().get(worker-1))){
            notifyTryNewCoordinatesBuild(false);
        }
        else    {

            notifyBoardUpdate(this.board);
        }


    }

    /**
     * This notify the view that the God chosen is not in the list of the God that the Challenger chose
     */
    public void GodNotCorrectException(){

        notifyGodNotCorrect(chosenGods);

    }



}

