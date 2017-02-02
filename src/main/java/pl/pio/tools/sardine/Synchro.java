package pl.pio.tools.sardine;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by tomasz on 01.02.17.
 */
public class Synchro {
    private Sardine connection;


    private Synchro(String[] args) {
        connection = SardineFactory.begin(args[1],args[2]);
    }

    public static void main(String... args) throws IOException {
        final Synchro synchro = new Synchro(args);

        Optional<DavResource> davResourceOptional = synchro.getRootResource(synchro.getConnection().list(args[0]+"/remote.php/webdav/"));
        if(davResourceOptional.isPresent()) {
            Optional<DavResource> davResource = synchro.getActualNo(args[0] + davResourceOptional.get().getPath());
        }

    }

    private Optional<DavResource> getActualNo(String path) throws IOException {
        connection.list(path,1,true).stream().filter(resource -> "application/pdf".equals(resource.getContentType())).forEach(resource -> System.out.println(resource.getModified()));
        return Optional.empty();
    }

    private Sardine getConnection() {
        return connection;
    }

    private Optional<DavResource> getRootResource(List<DavResource> list) throws IOException {
        return list.stream().filter(devResource -> "magazyn_programista".equals(devResource.getName())).findFirst();
    }
}
