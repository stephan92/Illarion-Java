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
package illarion.client.gui;

import javax.annotation.Nonnull;

/**
 * This interface defines the access to the crafting dialog GUI.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface DialogMerchantGui {
    /**
     * Show a tooltip for a crafting ingredient.
     *
     * @param dialogId the ID of the crafting dialog
     * @param list the used list
     * @param itemIndex the referenced index of the item
     * @param tooltip the tooltip
     */
    void showMerchantListTooltip(int dialogId, int list, int itemIndex, @Nonnull Tooltip tooltip);
}
