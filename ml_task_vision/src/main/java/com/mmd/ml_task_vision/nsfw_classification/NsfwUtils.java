package com.mmd.ml_task_vision.nsfw_classification;

import java.util.List;

public final class NsfwUtils {
    private static final float CONFUSE_THRESHOLD = 0.59F;
    private static final float NSFW_THRESHOLD = 0.35F;

    @SuppressWarnings("ConstantConditions")
    public static boolean isNsfwImage(ResultBundle results, List<String> nsfwTags,
                                      List<String> nsfwConfuseTags) {
        try {
            String key = results.getHighestResult().getKey();
            float value = results.getHighestResult().getValue();
            if (nsfwTags.contains(key)) {
                return true;
            }
            // as the dataset we used to for training. There are some confusing
            // between drawings and hentai, sexy and porn.
            if (nsfwConfuseTags.contains(key)) {
                if (value < CONFUSE_THRESHOLD) {
                    for (int i = 0; i < nsfwConfuseTags.size(); i++) {
                        if (key.equals(nsfwConfuseTags.get(i))) {
                            float confuseValue =
                                    results.getAllResults().get(nsfwTags.get(i));
                            return confuseValue > NSFW_THRESHOLD;
                        }
                    }
                } else {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
