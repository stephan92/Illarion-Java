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
package illarion.client.net.server;

import illarion.client.net.CommandList;
import illarion.client.net.annotations.ReplyMessage;
import illarion.client.world.MapTile;
import illarion.client.world.World;
import illarion.common.net.NetCommReader;
import illarion.common.types.ItemCount;
import illarion.common.types.ItemId;
import illarion.common.types.Location;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Servermessage: Change item on map ( {@link illarion.client.net.CommandList#MSG_CHANGE_ITEM}).
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @author Nop
 */
@ReplyMessage(replyId = CommandList.MSG_CHANGE_ITEM)
public final class ChangeItemMsg extends AbstractReply {
    /**
     * The new count value of the item.
     */
    private ItemCount count;

    /**
     * The location on the map this update is performed on.
     */
    private Location loc;

    /**
     * The ID of the item after the change.
     */
    private ItemId newItem;

    /**
     * The ID of the item before the change.
     */
    private ItemId oldItem;

    private int newTileMovePoints;

    /**
     * Decode the change item data the receiver got and prepare it for the execution.
     *
     * @param reader the receiver that got the data from the server that needs to be decoded
     * @throws IOException thrown in case there was not enough data received to decode the full message
     */
    @Override
    public void decode(@Nonnull NetCommReader reader) throws IOException {
        loc = decodeLocation(reader);
        oldItem = new ItemId(reader);
        newItem = new ItemId(reader);
        count = ItemCount.getInstance(reader);
        newTileMovePoints = reader.readUByte();
    }

    /**
     * Execute the change item message and send the decoded data to the rest of the client.
     *
     * @return true if the execution is done, false if it shall be called again
     */
    @Override
    public boolean executeUpdate() {
        MapTile tile = World.getMap().getMapAt(loc);
        if (tile != null) {
            tile.changeTopItem(oldItem, newItem, count);
            if (newTileMovePoints == 255) {
                tile.setMovementCost(-1);
            } else {
                tile.setMovementCost(newTileMovePoints);
            }
        }

        return true;
    }

    /**
     * Get the data of this change item message as string.
     *
     * @return the string that contains the values that were decoded for this message
     */
    @Nonnull
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return toString(oldItem + " to " + newItem + " - count: " + count + " at " + loc);
    }
}
