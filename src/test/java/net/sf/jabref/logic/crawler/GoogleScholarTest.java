package net.sf.jabref.logic.crawler;

import net.sf.jabref.BibtexEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.junit.Assert.*;

public class GoogleScholarTest {
    GoogleScholar finder;
    BibtexEntry entry;

    @Before
    public void setup() {
        finder = new GoogleScholar();
        entry = new BibtexEntry();
    }

    @Test(expected = NullPointerException.class)
    public void rejectNullParameter() throws IOException {
        finder.findFullText(null);
    }

    @Test
    public void requiresEntryTitle() throws IOException {
        Assert.assertEquals(Optional.empty(), finder.findFullText(entry));
    }

    @Test
    public void linkFound() throws IOException {
        entry.setField("title", "Towards Application Portability in Platform as a Service");

        Assert.assertEquals(
                Optional.of(new URL("https://www.uni-bamberg.de/fileadmin/uni/fakultaeten/wiai_lehrstuehle/praktische_informatik/Dateien/Publikationen/sose14-towards-application-portability-in-paas.pdf")),
                finder.findFullText(entry)
        );
    }

    @Test
    public void noLinkFound() throws IOException {
        Assert.assertEquals(Optional.empty(), finder.findFullText(entry));
    }
}