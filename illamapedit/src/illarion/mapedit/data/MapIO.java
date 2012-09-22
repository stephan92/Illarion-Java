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

import illarion.mapedit.data.formats.Decoder;
import illarion.mapedit.data.formats.Version1Decoder;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class takes care of loading and saving maps
 *
 * @author Tim
 */
public class MapIO {
    private static final String HEADER_V = "V:";
    private static final String HEADER_L = "L:";
    private static final String HEADER_X = "X:";
    private static final String HEADER_Y = "Y:";
    private static final String HEADER_W = "W:";
    private static final String HEADER_H = "H:";
    public static final String EXT_WARP = ".warps.txt";
    public static final String EXT_ITEM = ".items.txt";
    public static final String EXT_TILE = ".tiles.txt";
    private static final String NEWLINE = "\n\r";
    private static final String VERSION_PATTERN = "V: \\d+";
    private static final java.util.Map<String, Decoder> DECODERS = new HashMap<String, Decoder>();

    static {
        DECODERS.put("1", new Version1Decoder());
    }

    private MapIO() {

    }

    /**
     * Loads a map from a specified file
     *
     * @param path the path
     * @param name the map name
     * @return the map
     * @throws IOException if an error occurs
     */
    public static Map loadMap(final String path, final String name) throws IOException {

//        Open the streams for all 3 files, containing the map data
        final File tileFile = new File(path, name + EXT_TILE);
        final File itemFile = new File(path, name + EXT_ITEM);
        final File warpFile = new File(path, name + EXT_WARP);
        final BufferedReader tileInput = new BufferedReader(new InputStreamReader(new FileInputStream(tileFile)));
        final BufferedReader itemInput = new BufferedReader(new InputStreamReader(new FileInputStream(itemFile)));
        final BufferedReader warpInput = new BufferedReader(new InputStreamReader(new FileInputStream(warpFile)));
        final String version;
        final String versionLine = tileInput.readLine();
        final Decoder decoder;
        if (Pattern.matches(VERSION_PATTERN, versionLine)) {
            version = versionLine.substring(3).trim();
            decoder = DECODERS.get(version);
            decoder.newMap(name, path);
        } else {
            version = "1";
            decoder = DECODERS.get(version);
            decoder.newMap(name, path);
            decoder.decodeTileLine(versionLine);
        }


        String s;
        while ((s = tileInput.readLine()) != null) {
            decoder.decodeTileLine(s);
        }

        while ((s = itemInput.readLine()) != null) {
            decoder.decodeItemLine(s);
        }

        while ((s = warpInput.readLine()) != null) {
            decoder.decodeWarpLine(s);
        }


        return decoder.getDecodedMap();
    }

    /**
     * Loads the map, with the map name and path, stored in the map object
     *
     * @param map the map to save
     * @throws IOException
     */
    public static void saveMap(final Map map) throws IOException {
        saveMap(map, map.getName(), map.getPath());
    }

    /**
     * Loads the map, with specified the map name and path.
     *
     * @param map
     * @param name
     * @param path
     * @throws IOException
     */
    public static void saveMap(final Map map, final String name, final String path) throws IOException {
        final File tileFile = new File(path, name + EXT_TILE);
        final File itemFile = new File(path, name + EXT_ITEM);
        final File warpFile = new File(path, name + EXT_WARP);
        if (!checkFile(tileFile) || !checkFile(itemFile) || !checkFile(warpFile)) {
            throw new IOException("Files are folders or can't be created.");
        }
        final BufferedWriter tileOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tileFile)));
        final BufferedWriter itemOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(itemFile)));
        final BufferedWriter warpOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(warpFile)));

        tileOutput.write(String.format("%s %d%s", HEADER_V, 2, NEWLINE));
        tileOutput.write(String.format("%s %d%s", HEADER_L, map.getZ(), NEWLINE));
        tileOutput.write(String.format("%s %d%s", HEADER_X, map.getX(), NEWLINE));
        tileOutput.write(String.format("%s %d%s", HEADER_Y, map.getY(), NEWLINE));
        tileOutput.write(String.format("%s %d%s", HEADER_W, map.getWidth(), NEWLINE));
        tileOutput.write(String.format("%s %d%s", HEADER_H, map.getHeight(), NEWLINE));
//        <dx>;<dy>;<item ID>;<quality>[;<data value>[;...]]
        for (int y = 0; y < map.getWidth(); ++y) {
            for (int x = 0; x < map.getHeight(); ++x) {
                final MapTile tile = map.getTileAt(x, y);
                tileOutput.write(String.format("%d;%d;%d;%d;0%s", x, y, tile.getId(), tile.getMusicID(), NEWLINE));

                final List<MapItem> items = tile.getMapItems();
                if ((items != null) && !items.isEmpty()) {
                    for (final MapItem item : items) {
                        itemOutput.write(String.format("%d;%d;0;%d;%d;%s%s", x, y, item.getId(), item.getQuality(),
                                item.getItemData(), NEWLINE));
                    }
                }
                final MapWarpPoint warp = tile.getMapWarpPoint();
                if (warp != null) {
                    warpOutput.write(String.format("%d;%d;%d;%d;%d%s", x, y, warp.getXTarget(), warp.getYTarget(),
                            warp.getZTarget(), NEWLINE));
                }
            }
        }
    }

    private static boolean checkFile(final File file) {
        if (file.isDirectory()) {
            return false;
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
            return file.exists();
        }
        return true;
    }

}
