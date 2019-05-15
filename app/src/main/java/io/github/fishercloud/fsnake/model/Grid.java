package io.github.fishercloud.fsnake.model;

public class Grid {

    private int height = 1920; //手机高
    private int width = 1080; //手机宽

    private int offset = 90;//偏移量，就是间距  上 左 右 间距一样

    private int gridSize = 30;//每行格子的数量
    private int lineLength;//线的长度
    private int gridWidth;//格子宽

    public Grid() {
        lineLength = width - offset * 2;
        gridWidth = lineLength / gridSize;// 格子数量
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getLineLength() {
        return lineLength;
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "height=" + height +
                ", width=" + width +
                ", offset=" + offset +
                ", gridSize=" + gridSize +
                ", lineLength=" + lineLength +
                ", gridWidth=" + gridWidth +
                '}';
    }
}
