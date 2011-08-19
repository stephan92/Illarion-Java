/*
 * This file is part of the Illarion easyQuest Editor.
 *
 * Copyright 2011 - Illarion e.V.
 *
 * The Illarion easyQuest Editor is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * The Illarion easyQuest Editor is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Illarion easyQuest Editor. If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.easyquest.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

import illarion.easyquest.Lang;

final class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    public static ResizableIcon getResizableIconFromResource(
        final String resource) {
        Image image;
        try {
            image =
                ImageIO.read(Utils.class.getClassLoader()
                    .getResource(resource));
        } catch (final IOException e) {
            LOGGER.error("Failed to read image: \"" + resource + "\"");
            return null;
        }
        final int height = image.getHeight(null);
        final int width = image.getWidth(null);
        final ResizableIcon resizeIcon =
            ImageWrapperResizableIcon.getIcon(image, new Dimension(width,
                height));
        return resizeIcon;
    }
    
    protected static void saveEasyQuest(final Editor editor) {
        final File file = editor.getQuestFile();
        if (file == null) {
            selectAndSaveEasyQuest(editor);
            return;
        }

        final String quest = editor.getQuestXML();
        saveEasyQuestImpl(quest, file);
        editor.saved();
    }
    
    protected static void selectAndOpenQuest() {
        final JFileChooser fileDiag = new JFileChooser();
        fileDiag.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(final File f) {
                return !f.isFile() || f.getName().endsWith(".quest"); //$NON-NLS-1$
            }

            @Override
            public String getDescription() {
                return Lang.getMsg(Utils.class, "easyQuestFileType"); //$NON-NLS-1$
            }
        });
        //fileDiag.setCurrentDirectory(new File(Config.getInstance()
        //    .getEasyNpcFolder()));

        final int fileReturn =
            fileDiag.showOpenDialog(MainFrame.getInstance());
        if (fileReturn == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileDiag.getSelectedFile();
            openQuest(selectedFile);
        }
    }
    
    protected static void openQuest(final File file) {
        try {
            final int editorIndex = MainFrame.getInstance().alreadyOpen(file);
            if (editorIndex > -1) {
                MainFrame.getInstance().setCurrentEditorTab(editorIndex);
                return;
            }
            
            StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String quest = sb.toString();

            Editor editor = MainFrame.getInstance().addNewQuest();
            editor.setQuestFile(file);
    		editor.loadQuest(quest);
            
            MainFrame.getInstance().setCurrentTabTitle(file.getName());
            //Config.getInstance().addLastOpenedFile(file);
        } catch (final IOException e1) {
            //LOGGER.error("Reading the script failed.", e1); //$NON-NLS-1$
        }
    }
    
    protected static void selectAndSaveEasyQuest(final Editor editor) {
        final String quest = editor.getQuestXML();
        final JFileChooser fileDiag = new JFileChooser();
        fileDiag.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(final File f) {
                return !f.isFile() || f.getName().endsWith(".quest"); //$NON-NLS-1$
            }

            @Override
            public String getDescription() {
                return Lang.getMsg(Utils.class, "easyQuestFileType"); //$NON-NLS-1$
            }
        });
        //fileDiag.setCurrentDirectory(new File(Config.getInstance()
        //    .getEasyNpcFolder()));
        fileDiag.setSelectedFile(editor.getQuestFile());
        final int fileReturn =
            fileDiag.showSaveDialog(MainFrame.getInstance());
        if (fileReturn == JFileChooser.APPROVE_OPTION) {
            final File targetFile = fileDiag.getSelectedFile();
            saveEasyQuestImpl(quest, targetFile);
            editor.setQuestFile(targetFile);
            MainFrame.getInstance().setTabTitle(editor, targetFile.getName());
        }
        editor.saved();
    }
    
    private static void saveEasyQuestImpl(final String quest,
        final File targetFile) {
        final File backupFile =
            new File(targetFile.getAbsolutePath() + ".bak"); //$NON-NLS-1$
            
        FileWriter fw = null;
        try {
            if (backupFile.exists()) {
                backupFile.delete();
            }
            if (targetFile.exists()) {
                targetFile.renameTo(backupFile);
            }

            fw = new FileWriter(targetFile);
			fw.write(quest);

            if (backupFile.exists()) {
                backupFile.delete();
            }
        } catch (final Exception e) {
            if (backupFile.exists()) {
                if (targetFile.exists()) {
                    targetFile.delete();
                }
                backupFile.renameTo(targetFile);
            }
            //LOGGER.error("Writing the easyNPC Script failed.", e); //$NON-NLS-1$
        } finally {
            if ( fw != null ) 
                try { fw.close(); } catch ( IOException e ) { } 
        }
    }
}