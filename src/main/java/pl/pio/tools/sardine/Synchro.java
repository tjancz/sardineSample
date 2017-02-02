package pl.pio.tools.sardine;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.apache.commons.io.FileUtils;
import sun.misc.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
            if(davResource.isPresent()) {
                final DavResource magazyn = davResource.get();
                final File temporary = File.createTempFile("magazyn",".pdf");
                final InputStream magazynStream = synchro.getConnection().get(args[0]+magazyn.getPath());
                FileUtils.copyInputStreamToFile(magazynStream,temporary);
                Desktop.getDesktop().open(temporary);
            }
        }

    }

    private Optional<DavResource> getActualNo(String path) throws IOException {
        return connection.list(path,1,true).stream().filter(resource -> "application/pdf".equals(resource.getContentType()))
                .sorted((resourceA, resourceB) -> {
                    if(resourceA == null || resourceB == null) {
                        return 0;
                    }
                    if(resourceA.getModified() == null || resourceB.getModified() == null) {
                        return 0;
                    }
                    return resourceA.getModified().after(resourceB.getModified()) ? -1 : 1;
                }).findFirst();
    }

    private Sardine getConnection() {
        return connection;
    }

    private Optional<DavResource> getRootResource(List<DavResource> list) throws IOException {
        return list.stream().filter(devResource -> "magazyn_programista".equals(devResource.getName())).findFirst();
    }
}
