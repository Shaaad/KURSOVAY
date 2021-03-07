package model;

import java.util.List;

public class Calculator implements Dao_calc{

    private Room room;

    public Calculator(double height, List<Rectangle> walls, List<Rectangle> holes) {
        try {
            room = new Room(height, walls, holes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getUsefulSquare() throws Exception {
        return room.getUsefulSquare();
    }

    @Override
    public double getCountMaterials(Rectangle material) {
        try {
            return (Math.ceil(getUsefulSquare() / material.getSquare()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
