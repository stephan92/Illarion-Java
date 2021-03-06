/*
 * This file is part of the Illarion project.
 *
 * Copyright © 2014 - Illarion e.V.
 *
 * Illarion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Illarion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package illarion.client.gui.controller.game;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import illarion.client.graphics.AnimationUtility;
import illarion.client.gui.PlayerStatusGui;
import illarion.client.world.characters.CharacterAttribute;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.illarion.engine.GameContainer;
import org.illarion.nifty.controls.Progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This handler takes care for showing the hit points, mana points and food points of the character on the screen.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class CharStatusHandler implements PlayerStatusGui, ScreenController, UpdatableHandler {
    /**
     * The progress bar that shows the hit points.
     */
    @Nullable
    private Progress hitPointBar;

    /**
     * The progress bar that shows the mana points.
     */
    private Progress manaPointBar;

    /**
     * The progress bar that shows the food points.
     */
    private Progress foodPointBar;

    /**
     * The last reported value of the hit points.
     */
    private int hitPoints;

    /**
     * The last reported value of the mana points.
     */
    private int manaPoints;

    /**
     * The last reported value of the food points.
     */
    private int foodPoints;

    /**
     * The currently displayed hit points.
     */
    private int currentHitPoints;

    /**
     * The currently displayed food points.
     */
    private int currentFoodPoints;

    /**
     * The currently displayed mana points.
     */
    private int currentManaPoints;

    @Override
    public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
        hitPointBar = screen.findNiftyControl("healthBar", Progress.class);
        manaPointBar = screen.findNiftyControl("manaBar", Progress.class);
        foodPointBar = screen.findNiftyControl("foodBar", Progress.class);
    }

    @Override
    public void onStartScreen() {
        AnnotationProcessor.process(this);
    }

    @Override
    public void onEndScreen() {
        AnnotationProcessor.unprocess(this);
    }

    @Override
    public void update(final GameContainer container, final int delta) {
        if (hitPoints != currentHitPoints) {
            currentHitPoints = AnimationUtility.approach(currentHitPoints, hitPoints, 0, 10000, delta);
            hitPointBar.setProgress((float) currentHitPoints / 10000.f);
        }
        if (manaPoints != currentManaPoints) {
            currentManaPoints = AnimationUtility.approach(currentManaPoints, manaPoints, 0, 10000, delta);
            manaPointBar.setProgress((float) currentManaPoints / 10000.f);
        }
        if (foodPoints != currentFoodPoints) {
            currentFoodPoints = AnimationUtility.approach(currentFoodPoints, foodPoints, 0, 60000, delta);
            foodPointBar.setProgress((float) currentFoodPoints / 60000.f);
        }
    }

    @Override
    public void setAttribute(@Nonnull final CharacterAttribute attribute, final int value) {
        switch (attribute) {
            case HitPoints:
                hitPoints = value;
                break;
            case FoodPoints:
                foodPoints = value;
                break;
            case ManaPoints:
                manaPoints = value;
                break;
        }
    }
}
