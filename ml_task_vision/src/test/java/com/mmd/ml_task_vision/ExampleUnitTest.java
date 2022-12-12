package com.mmd.ml_task_vision;

import org.junit.Test;

import static org.junit.Assert.*;

import com.mmd.ml_task_vision.nsfw_classification.NsfwUtils;
import com.mmd.ml_task_vision.nsfw_classification.ResultBundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private final Map<String, Float> resultSafe = new HashMap<>() {{
        put("safe", 0.88f);
        put("unsafe", 0.12f);
    }};

    private final Map<String, Float> resultUnSafe = new HashMap<>() {{
        put("safe", 0.1f);
        put("unsafe", 0.9f);
    }};

    private final Map<String, Float> resultSafeConfuse = new HashMap<>() {{
        put("safe", 0.55f);
        put("unsafe", 0.45f);
    }};

    @Test
    public void resultSafeFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultSafe, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle);
        assertFalse(isNsfw);
    }

    @Test
    public void resultUnSafeFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultUnSafe, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle);
        assertTrue(isNsfw);
    }

    @Test
    public void resultSafeConfuseFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultSafeConfuse, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle);
        assertTrue(isNsfw);
    }
}
