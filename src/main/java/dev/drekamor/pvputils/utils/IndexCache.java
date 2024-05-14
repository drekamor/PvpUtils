package dev.drekamor.pvputils.utils;

import java.util.ArrayList;
import java.util.List;

public class IndexCache {
    private List<String> inventoryIndex = new ArrayList<>();
    private List<String> warpIndex = new ArrayList<>();

    public void setInventoryIndex(List<String> index) {
        if(index != null) this.inventoryIndex = index;
    }

    public void setWarpIndex(List<String> index) {
        if(index != null) this.warpIndex = index;
    }

    public List<String> getInventoryIndex() {
        return this.inventoryIndex;
    }

    public List<String> getWarpIndex() {
        return this.warpIndex;
    }

    public void addInventory(String inventory) {
        this.inventoryIndex.add(inventory);
    }

    public void removeInventory(String inventory) {
        this.inventoryIndex.remove(inventory);
    }

    public void addWarp(String warp) {
        this.warpIndex.add(warp);
    }

    public void removeWarp(String warp) {
        this.warpIndex.remove(warp);
    }
}
