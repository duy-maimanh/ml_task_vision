package com.mmd.ml_task_vision.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.mmd.ml_task_vision.core.OutputImageOptions;

public final class ImageUtils {

    public static Bitmap blurImage(Context context, Bitmap input, double filterNumber) {
        try {
            boolean isResize = false;
            Bitmap processImageInput = input;
            if (filterNumber < 1) {
                isResize = true;
                processImageInput = Bitmap.createScaledBitmap(input,
                        (int) (input.getWidth() * filterNumber),
                        (int) (input.getHeight() * filterNumber),
                        false
                );
            }
            RenderScript rsScript = RenderScript.create(context);
            Allocation alloc = Allocation.createFromBitmap(rsScript, processImageInput);

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, Element.U8_4(rsScript));
            blur.setRadius(25);
            blur.setInput(alloc);

            Bitmap result = Bitmap.createBitmap(processImageInput.getWidth(), processImageInput.getHeight(), Bitmap.Config.ARGB_8888);
            Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);

            blur.forEach(outAlloc);
            outAlloc.copyTo(result);

            rsScript.destroy();
            if (isResize) {
                result = Bitmap.createScaledBitmap(result,
                        input.getWidth(),
                        input.getHeight(),
                        true);
            }
            return result;
        } catch (Exception e) {
            return input;
        }
    }

    public static Bitmap scaleImage(Bitmap bitmap,
                                    int targetWidth,
                                    int targetHeight) {
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight,true);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap,
                                      int rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public static Bitmap processOutput(Bitmap bitmap,
                                       OutputImageOptions options) {
        int rotationDegree = options.rotationDegrees();
        int targetWidth = options.targetWidth();
        int targetHeight = options.targetHeight();
        if (rotationDegree == 0 && (targetHeight == 0 || targetWidth == 0) && options.scaleRatio() == 1) {
            return bitmap;
        }
        Bitmap processOutput = bitmap;

        if (rotationDegree != 0) {
            processOutput = rotateBitmap(processOutput, rotationDegree);
        }

        // Scale block
        if (targetHeight > 0 && targetWidth > 0) {
            processOutput = scaleImage(processOutput, targetWidth, targetHeight);
        } else if (options.scaleRatio() < 1) {
            targetWidth = (int) (bitmap.getWidth() * options.scaleRatio());
            targetHeight = (int) (bitmap.getHeight() * options.scaleRatio());
            processOutput = scaleImage(processOutput, targetWidth, targetHeight);
        }
        return processOutput;
    }
}
