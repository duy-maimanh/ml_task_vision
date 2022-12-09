package com.mmd.ml_task_vision.nsfw_classification;

import android.graphics.Bitmap;

import java.util.Map;

public class ResultBundle {
    private Bitmap filteredImage;
    private Map<String, Float> results;

    public ResultBundle(Map<String, Float> results, Bitmap filteredImage) {
        this.filteredImage = filteredImage;
        this.results = results;
    }

    public void setFilteredImage(Bitmap filteredImage) {
        this.filteredImage = filteredImage;
    }

    public void setResults(Map<String, Float> results) {
        this.results = results;
    }

    public Bitmap getFilteredImage() {
        return filteredImage;
    }

    public Map<String, Float> getAllResults() {
        return results;
    }

    public Map.Entry<String, Float> getHighestResult() {
        Map.Entry<String, Float> maxEntry = null;

        for (Map.Entry<String, Float> entry : results.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }
}
