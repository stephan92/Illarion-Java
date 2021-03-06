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

/**
 * This interface is used for classes that supply the applications with messages
 * and texts.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface MessageSource {
    /**
     * Get the message that fits to a key.
     *
     * @param key the key to fetch the message from
     * @return the message that belong to the key or the key wrapped in &lt; and
     * &gt; in case there is not message bound to it
     */
    String getMessage(String key);
}
