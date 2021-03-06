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
package illarion.mapedit.events;

/**
 * @author Fredrik K
 */
public class PasteEvent {

    private final int x;
    private final int y;

    public PasteEvent(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordiante
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }
}
