package com.github.reugn.gravity.traits;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface MimeTypeDetector {

    default String detectMimeType(String target) throws IOException {
        InputStream is = new ByteArrayInputStream(target.getBytes(StandardCharsets.UTF_8));
        return detectMimeType(is);
    }

    default String detectMimeType(InputStream is) throws IOException {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        MediaType mediaType = detector.detect(is, metadata);
        return mediaType.toString();
    }
}
