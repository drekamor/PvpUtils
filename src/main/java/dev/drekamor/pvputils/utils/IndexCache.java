package dev.drekamor.pvputils.utils;

import java.util.ArrayList;
import java.util.List;

public class IndexCache {
    private List<String> inventoriesIndex = new ArrayList<>();
    private List<String> warpsIndex = new ArrayList<>();

    public void setInventoriesIndex(List<String> index) {
        if(index != null) this.inventoriesIndex = index;
    }

    public void setWarpsIndex(List<String> index) {
        if(index != null) this.warpsIndex = index;
    }

    public List<String> getInventoriesIndex() {
        return this.inventoriesIndex;
    }

    public List<String> getWarpsIndex() {
        return this.warpsIndex;
    }

    public void addInventory(String inventory) {
        this.inventoriesIndex.add(inventory);
    }

    public void removeInventory(String inventory) {
        this.inventoriesIndex.remove(inventory);
    }

    public void addWarp(String warp) {
        this.warpsIndex.add(warp);
    }

    public void removeWarp(String warp) {
        this.warpsIndex.remove(warp);
    }
}
