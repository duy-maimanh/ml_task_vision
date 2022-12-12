package com.mmd.ml_task_vision.nsfw_classification;

import com.mmd.ml_task_vision.core.FilterMode;
import com.mmd.ml_task_vision.core.BaseOptions;

final class AutoValueNsfwProcessOptions extends NsfwProcess.NsfwProcessOptions {
    private final BaseOptions baseOptions;
    private final FilterMode filterMode;
    private final double filterNumber;

    public AutoValueNsfwProcessOptions(BaseOptions baseOptions, FilterMode filterMode, double filterNumber) {
        this.baseOptions = baseOptions;
        this.filterMode = filterMode;
        this.filterNumber = filterNumber;
    }

    @Override
    BaseOptions baseOptions() {
        return this.baseOptions;
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
        private BaseOptions baseOptions;
        private FilterMode filterMode;
        private double filterNumber;

        public Builder() {
        }

        @Override
        public NsfwProcess.NsfwProcessOptions.Builder setBaseOptions(BaseOptions options) {
            this.baseOptions = options;
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
                    this.baseOptions,
                    this.filterMode,
                    this.filterNumber
            );
        }
    }
}
