package com.mmd.ml_task_vision.nsfw_classification;

public final class NsfwUtils {
    private static final float CONFUSE_THRESHOLD = 0.59F;

    public static boolean isNsfwImage(ResultBundle results) {
        try {
            String key = results.getHighestResult().getKey();
            float value = results.getHighestResult().getValue();
            if (NsfwProcess.NSFW_TAG.equals(key)) {
                return true;
            } else {
                // as the dataset we used to for training. There are some confusing
                // between drawings and hentai, sexy and porn. So if model
                // confuse between them, I think it is good to mark the image
                // is nsfw. Note for update when we have more data for training.
                return value < CONFUSE_THRESHOLD;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
