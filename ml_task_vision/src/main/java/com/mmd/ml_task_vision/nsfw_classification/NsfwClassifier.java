package com.mmd.ml_task_vision.nsfw_classification;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.InterpreterApi;
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
    private static final int NUM_THREAD = 4;
    private final Context context;
    private final TensorBuffer probabilityBuffer =
            TensorBuffer.createFixedSize(new int[]{1, 5}, DataType.FLOAT32);
    private InterpreterApi interpreterApi;
    private List<String> associatedAxisLabels;

    NsfwClassifier(Context context) {
        this.context = context;
        create();
    }

    private void create() {
        MappedByteBuffer mappedByteBuffer;
        InterpreterApi.Options options =
                new InterpreterApi.Options().setNumThreads(NUM_THREAD);
        try {
            mappedByteBuffer = FileUtil.loadMappedFile(context, NSFW_MODEL_NAME);
            interpreterApi = InterpreterApi.create(mappedByteBuffer, options);
            associatedAxisLabels = FileUtil.loadLabels(context, NSFW_LABELS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TensorImage prepareTensorInput(Bitmap bitmap) {
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(256, 256,
                                ResizeOp.ResizeMethod.BILINEAR))
                        .build();
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);

        // Preprocess the image
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage;
    }

    Map<String, Float> process(Bitmap bitmap) {

        TensorImage tensorImage = prepareTensorInput(bitmap);

        return inference(tensorImage);
    }

    Map<String, Float> process(Uri uri) {
        try {
            Bitmap bitmap =
                    MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            TensorImage tensorImage = prepareTensorInput(bitmap);

            return inference(tensorImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Float> inference(TensorImage tensorImage) {
        if (null != interpreterApi) {
            interpreterApi.run(tensorImage.getBuffer(), probabilityBuffer.getBuffer());
        }

        TensorProcessor probabilityProcessor =
                new TensorProcessor.Builder().build();
        Map<String, Float> floatMap = null;
        if (null != associatedAxisLabels) {
            // Map of labels and their corresponding probability
            TensorLabel labels = new TensorLabel(associatedAxisLabels,
                    probabilityProcessor.process(probabilityBuffer));

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
