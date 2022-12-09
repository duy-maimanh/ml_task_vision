package com.mmd.ml_task_vision.nsfw_classification;

import com.mmd.ml_task_vision.core.FilterMode;
import com.mmd.ml_task_vision.core.OutputImageOptions;

final class AutoValueNsfwProcessOptions extends NsfwProcess.NsfwProcessOptions {
    private final OutputImageOptions outputImageOptions;
    private final FilterMode filterMode;
    private final double filterNumber;

    public AutoValueNsfwProcessOptions(OutputImageOptions outputImageOptions, FilterMode filterMode, double filterNumber) {
        this.outputImageOptions = outputImageOptions;
        this.filterMode = filterMode;
        this.filterNumber = filterNumber;
    }

    @Override
    OutputImageOptions outputImageOptions() {
        return this.outputImageOptions;
    }

    @Override
    FilterMode filterMode() {
        return this.filterMode;
    }

    @Override
    double filterNumber() {
        return this.filterNumber;
    }

    static final class Builder extends NsfwProcess.NsfwProcessOptions.Builder {
        private OutputImageOptions outputImageOptions;
        private FilterMode filterMode;
        private double filterNumber;

        public Builder() {
        }

        @Override
        public NsfwProcess.NsfwProcessOptions.Builder setOutputImageOptions(OutputImageOptions options) {
            this.outputImageOptions = options;
            return this;
        }

        @Override
        public NsfwProcess.NsfwProcessOptions.Builder setFilterMode(FilterMode mode, double filterNumber) {
            this.filterMode = mode;
            this.filterNumber = filterNumber;
            return this;
        }

        @Override
        NsfwProcess.NsfwProcessOptions autoBuild() {
            return new AutoValueNsfwProcessOptions(
                    this.outputImageOptions,
                    this.filterMode,
                    this.filterNumber
            );
        }
    }
}
