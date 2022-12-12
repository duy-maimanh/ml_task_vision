package com.mmd.ml_task_vision.nsfw_classification;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.mmd.ml_task_vision.core.BaseOptions;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.InterpreterApi;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.gpu.GpuDelegateFactory;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

class NsfwClassifier {
    private static final String NSFW_MODEL_NAME = "nsfw_classifier.tflite";
    private static final String NSFW_LABELS = "nsfw_labels.txt";
    private final Context context;
    private final BaseOptions baseOptions;
    private final TensorBuffer probabilityBuffer =
            TensorBuffer.createFixedSize(new int[]{1, 2}, DataType.FLOAT32);
    private InterpreterApi interpreterApi;
    private List<String> associatedAxisLabels;

    NsfwClassifier(Context context, BaseOptions baseOptions) {
        this.context = context;
        this.baseOptions = baseOptions;
        create();
    }

    private void create() {
        MappedByteBuffer mappedByteBuffer;
        InterpreterApi.Options options = new InterpreterApi.Options();
        CompatibilityList compatList = new CompatibilityList();
        if (baseOptions.isUseGPU()) {
            if (compatList.isDelegateSupportedOnThisDevice()) {
                GpuDelegateFactory.Options delegateOptions = compatList.getBestOptionsForThisDevice();
                GpuDelegate gpuDelegate = new GpuDelegate(delegateOptions);
                options.addDelegate(gpuDelegate);
            } else {
                options.setNumThreads(baseOptions.numberThreads());
            }
        } else {
            options.setNumThreads(baseOptions.numberThreads());
        }
        try {
            mappedByteBuffer = FileUtil.loadMappedFile(context, NSFW_MODEL_NAME);
            interpreterApi = InterpreterApi.create(mappedByteBuffer, options);
            associatedAxisLabels = FileUtil.loadLabels(context, NSFW_LABELS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TensorImage prepareTensorInput(Bitmap bitmap) {
        ImageProcessor imageProcessor = new ImageProcessor.Builder().add(new ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR)).build();
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);

        // Preprocess the image
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage;
    }

    Map<String, Float> detect(Bitmap bitmap) {

        TensorImage tensorImage = prepareTensorInput(bitmap);

        return inference(tensorImage);
    }

    private Map<String, Float> inference(TensorImage tensorImage) {
        if (null != interpreterApi) {
            interpreterApi.run(tensorImage.getBuffer(), probabilityBuffer.getBuffer());
        }

        TensorProcessor probabilityProcessor = new TensorProcessor.Builder().build();
        Map<String, Float> floatMap = null;
        if (null != associatedAxisLabels) {
            // Map of labels and their corresponding probability
            TensorLabel labels = new TensorLabel(associatedAxisLabels, probabilityProcessor.process(probabilityBuffer));

            // Create a map to access the result based on label
            floatMap = labels.getMapWithFloatValue();
        }
        return floatMap;
    }

    void close() {
        interpreterApi.close();
        interpreterApi = null;
    }
}
