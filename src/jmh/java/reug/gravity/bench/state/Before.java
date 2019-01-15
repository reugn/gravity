package reug.gravity.bench.state;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import reug.gravity.model.Pattern;
import reug.gravity.util.Utils;

import java.io.InputStream;

@State(Scope.Thread)
public class Before {

    public InputStream is = Utils.readResource("/apache-license-2.0.txt");
    public Pattern pattern = new Pattern("within third-party archives", 1);
}
