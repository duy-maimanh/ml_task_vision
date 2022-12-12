package com.mmd.ml_task_vision.nsfw_classification;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.FloatRange;

import com.mmd.ml_task_vision.core.FilterMode;
import com.mmd.ml_task_vision.core.OutputImageOptions;
import com.mmd.ml_task_vision.core.utils.ImageUtils;

import java.util.Arrays;
import java.util.List;

public final class NsfwProcess {
    private Context context;
    private final NsfwProcessOptions options;
    private final NsfwClassifier nsfwClassifier;
    private final List<String> nsfwTags;
    private final List<String> nsfwConfuseTags;

    public static NsfwProcess create(Context context) {
        return create(context, null);
    }

    public static NsfwProcess create(Context context, NsfwProcessOptions options) {
        return new NsfwProcess(context, options);
    }

    private NsfwProcess(Context context, NsfwProcessOptions options) {
        this.context = context;
        this.options = options;
        this.nsfwClassifier = new NsfwClassifier(context);
        this.nsfwTags = Arrays.asList("hentai", "porn");
        this.nsfwConfuseTags = Arrays.asList("drawings", "sexy");
    }

    public ResultBundle process(Bitmap bitmap) {
        ResultBundle resultBundle = new ResultBundle(nsfwClassifier.process(bitmap), null);

        Bitmap outputBitmap = bitmap;

        if (NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags)) {
            // post process the output image if need.
            outputBitmap = ImageUtils.processOutput(outputBitmap, options.outputImageOptions());
            if (options.filterMode() != FilterMode.NONE) {
                outputBitmap = ImageUtils.blurImage(this.context,
                        outputBitmap, 1f - (0.9 * options.filterNumber()));
            }
        }

        resultBundle.setFilteredImage(outputBitmap);
        return resultBundle;
    }

    public void close() {
        nsfwClassifier.close();
        context = null;
    }

    public abstract static class NsfwProcessOptions {
        public NsfwProcessOptions() {
        }

        abstract OutputImageOptions outputImageOptions();

        abstract FilterMode filterMode();

        abstract double filterNumber();

        public static NsfwProcessOptions.Builder builder() {
            return (new AutoValueNsfwProcessOptions.Builder())
                    .setFilterMode(FilterMode.BLUR, 1.0)
                    .setOutputImageOptions(OutputImageOptions.builder().build());
        }

        public abstract static class Builder {
            public Builder() {
            }

            @Deprecated
            public abstract NsfwProcessOptions.Builder setOutputImageOptions(OutputImageOptions options);

            public abstract NsfwProcessOptions.Builder setFilterMode(FilterMode mode, @FloatRange(from = 0, to = 1) double filterNumber);

            abstract NsfwProcessOptions autoBuild();

            public final NsfwProcessOptions build() {
                NsfwProcessOptions options = this.autoBuild();
                if (options.filterMode() != FilterMode.NONE && options.filterMode() != FilterMode.BLUR) {
                    throw new IllegalArgumentException(String.format("NsfwProcessOptions only " + "support FilterMode.NONE and FilterMode.BLUR, but" + " found %s", options.filterMode().name()));
                }
                return this.autoBuild();
            }
        }
    }
}
