package io.github.fishercloud.fsnake.model;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> snake = new ArrayList<>();

    public List<Point> getSnake() {
        return snake;
    }

    public void setSnake(List<Point> snake) {
        this.snake = snake;
    }

    @Override
    public String toString() {
        return "SnakeBean{" +
                "snake=" + snake +
                '}';
    }
}
