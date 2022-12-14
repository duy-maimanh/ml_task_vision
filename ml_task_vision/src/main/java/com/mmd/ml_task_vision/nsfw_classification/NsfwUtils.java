package com.mmd.ml_task_vision.nsfw_classification;

public final class NsfwUtils {
    private static final float CONFUSE_THRESHOLD_NSFW = 0.35F;
    private static final float CONFUSE_SEXY = 0.7F;

    public static boolean isNsfwImage(ResultBundle results) {
        try {
            String key = results.getHighestResult().getKey();
            float value = results.getHighestResult().getValue();
            if (NsfwProcess.NSFW_TAG.equals(key)) {
                return true;
            } else {
                // base the dataset we used to for training. There are some confusing
                // between drawings and hentai, sexy and nude. So if model
                // confuse between them, I think it is good to mark the image
                // is nsfw. Note for update when we have more data for training.
                float nsfwValue = results.getAllResults().get(NsfwProcess.NSFW_TAG);
                return nsfwValue >= CONFUSE_THRESHOLD_NSFW && value < CONFUSE_SEXY;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
