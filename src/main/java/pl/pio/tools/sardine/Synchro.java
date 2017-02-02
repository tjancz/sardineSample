package pl.pio.tools.sardine;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import java.io.IOException;

/**
 * Created by tomasz on 01.02.17.
 */
public class Synchro {

    public static void main(String... args) throws IOException {
        Sardine connection = SardineFactory.begin(args[1],args[2]);
        connection.list(args[0]).forEach(System.out::println);
    }
}
