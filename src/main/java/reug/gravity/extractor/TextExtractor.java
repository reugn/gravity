package reug.gravity.extractor;

import java.io.InputStream;

public interface TextExtractor {

    /**
     * Extract text from document byte array
     *
     * @param target byte array of a target document
     * @return extracted text
     * @throws Exception tika parse exception
     */
    String extract(byte[] target) throws Exception;

    /**
     * Extract text from document InputStream
     *
     * @param is InputStream of a target document
     * @return extracted text
     * @throws Exception tika parse exception
     */
    String extract(InputStream is) throws Exception;
}
