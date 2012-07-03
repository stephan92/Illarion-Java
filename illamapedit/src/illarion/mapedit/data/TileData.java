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
package illarion.mapedit.data;

import javolution.text.TextBuilder;

import java.io.*;
import java.util.Scanner;

/**
 * @author Tim
 */
public class TileData {



    private final MapDimensions mapDimensions;
    private final Tile[][] tileData;

    public TileData(MapDimensions mapDimensions) {
        this.mapDimensions = mapDimensions;
        tileData = new Tile[mapDimensions.getW()][mapDimensions.getH()];
    }

    public TileData(MapDimensions mapDimensions, final TileData old) {
        this.mapDimensions = mapDimensions;

        tileData = new Tile[mapDimensions.getW()][mapDimensions.getH()];
        final int minWidth = (mapDimensions.getW() < old.tileData.length) ? mapDimensions.getW() : old.tileData.length;
        final int minHeight = (mapDimensions.getH() < old.tileData[0].length) ? mapDimensions.getH() : old.tileData[0].length;

        for (int x = 0; x < minWidth; ++x) {
            System.arraycopy(old.tileData[x], 0, tileData[x], 0, minHeight);
        }
    }

    public void setTileAt(final Tile tile, final int x, final int y) {
        tileData[x][y] = tile;
    }

    public Tile getTileAt(final int x, final int y) {
        return tileData[x][y];
    }

    public MapDimensions getMapDimensions() {
        return mapDimensions;
    }

    public static TileData fromInputStream(final InputStream is) {
        final Scanner scanner = new Scanner(is);
        MapDimensions dimensions = new MapDimensions();

        dimensions.setL(Integer.parseInt(scanner.nextLine().substring(3)));
        dimensions.setX(Integer.parseInt(scanner.nextLine().substring(3)));
        dimensions.setY(Integer.parseInt(scanner.nextLine().substring(3)));
        dimensions.setW(Integer.parseInt(scanner.nextLine().substring(3)));
        dimensions.setH(Integer.parseInt(scanner.nextLine().substring(3)));

        final TileData data = new TileData(dimensions);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Tile t = Tile.fromString(line);
            data.tileData[t.getX()][t.getY()] = t;
        }

        return data;
    }

    public void saveToFile(final File file) throws IOException{
        final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
        final TextBuilder builder = TextBuilder.newInstance();
        builder.append(Map.HEADER_LEVEL).append(mapDimensions.getL()).append(Map.NL);
        builder.append(Map.HEADER_X).append(mapDimensions.getX()).append(Map.NL);
        builder.append(Map.HEADER_Y).append(mapDimensions.getY()).append(Map.NL);
        builder.append(Map.HEADER_WIDTH).append(mapDimensions.getW()).append(Map.NL);
        builder.append(Map.HEADER_HEIGHT).append(mapDimensions.getH()).append(Map.NL);

        for (int y = 0; y < mapDimensions.getH(); ++y) {
            for (int x = 0; x < mapDimensions.getW(); ++x) {
                builder.append(x).append(Map.DM);
                builder.append(y).append(Map.DM);
                builder.append(tileData[x][y].getId()).append(Map.DM);
                builder.append(tileData[x][y].getMusicID()).append(Map.DM);
                builder.append(0).append(Map.NL);
            }
        }
        writer.write(builder.toString());
        writer.close();
        TextBuilder.recycle(builder);

    }
}
