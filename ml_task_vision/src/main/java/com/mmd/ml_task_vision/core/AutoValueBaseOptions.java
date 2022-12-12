package com.mmd.ml_task_vision.core;

class AutoValueBaseOptions extends BaseOptions {
    private final int numberThreads;
    private final boolean isUseGPU;

    private AutoValueBaseOptions(int numberThreads, boolean isUseGPU) {
        this.numberThreads = numberThreads;
        this.isUseGPU = isUseGPU;
    }

    @Override
    public boolean isUseGPU() {
        return this.isUseGPU;
    }

    @Override
    public int numberThreads() {
        return this.numberThreads;
    }


    static final class Builder extends BaseOptions.Builder {
        private int numberThreads;
        private boolean isUseGPU;

        @Override
        public BaseOptions.Builder isUseGPU(boolean isUseGPU) {
            this.isUseGPU = isUseGPU;
            return this;
        }

        @Override
        public BaseOptions.Builder setNumberThreads(int numberThreads) {
            this.numberThreads = numberThreads;
            return this;
        }

        @Override
        BaseOptions autoBuild() {
            return new AutoValueBaseOptions(this.numberThreads, this.isUseGPU);
        }
    }
}
