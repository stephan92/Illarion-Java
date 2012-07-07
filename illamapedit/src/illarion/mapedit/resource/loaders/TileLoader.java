/*
 * This file is part of the Illarion Mapeditor.
 *
 * Copyright © 2012 - Illarion e.V.
 *
 * The Illarion Mapeditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Mapeditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Mapeditor.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.mapedit.resource.loaders;

import illarion.common.graphics.TileInfo;
import illarion.common.util.TableLoaderSink;
import illarion.common.util.TableLoaderTiles;
import illarion.mapedit.resource.Resource;
import illarion.mapedit.resource.TileImg;
import javolution.util.FastList;

import java.awt.*;
import java.io.IOException;

/**
 * @author Tim
 */
public class TileLoader implements TableLoaderSink<TableLoaderTiles>, Resource {

    private static final TileLoader INSTANCE = new TileLoader();
    private static final String DIR_IMG_TILES = "data/tiles/";

    private final FastList<TileImg> tiles = new FastList<TileImg>();

    private TileLoader() {
    }

    @Override
    public void load() throws IOException {
        new TableLoaderTiles("Tiles", this);
    }

    @Override
    public String getDescription() {
        return "Tiles";
    }

    /**
     * Handle a single line of the resource table.
     */
    @Override
    public boolean processRecord(final int line, final TableLoaderTiles loader) {
        final int id = loader.getTileId();
        final int mode = loader.getTileMode();
        final String name = loader.getResourceName();
        final TextureLoaderAwt texture = TextureLoaderAwt.getInstance();
        TileImg tile = null;
        final TileInfo info =
                new TileInfo(loader.getTileColor(), loader.getMovementCost(), loader.isOpaque());
        switch (mode) {
            case TableLoaderTiles.TILE_MODE_ANIMATED:

                tile = new TileImg(id, name, loader.getFrameCount(), loader.getAnimationSpeed(), info,
                        getImages(name, loader.getFrameCount()));
                break;

            case TableLoaderTiles.TILE_MODE_VARIANT:
                tile = new TileImg(id, name, loader.getFrameCount(), 0, info,
                        getImages(name, loader.getFrameCount()));
                break;

            default:
                tile = new TileImg(id, name, 1, 0, info,
                        getImages(name, 1));
                break;

        }

        tiles.add(tile);

        return true;
    }

    public Image[] getImages(final String name, final int frames) {
        final Image[] imgs = new Image[frames];
        final TextureLoaderAwt tx = TextureLoaderAwt.getInstance();
        if (frames > 1) {
            for (int i = 0; i < frames; ++i) {
                imgs[i] = tx.getTexture(String.format("%s%s-%d", DIR_IMG_TILES, name, i));
            }
        } else {
            imgs[0] = tx.getTexture(DIR_IMG_TILES + name);
        }
        return imgs;
    }

    public TileImg getTileFromId(int id) {
        for (TileImg t : tiles) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public static TileLoader getInstance() {
        return INSTANCE;
    }
}
