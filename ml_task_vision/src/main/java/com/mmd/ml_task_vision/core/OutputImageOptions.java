package com.mmd.ml_task_vision.core;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

@Deprecated
public abstract class OutputImageOptions {
    public OutputImageOptions() {
    }

    public abstract double scaleRatio();

    public abstract int rotationDegrees();

    public abstract int targetWidth();

    public abstract int targetHeight();

    public static OutputImageOptions.Builder builder() {
        return new AutoValueOutputImageOptions.Builder()
                .setScaleRatio(1.0)
                .setRotationDegrees(0)
                .setTargetSize(0, 0);
    }

    public abstract static class Builder {
        public Builder() {

        }

        public abstract OutputImageOptions.Builder setRotationDegrees(int value);

        public abstract OutputImageOptions.Builder setScaleRatio(@FloatRange(from = 0.1) double value);

        public abstract OutputImageOptions.Builder setTargetSize(@IntRange(from = 0) int targetWidth, @IntRange(from = 0) int targetHeight);

        abstract OutputImageOptions autoBuild();

        public final OutputImageOptions build() {
            OutputImageOptions options = this.autoBuild();
            if (options.rotationDegrees() % 90 != 0) {
                throw new IllegalArgumentException(String.format("Expected rotation to be a multiple of 90°, found: %d.", options.rotationDegrees()));
            }
            if (options.scaleRatio() > 1.0 || options.scaleRatio() < 0.0) {
                throw new IllegalArgumentException(String.format("Expected " + "ratio should be start form 0.1, found: %s", options.scaleRatio()));
            }
            if (options.targetWidth() < 0 || options.targetHeight() < 0) {
                throw new IllegalArgumentException(String.format("Expect the edge of the image to be greater than or equal to 0, found: %s",
                        options.targetWidth() + "*" + options.targetHeight()));
            }
            return options;
        }
    }
}
