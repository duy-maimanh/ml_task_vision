package com.mmd.ml_task_vision.core;

import androidx.annotation.IntRange;

public abstract class BaseOptions {
    public BaseOptions() {
    }

    public abstract boolean isUseGPU();

    public abstract int numberThreads();

    public static BaseOptions.Builder builder() {
        return new AutoValueBaseOptions.Builder().isUseGPU(false).setNumberThreads(4);

    }

    public abstract static class Builder {
        public Builder() {

        }

        public abstract BaseOptions.Builder isUseGPU(boolean isUseGPU);

        public abstract BaseOptions.Builder setNumberThreads(
                @IntRange(from = 1, to = 4) int numberThreads
        );

        abstract BaseOptions autoBuild();

        public final BaseOptions build() {
            BaseOptions options = this.autoBuild();
            if (options.numberThreads() > 4 || options.numberThreads() < 1) {
                throw new IllegalArgumentException(String.format("Expect the " +
                        "number of the threads between 1 and 4, " +
                        "found:" +
                        " %s", options.numberThreads()));
            }
            return options;
        }
    }
}
