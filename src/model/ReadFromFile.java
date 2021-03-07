package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFromFile {
    private static ReadFromFile instance;
    private String def_wallpapers_size = "wallpapers.txt";
    private String def_tiles_size = "tiles.txt";
    private String def_panels_size = "panels.txt";

    public static ReadFromFile getInstance() {
        if (instance == null) {
            instance = new ReadFromFile();
        }
        return instance;
    }

    public ArrayList<Double> getWallpapersSize() {
        return getData(def_wallpapers_size);
    }

    public ArrayList<Double> getTilesSize() {
        return getData(def_tiles_size);
    }

    public ArrayList<Double> getPanelsSize() {
        return getData(def_panels_size);
    }

    private ArrayList<Double> getData(String filename) {
        String data = "";
        ArrayList<Double> doubles = new ArrayList<>();

        try {
            File myObj = new File("src/model/resources/" + filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if (data != "") {
            String[] strings = data.split("~");
            for (int i = 0; i < strings.length; i++)
                doubles.add(Double.parseDouble(strings[i]));
        }
        return doubles;
    }

}
