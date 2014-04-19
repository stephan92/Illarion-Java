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
package illarion.client.graphics;

import illarion.client.input.ClickOnMapEvent;
import illarion.client.input.PointOnMapEvent;
import illarion.client.resources.OverlayFactory;
import illarion.client.resources.Resource;
import illarion.client.resources.TileFactory;
import illarion.client.resources.data.OverlayTemplate;
import illarion.client.resources.data.TileTemplate;
import illarion.client.world.MapGroup;
import illarion.client.world.MapTile;
import illarion.client.world.World;
import illarion.common.graphics.MapVariance;
import illarion.common.graphics.TileInfo;
import illarion.common.types.Location;
import org.illarion.engine.GameContainer;
import org.illarion.engine.graphic.Color;
import org.illarion.engine.graphic.Graphics;
import org.illarion.engine.graphic.SceneEvent;
import org.illarion.engine.graphic.effects.TextureEffect;
import org.illarion.engine.input.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class represents one tile on the screen.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @author Nop
 */
@SuppressWarnings("ClassNamingConvention")
public class Tile extends AbstractEntity<TileTemplate> implements Resource {
    /**
     * The animation that is applied to this tile or {@code null} in case there is none.
     */
    @Nullable
    private final FrameAnimation animation;

    /**
     * The template of the overlay that is rendered on top of the tile or {@code null} in case just the plain tile is
     * rendered.
     */
    @Nullable
    private OverlayTemplate overlay;

    /**
     * The shape of the overlay that is rendered on the tile.
     */
    private int overlayShape;

    /**
     * The parent map tile reference.
     */
    @Nonnull
    private final MapTile parentTile;

    /**
     * The instance of the logging class for this class.
     */
    @SuppressWarnings("UnusedDeclaration")
    private static final Logger LOGGER = LoggerFactory.getLogger(Tile.class);

    public Tile(int tileId, @Nonnull MapTile parentTile) {
        this(TileFactory.getInstance().getTemplate(TileInfo.getBaseID(tileId)), tileId, parentTile);
    }

    public Tile(@Nonnull TileTemplate template, int tileId, @Nonnull MapTile parentTile) {
        super(template);

        if (template.getAnimationSpeed() > 0) {
            // start animation right away. All tiles of this type will share it
            animation = template.getSharedAnimation();
        } else if (template.getFrames() > 1) { // a tile with variants
            animation = null;
            Location location = parentTile.getLocation();
            setFrame(MapVariance.getTileFrameVariance(location.getScX(), location.getScY(), template.getFrames()));
        } else {
            animation = null;
        }
        this.parentTile = parentTile;

        if (TileInfo.hasOverlay(tileId)) {
            overlay = OverlayFactory.getInstance().getTemplate(TileInfo.getOverlayID(tileId));
            overlayShape = TileInfo.getShapeId(tileId) - 1;
            setFadingCorridorEffectEnabled(false);
        }
    }

    /**
     * Draw tile and its overlay
     *
     * @param g the graphics object that is used to render the tile.
     */
    @Override
    public void render(@Nonnull Graphics g) {
        MapTile obstructingTile = parentTile.getObstructingTile();
        if ((obstructingTile != null) && obstructingTile.isOpaque()) {
            return;
        }

        super.render(g);
    }

    @Override
    protected void renderSprite(
            @Nonnull Graphics g, int x, int y, @Nonnull Color light, @Nonnull TextureEffect... effects) {
        super.renderSprite(g, x, y, light, effects);
        if (overlay != null) {
            g.drawSprite(overlay.getSprite(), x, y, light, overlayShape, getScale(), 0.f, effects);
        }
    }

    @Override
    public void show() {
        MapGroup group = parentTile.getMapGroup();
        if ((group != null) && group.isHidden()) {
            setAlphaTarget(0);
            setAlpha(0);
            setFadingCorridorEffectEnabled(false);
        } else {
            setFadingCorridorEffectEnabled(true);
        }
        super.show();
        if (animation != null) {
            animation.addTarget(this, true);
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (animation != null) {
            animation.removeTarget(this);
        }
    }

    @Override
    public void update(@Nonnull GameContainer container, int delta) {
        MapGroup group = parentTile.getMapGroup();
        if ((group != null) && group.isHidden()) {
            setAlphaTarget(0);
            setFadingCorridorEffectEnabled(false);
        } else {
            setFadingCorridorEffectEnabled(true);
        }
        super.update(container, delta);
    }

    @Override
    public boolean isEventProcessed(
            @Nonnull GameContainer container, int delta, @Nonnull SceneEvent event) {
        if (event instanceof PointOnMapEvent) {
            if (!isVisible()) {
                return false;
            }

            PointOnMapEvent pointEvent = (PointOnMapEvent) event;
            if (isMouseInInteractionRect(pointEvent.getX(), pointEvent.getY())) {
                return true;
            }
        }
        if (!parentTile.isAtPlayerLevel()) {
            return false;
        }

        if (event instanceof ClickOnMapEvent) {
            ClickOnMapEvent clickEvent = (ClickOnMapEvent) event;
            if (clickEvent.getKey() != Button.Left) {
                return false;
            }
            if (!isMouseInInteractionRect(clickEvent.getX(), clickEvent.getY())) {
                return false;
            }

            World.getPlayer().getMovementHandler().walkTo(parentTile.getLocation());
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation of the parent light fetches the light of the parent tile in order to ensure that the same
     * color value is used.
     */
    @Override
    @Nonnull
    protected Color getParentLight() {
        return parentTile.getLight();
    }
}
