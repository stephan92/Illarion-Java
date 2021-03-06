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
 * Interface for movement based animation targets.
 *
 * @author Nop
 */
public interface AnimatedMove extends Animated {
    /**
     * Update the position for a move animation.
     *
     * @param posX the x coordinate of the new movement position
     * @param posY the y coordinate of the new movement position
     * @param posZ the z coordinate of the new movement position
     */
    void setPosition(int posX, int posY, int posZ);
}
