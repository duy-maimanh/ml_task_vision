package com.mmd.ml_task_vision.nsfw_classification;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.FloatRange;

import com.mmd.ml_task_vision.core.FilterMode;
import com.mmd.ml_task_vision.core.BaseOptions;
import com.mmd.ml_task_vision.core.utils.ImageUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class NsfwProcess {
    private Context context;
    private final NsfwProcessOptions options;
    private final NsfwClassifier nsfwClassifier;
    static final String NSFW_TAG = "unsafe";

    public static NsfwProcess create(Context context) {
        return create(context, NsfwProcessOptions.builder().build());
    }

    public static NsfwProcess create(Context context, NsfwProcessOptions options) {
        return new NsfwProcess(context, options);
    }

    private NsfwProcess(Context context, NsfwProcessOptions options) {
        this.context = context;
        this.options = options;
        this.nsfwClassifier = new NsfwClassifier(context, options.baseOptions());
    }

    public ResultBundle detect(Bitmap bitmap) {
        ResultBundle resultBundle = new ResultBundle(nsfwClassifier.detect(bitmap), null);

        Bitmap outputBitmap = bitmap;

        if (NsfwUtils.isNsfwImage(resultBundle)) {
            if (options.filterMode() != FilterMode.NONE) {
                outputBitmap = ImageUtils.blurImage(this.context,
                        outputBitmap, options.filterNumber());
            }
        }

        resultBundle.setFilteredImage(outputBitmap);
        return resultBundle;
    }

    public ResultBundle detect(Uri uri) {
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return detect(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        nsfwClassifier.close();
        context = null;
    }

    public abstract static class NsfwProcessOptions {
        public NsfwProcessOptions() {
        }

        abstract BaseOptions baseOptions();

        abstract FilterMode filterMode();

        abstract double filterNumber();

        public static NsfwProcessOptions.Builder builder() {
            return (new AutoValueNsfwProcessOptions.Builder())
                    .setFilterMode(FilterMode.BLUR, 0.0)
                    .setBaseOptions(BaseOptions.builder().build());
        }

        public abstract static class Builder {
            public Builder() {
            }

            public abstract NsfwProcessOptions.Builder setBaseOptions(BaseOptions options);

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
