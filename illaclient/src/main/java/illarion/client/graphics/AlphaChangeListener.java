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
package illarion.client.graphics;

/**
 * This interface holds the listener that is notified in case the alpha value of
 * a interface changes.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface AlphaChangeListener {
    /**
     * This function is called to set the change of a alpha value.
     *
     * @param from the old value of the alpha
     * @param to the new value of the alpha
     */
    void alphaChanged(int from, int to);
}
