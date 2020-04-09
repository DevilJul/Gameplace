package de.gameplace.games.tools;

import java.util.List;

public class ListHelper {

    public static <T> T getLastValue(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> void changeLastValue(List<T> list, T value) {
        list.set(list.size() - 1, value);
    }

    public static void incrementLastValue(List<Integer> list) {
        list.set(list.size() - 1, getLastValue(list) + 1);
    }

}