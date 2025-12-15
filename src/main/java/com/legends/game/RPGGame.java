package com.legends.game;

import com.legends.io.Input;
import com.legends.io.Output;
import com.legends.model.DragonFactory;
import com.legends.model.ExoskeletonFactory;
import com.legends.model.SpiritFactory;
import com.legends.ui.StyledOutput;

/**
 * Abstract base class for RPG style games.
 * Provides common functionality for RPG games like factories and styled output.
 */
public abstract class RPGGame extends GameInterface {
    private static final long serialVersionUID = 1L;
    
    protected transient StyledOutput styledOutput;
    protected SpiritFactory spiritFactory;
    protected DragonFactory dragonFactory;
    protected ExoskeletonFactory exoskeletonFactory;

    public RPGGame(Input input, Output output) {
        super(input, output);
        this.styledOutput = new StyledOutput(output);
        this.spiritFactory = new SpiritFactory();
        this.dragonFactory = new DragonFactory();
        this.exoskeletonFactory = new ExoskeletonFactory();
    }
}
