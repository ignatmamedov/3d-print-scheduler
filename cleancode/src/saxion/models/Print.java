package saxion.models;

import saxion.facade.PrintDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import java.util.List;

public class Print {
    private String name;
    private int height;

    private int width;
    private int length;
    private ArrayList<Double> filamentLength;
    private int printTime;

    public Print(String name, int height, int width, int length, ArrayList<Double> filamentLength, int printTime) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;
        this.filamentLength = filamentLength;
        this.printTime = printTime;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Double> getFilamentLength() {
        return filamentLength;
    }


    public static Print fromMap(Map<String, Object> map) {
        String name = String.valueOf(map.getOrDefault("name", map.getOrDefault("1", "unknown")));
        int height = Integer.parseInt(String.valueOf(map.getOrDefault("height", map.getOrDefault("2", "0"))));
        int width = Integer.parseInt(String.valueOf(map.getOrDefault("width", map.getOrDefault("3", "0"))));
        int length = Integer.parseInt(String.valueOf(map.getOrDefault("length", map.getOrDefault("4", "0"))));
        int printTime = Integer.parseInt((map.getOrDefault("printTime", map.getOrDefault("5", "0"))).toString());

        ArrayList<Double> filamentLength = map.getOrDefault("filamentLength", map.getOrDefault("6", ""))
                instanceof List
                ? new ArrayList<>((List<Double>) map.getOrDefault("filamentLength", map.getOrDefault("6", new ArrayList<Double>())))
                : new ArrayList<>(Arrays.stream(String.valueOf(map.getOrDefault("filamentLength", map.getOrDefault("6", "0"))).split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Double::parseDouble)
                .toList());

        return new Print(name, height, width, length, filamentLength, printTime);
    }

    @Override
    public String toString(){
        return getName();
    }

    public PrintDTO toDTO(){
        return new PrintDTO(name, height, width, length, filamentLength, printTime);
    }


}
