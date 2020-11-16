package edu.utep.cs.cs4330.mypricewatchernew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class ItemList {
    private static List<Item> items = new ArrayList<>();
    private String name;

    public ItemList() {
        this.name = "New List";
    }

    public ItemList(String name) {
        this.name = name;
    }

    //region Getters

    public String getName() {
        return this.name;
    }

    public List<Item> getItems() {
        return items;
    }

    //endregion

    public void setName(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void refreshItems() {
        for (Item item : items) {
            item.refresh();
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public static List<Item> sortByItemName() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item obj1, Item obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName()); // ascending order
                //return 0;
            }

        });
        return items;
    }

    public List<Item> sortByDateAdded() {
        return items;
    }

    public List<Item> filterBySourceName() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item obj1, Item obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName()); // ascending order
                //return 0;
            }

        });
        return items;
    }

    public List<Item> filterByPriceRange() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
