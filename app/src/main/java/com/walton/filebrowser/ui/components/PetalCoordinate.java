package com.walton.filebrowser.ui.components;

/**
 * coordinate to draw a petal in flower
 */

/**
 * Modify by Amlan on 16/01/2021
 */
public class PetalCoordinate {

    private int startX, startY, endX, endY;

    public PetalCoordinate(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
