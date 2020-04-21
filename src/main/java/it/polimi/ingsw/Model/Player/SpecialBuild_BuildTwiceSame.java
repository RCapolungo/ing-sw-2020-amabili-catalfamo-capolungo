package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;



public class SpecialBuild_BuildTwiceSame extends PlayerDecorator {

    public SpecialBuild_BuildTwiceSame(PlayerInterface p) {
        super(p);
    }

    /**Builds twice on the same BoardCell
     * @param row BoardCell row
     * @param col BoardCell col
     * @param worker Worker used
     * @param specialEffect true <--> the effect has to be enabled</-->
     * @return true <--> the method works </-->
     */

    public boolean build(int row, int col, @NotNull Worker worker, boolean specialEffect) {
        if (specialEffect) {
            if (availableCellsToBuild(worker, true).contains(worker.getBoard().getGrid()[row][col])) {
                BoardCell b = worker.getBoard().getGrid()[row][col];
                b.setLevel((b.getLevel() + 2));
                return true;
            }
        }
        return false;
    }


    public List<BoardCell> availableCellsToBuild(@NotNull Worker worker, boolean specialEffect) {
        List<BoardCell> adj = worker.getBoard().adjacentCells(worker.getCurCell());
        adj.removeIf((n) -> n.getWorker() != null);
        adj.removeIf(BoardCell::getDome);
        adj.removeIf((n) -> n.getLevel() == 2);
        return adj;

    }


}