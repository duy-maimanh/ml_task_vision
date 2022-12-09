package com.mmd.ml_task_vision;

import org.junit.Test;

import static org.junit.Assert.*;

import com.mmd.ml_task_vision.nsfw_classification.NsfwUtils;
import com.mmd.ml_task_vision.nsfw_classification.ResultBundle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private final List<String> nsfwTags = Arrays.asList("hentai", "porn");
    private final List<String> nsfwConfuseTags = Arrays.asList("drawings", "sexy");

    private final Map<String, Float> resultsNeutral = new HashMap<String, Float>() {{
        put("drawings", 0.1f);
        put("hentai", 0.0f);
        put("neutral", 0.8f);
        put("porn", 0.1f);
        put("sexy", 0.0f);
    }};

    private final Map<String, Float> resultsDrawings = new HashMap<String, Float>() {{
        put("drawings", 0.55f);
        put("hentai", 0.05f);
        put("neutral", 0.2f);
        put("porn", 0.1f);
        put("sexy", 0.1f);
    }};

    private final Map<String, Float> resultsSexy = new HashMap<String, Float>() {{
        put("drawings", 0.1f);
        put("hentai", 0.0f);
        put("neutral", 0.0f);
        put("porn", 0.1f);
        put("sexy", 0.8f);
    }};

    private final Map<String, Float> resultsSexyConfuse = new HashMap<String, Float>() {{
        put("drawings", 0.0f);
        put("hentai", 0.05f);
        put("neutral", 0.0f);
        put("porn", 0.42f);
        put("sexy", 0.53f);
    }};

    private final Map<String, Float> resultsDrawingsConfuse = new HashMap<String, Float>() {{
        put("drawings", 0.54f);
        put("hentai", 0.05f);
        put("neutral", 0.0f);
        put("porn", 0.42f);
        put("sexy", 0.0f);
    }};

    private final Map<String, Float> resultsPorn = new HashMap<String, Float>() {{
        put("drawings", 0.0f);
        put("hentai", 0.1f);
        put("neutral", 0.0f);
        put("porn", 0.6f);
        put("sexy", 0.3f);
    }};

    private final Map<String, Float> resultHentai = new HashMap<String, Float>() {{
        put("drawings", 0.15f);
        put("hentai", 0.75f);
        put("neutral", 0.0f);
        put("porn", 0.1f);
        put("sexy", 0.0f);
    }};

    @Test
    public void resultNeutralFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultsNeutral, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertFalse(isNsfw);
    }

    @Test
    public void resultDrawingsFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultsDrawings, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertFalse(isNsfw);
    }

    @Test
    public void resultSexyFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultsSexy, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertFalse(isNsfw);
    }

    @Test
    public void resultSexyConfuseFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultsSexyConfuse, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertTrue(isNsfw);
    }

    @Test
    public void resultDrawingsConfuseFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultsDrawingsConfuse, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertFalse(isNsfw);
    }

    @Test
    public void resultPornFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultsPorn, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertTrue(isNsfw);
    }

    @Test
    public void resultHentaiFromNsfwClassifierCorrect() {
        ResultBundle resultBundle = new ResultBundle(resultHentai, null);
        boolean isNsfw = NsfwUtils.isNsfwImage(resultBundle, nsfwTags, nsfwConfuseTags);
        assertTrue(isNsfw);
    }
}
