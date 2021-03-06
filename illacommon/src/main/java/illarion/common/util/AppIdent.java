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
package illarion.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * This class is used to fetch the version number of the specified application.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class AppIdent implements Externalizable {
    /**
     * Serialization UID.
     */
    public static final long serialVersionUID = 1L;

    /**
     * The name of the application.
     */
    @Nonnull
    private String appName;

    /**
     * The version of the application.
     */
    @Nonnull
    private String appVersion;

    /**
     * Create a new instance of the application identification class. Specify the name and the version of the
     * application.
     *
     * @param appName the name of the application
     * @param appVersion the version of the application
     */
    public AppIdent(@Nonnull final String appName, @Nonnull final String appVersion) {
        this.appName = appName;
        this.appVersion = appVersion;
    }

    /**
     * Constructor for deserialization.
     */
    public AppIdent() {
    }

    /**
     * Create a new instance of the application identification class and specify the name of the application. The
     * class will try to fetch the version from the meta information that are attached to the class.
     *
     * @param appName the name of the application
     */
    public AppIdent(@Nonnull final String appName) {
        this.appName = appName;

        @Nullable String foundVersion = null;

        try {
            final Enumeration<URL> resources = AppIdent.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                InputStream stream = null;
                try {
                    final URL currentResource = resources.nextElement();
                    stream = currentResource.openStream();
                    final Manifest manifest = new Manifest(currentResource.openStream());
                    final Attributes mainAttribs = manifest.getMainAttributes();
                    if (appName.equals(mainAttribs.getValue("Implementation-Title"))) {
                        foundVersion = mainAttribs.getValue("Implementation-Version");
                        break;
                    }
                } catch (@Nonnull final IOException ex) {
                    // nothing
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (@Nonnull final IOException ex) {
                            // nothing
                        }
                    }
                }
            }
        } catch (@Nonnull final IOException ex) {
            // nothing
        }

        if ((foundVersion == null) || "unknown".equals(foundVersion)) {
            appVersion = "";
        } else {
            appVersion = foundVersion;
        }
    }

    /**
     * Get the name of the application.
     *
     * @return the name of the application
     */
    @Nonnull
    public String getApplicationName() {
        return appName;
    }

    /**
     * Get the version of the application.
     *
     * @return the version of the application or a empty string in case the version is unknown
     */
    @Nonnull
    public String getApplicationVersion() {
        return appVersion;
    }

    /**
     * Get the version of the application stripped from all appending data that identifies the exact version.
     *
     * @return the application root version
     */
    @Nonnull
    public String getApplicationRootVersion() {
        final int indexOfSeparator = appVersion.indexOf('-');
        if (indexOfSeparator == -1) {
            return appVersion;
        }
        return appVersion.substring(0, indexOfSeparator);
    }

    /**
     * Get the amount of commit that are applied to this version of the application since the release.
     *
     * @return the commit count
     */
    public int getCommitCount() {
        final int indexOfSeparator = appVersion.indexOf('-');
        if (indexOfSeparator == -1) {
            return 0;
        }
        final int indexSecondSeparator = appVersion.indexOf('-', indexOfSeparator + 1);
        if (indexSecondSeparator == -1) {
            return 0;
        }
        try {
            return Integer.parseInt(appVersion.substring(indexOfSeparator + 1, indexSecondSeparator));
        } catch (@Nonnull final NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Get the name of the application and the version of the application on case its known.
     *
     * @return the identifier of the application
     */
    @Nonnull
    public String getApplicationIdentifier() {
        if (appVersion.isEmpty()) {
            return appName;
        }
        return appName + ' ' + appVersion;
    }

    @Override
    public void writeExternal(@Nonnull final ObjectOutput out) throws IOException {
        out.writeLong(serialVersionUID);
        out.writeObject(appName);
        out.writeObject(appVersion);
    }

    @Override
    public void readExternal(@Nonnull final ObjectInput in) throws IOException, ClassNotFoundException {
        final long version = in.readLong();
        if (version == 1L) {
            appName = in.readObject().toString();
            appVersion = in.readObject().toString();
        } else {
            throw new ClassNotFoundException("Class version invalid. Found: " + Long.toString(version) +
                                                     " expected: 1");
        }
    }
}
