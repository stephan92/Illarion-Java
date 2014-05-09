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
package illarion.client.world.movement;

/**
 * This is the main interface of a movement handler. All movement systems have to use this one.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface MovementHandler {
    /**
     * Check if this movement handler is currently active.
     *
     * @return {@code true} in case this movement is currently active
     */
    boolean isActive();

    /**
     * Calling this function causes this handler to take over control over the movement.
     */
    void assumeControl();

    /**
     * Calling this function causes the handler to stop handling movement.
     */
    void disengage();
}
