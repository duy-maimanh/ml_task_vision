package com.mmd.ml_task_vision.core;

class AutoValueOutputImageOptions extends OutputImageOptions {
    private final double scaleRatio;
    private final int rotationDegrees;
    private int targetWidth;
    private int targetHeight;

    private AutoValueOutputImageOptions(int rotationDegrees,
                                        double scaleRatio,
                                        int targetWidth, int targetHeight) {
        this.rotationDegrees = rotationDegrees;
        this.scaleRatio = scaleRatio;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    @Override
    public double scaleRatio() {
        return this.scaleRatio;
    }

    @Override
    public int rotationDegrees() {
        return this.rotationDegrees;
    }

    @Override
    public int targetWidth() {
        return this.targetWidth;
    }

    @Override
    public int targetHeight() {
        return this.targetHeight;
    }

    static final class Builder extends OutputImageOptions.Builder {
        private Double scaleRatio;
        private Integer rotationDegrees;
        private int targetWidth;
        private int targetHeight;

        @Override
        public OutputImageOptions.Builder setRotationDegrees(int value) {
            this.rotationDegrees = value;
            return this;
        }

        @Override
        public OutputImageOptions.Builder setScaleRatio(double value) {
            this.scaleRatio = value;
            return this;
        }

        @Override
        public OutputImageOptions.Builder setTargetSize(int targetWidth, int targetHeight) {
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            return this;
        }

        @Override
        OutputImageOptions autoBuild() {
            String missing = "";
            if (this.rotationDegrees == null) {
                missing = missing + " rotationDegrees";
            }
            if (this.scaleRatio == null) {
                missing = missing + "scaleRatio";
            }
            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required " +
                        "properties:" + missing);
            } else {
                return new AutoValueOutputImageOptions(
                        this.rotationDegrees,
                        this.scaleRatio,
                        this.targetWidth,
                        this.targetHeight);
            }
        }
    }
}
