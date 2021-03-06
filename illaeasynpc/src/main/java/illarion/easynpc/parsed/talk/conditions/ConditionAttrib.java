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
package illarion.easynpc.parsed.talk.conditions;

import illarion.easynpc.data.CharacterAttribute;
import illarion.easynpc.data.CompareOperators;
import illarion.easynpc.parsed.talk.AdvancedNumber;
import illarion.easynpc.parsed.talk.TalkCondition;
import illarion.easynpc.writer.LuaWriter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;

/**
 * This class is used to store all required values for the attribute condition.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class ConditionAttrib implements TalkCondition {
    /**
     * The LUA code needed for this consequence to work.
     */
    private static final String LUA_CODE =
            "talkEntry:addCondition(%1$s.attribute(\"%2$s\", \"%3$s\", %4$s));" + LuaWriter.NL;

    /**
     * The LUA module required for this condition to work.
     */
    private static final String LUA_MODULE = BASE_LUA_MODULE + "attribute";

    /**
     * The attribute that is checked with this condition.
     */
    private final CharacterAttribute attrib;

    /**
     * The compare operator.
     */
    private final CompareOperators operator;

    /**
     * The value the attribute is compared with.
     */
    private final AdvancedNumber value;

    /**
     * Constructor that limits the access to this class, in order to have only
     * the factory creating instances.
     *
     * @param attribData the attribute of this attribute condition
     * @param op the compare operator
     * @param newValue the value the attribute is compared against
     */
    public ConditionAttrib(
            CharacterAttribute attribData, CompareOperators op, AdvancedNumber newValue) {
        attrib = attribData;
        operator = op;
        value = newValue;
    }

    /**
     * Get the LUA module needed for this condition.
     */
    @Nonnull
    @Override
    public String getLuaModule() {
        return LUA_MODULE;
    }

    /**
     * Write the LUA code needed for this attribute condition.
     */
    @Override
    public void writeLua(@Nonnull Writer target) throws IOException {
        target.write(String.format(LUA_CODE, LUA_MODULE, attrib.name(), operator.getLuaComp(), value.getLua()));
    }
}
