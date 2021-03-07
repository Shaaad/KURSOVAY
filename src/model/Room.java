package model;

import java.util.List;

public class Room {

    private final Exception walls_error = new Exception("Недопустимое количество стен");
    private final Exception height_error = new Exception("Высота помещения должна быть положительной величиной");
    private final Exception holes_error = new Exception("Отверстий не быжет быть меньше нуля");
    private final Exception square_error = new Exception("Площадь отверстий не мошет привышать площать стен");
    private List<Rectangle> walls;
    private List<Rectangle> holes;
    private double height;

    public Room(double height, List<Rectangle> walls, List<Rectangle> holes) throws Exception {
        setWalls(walls);
        setHeight(height);
        setHoles(holes);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) throws Exception {
        if (height > 0)
            this.height = height;
        else
            throw height_error;
    }

    public List<Rectangle> getWalls() {
        return walls;
    }

    public void setWalls(List<Rectangle> walls) throws Exception {
        if (walls.size() >= 3)
            this.walls = walls;
        else throw walls_error;
    }

    public List<Rectangle> getHoles() {
        return holes;
    }

    public void setHoles(List<Rectangle> holes) throws Exception {
        if (holes.size() >= 0)
            this.holes = holes;
        else throw holes_error;
    }

    public double getFullWallsSquare() {
        double square = 0;
        for (Rectangle wall : walls)
            square += wall.getHeight() * height;
        return square;
    }

    public double getUsefulSquare() throws Exception {
        double square = getFullWallsSquare();
        for (Rectangle hole : holes)
            square -= hole.getSquare();
        if (square <= 0)
            throw square_error;
        else
            return square;
    }

}
