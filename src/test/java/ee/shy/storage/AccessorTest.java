package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import ee.shy.io.PathUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AccessorTest {
    private static final String TEST_STRING = "Hello, World!";

    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private FileAccessor accessor;
    private Predicate<Path> fileChecker;

    private Path file;

    @Parameters
    public static Collection<Object[]> accessors() {
        Supplier<FileAccessor> plainSupplier = () -> new PlainFileAccessor();
        Predicate<Path> plainChecker = path -> Files.isRegularFile(path);

        Supplier<FileAccessor> gzipSupplier = () -> new GzipFileAccessor();
        Predicate<Path> gzipChecker = path -> Files.isRegularFile(PathUtils.addExtension(path, ".gz"));

        Supplier<FileAccessor> aggregateSupplier = () -> new AggregateFileAccessor(Arrays.asList(
                gzipSupplier.get(),
                plainSupplier.get()
        ));

        return Arrays.asList(new Object[][]{
                {plainSupplier, plainChecker},
                {gzipSupplier, gzipChecker},
                {aggregateSupplier, gzipChecker}
        });
    }

    public AccessorTest(Supplier<FileAccessor> accessorSupplier, Predicate<Path> fileChecker) {
        this.accessor = accessorSupplier.get();
        this.fileChecker = fileChecker;
    }

    @Before
    public void setUp() throws Exception {
        file = temporaryDirectory.newFile();
        Files.delete(file);
    }

    @Test
    public void testRetrieve() throws Exception {
        accessor.add(file, IOUtils.toInputStream(TEST_STRING));
        assertTrue(fileChecker.test(file));
        assertEquals(TEST_STRING, IOUtils.toString(accessor.get(file)));
    }

    @Test
    public void testNonExistent() throws Exception {
        assertNull(accessor.get(file));
    }
}
