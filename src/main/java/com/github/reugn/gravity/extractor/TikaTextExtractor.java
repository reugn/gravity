package com.github.reugn.gravity.extractor;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TikaTextExtractor implements TextExtractor {

    @Override
    public String extract(byte[] target) throws Exception {
        InputStream is = new ByteArrayInputStream(target);
        return extract(is);
    }

    @Override
    public String extract(InputStream is) throws Exception {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        parser.parse(is, handler, metadata, context);
        return handler.toString();
    }
}
