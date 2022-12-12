package com.mmd.ml_task_vision.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.mmd.ml_task_vision.core.BaseOptions;

public final class ImageUtils {

    /**
     * Because ScriptIntrinsicBlur is max 25. So we scale down this image and
     * scale up it after blur to keep bitmap size.
     *
     * @param context
     * @param input        is input bitmap
     * @param filterNumber is determine how much blur.
     * @return a bitmap after blur.
     */
    public static Bitmap blurImage(Context context, Bitmap input, double filterNumber) {
        try {
            boolean isResize = false;
            Bitmap processImageInput = input;
            if (filterNumber > 0) {
                isResize = true;
                int targetWidth = (int) (input.getWidth() * (1 - filterNumber));
                int targetHeight = (int) (input.getHeight() * (1 - filterNumber));
                // at least ten pixel
                if (targetWidth < 10) {
                    targetWidth = 10;
                }
                if (targetHeight < 10) {
                    targetHeight = 10;
                }
                processImageInput = Bitmap.createScaledBitmap(input, targetWidth, targetHeight, false);
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
                result = Bitmap.createScaledBitmap(result, input.getWidth(), input.getHeight(), true);
            }
            return result;
        } catch (Exception e) {
            return input;
        }
    }
}
