package com.legends.ai;

import com.legends.model.Monster;
import com.legends.board.ValorBoard;
import com.legends.io.Output;
import java.io.Serializable;

import java.util.List;
import com.legends.model.Hero;

/**
 * Strategy pattern will be used through this interface.
 */
public interface MonsterAI extends Serializable {

    /**
     * Perform exactly one monster action this turn.
     */
    void takeTurn(Monster monster, ValorBoard board, Output output);

    /**
     * Perform a battle action (attack) when not on the board.
     */
    void takeBattleTurn(Monster monster, List<Hero> heroes, Output output);
}
