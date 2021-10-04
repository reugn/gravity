package io.github.reugn.gravity.bench.state;

import io.github.reugn.gravity.model.Pattern;
import io.github.reugn.gravity.util.Utils;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.InputStream;

@State(Scope.Thread)
public class Before {

    public InputStream is = Utils.readResource("/apache-license-2.0.txt");
    public Pattern pattern = new Pattern("within third-party archives", 1);
}
