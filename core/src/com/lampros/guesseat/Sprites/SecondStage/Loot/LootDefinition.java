package com.lampros.guesseat.Sprites.SecondStage.Loot;

import com.badlogic.gdx.math.Vector2;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class LootDefinition {
    public Vector2 position;
    public Class<?> type;

    public LootDefinition(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
