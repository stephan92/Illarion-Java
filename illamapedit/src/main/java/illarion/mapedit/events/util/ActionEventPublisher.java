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
package illarion.mapedit.events.util;

import org.bushe.swing.event.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Tim
 */
public class ActionEventPublisher implements ActionListener {

    private final Object[] event;

    public ActionEventPublisher(final Object... event) {
        this.event = event;
    }

    @Override
    public void actionPerformed(final ActionEvent ignore) {
        for (final Object e : event) {
            EventBus.publish(e);
        }
    }
}
