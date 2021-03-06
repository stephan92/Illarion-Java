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
package illarion.client.states;

import de.lessvoid.nifty.Nifty;
import illarion.client.Game;
import illarion.client.Login;
import illarion.client.input.InputReceiver;
import illarion.client.world.MapDimensions;
import illarion.client.world.World;
import illarion.client.world.events.CloseGameEvent;
import illarion.client.world.events.ServerNotFoundEvent;
import org.bushe.swing.event.EventBus;
import org.illarion.engine.GameContainer;

import javax.annotation.Nonnull;

/**
 * This state is active while the player is playing the game.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class PlayingState implements GameState {
    /**
     * The input receiver of the game.
     */
    @Nonnull
    private final InputReceiver receiver;

    public PlayingState(@Nonnull InputReceiver inputReceiver) {
        receiver = inputReceiver;
    }

    @Override
    public void create(@Nonnull Game game, @Nonnull GameContainer container, @Nonnull Nifty nifty) {
        World.initGui(container.getEngine());
        nifty.registerScreenController(World.getGameGui().getScreenController());

        Util.loadXML(nifty, "illarion/client/gui/xml/gamescreen.xml");
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(@Nonnull GameContainer container, int width, int height) {
        MapDimensions.getInstance().reportScreenSize(width, height);
    }

    @Override
    public void update(@Nonnull GameContainer container, int delta) {
        if (World.getGameGui().isReady()) {
            World.getUpdateTaskManager().onUpdateGame(container, delta);
        }
        World.getGameGui().onUpdateGame(container, delta);
        World.getWeather().update(delta);
        World.getMapDisplay().update(container, delta);
        World.getAnimationManager().animate(delta);
        World.getMusicBox().update();
    }

    @Override
    public void render(@Nonnull GameContainer container) {
        World.getMap().getMiniMap().render(container);
        World.getMapDisplay().render(container);
    }

    @Override
    public boolean isClosingGame() {
        EventBus.publish(new CloseGameEvent());
        return false;
    }

    @Override
    public void enterState(@Nonnull GameContainer container, @Nonnull Nifty nifty) {
        nifty.gotoScreen("gamescreen");
        receiver.setEnabled(true);

        if (Login.getInstance().login()) {
            MapDimensions.getInstance().reportScreenSize(container.getWidth(), container.getHeight());
        } else {
            EventBus.publish(new ServerNotFoundEvent());
        }
    }

    @Override
    public void leaveState(@Nonnull GameContainer container) {
        receiver.setEnabled(false);
    }
}
