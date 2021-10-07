package io.github.reugn.gravity.extractor;

import java.io.InputStream;

public interface TextExtractor {

    /**
     * Extracts a text from the <code>target</code> byte array.
     *
     * @param target the byte array of a target source.
     * @return the extracted text, empty string if no text found.
     * @throws Exception on any data parsing error.
     */
    String extract(byte[] target) throws Exception;

    /**
     * Extracts a text from the <code>is</code> InputStream.
     *
     * @param is the InputStream of a target source.
     * @return the extracted text, empty string if no text found.
     * @throws Exception on any data parsing error.
     */
    String extract(InputStream is) throws Exception;
}
