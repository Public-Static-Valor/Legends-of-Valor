package com.legends.ai;

import com.legends.model.Monster;
import com.legends.gameFiles.ValorBoard;
import com.legends.io.Output;


/**
 * Strategy pattern will be used through this interface.
 */
public interface MonsterAI {

    /**
     * Perform exactly one monster action this turn.
     */
    void takeTurn(Monster monster, ValorBoard board, Output output);
}
