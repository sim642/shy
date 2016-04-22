package ee.shy.core;

import com.jcraft.jsch.JSchException;
import com.pastdev.jsch.DefaultSessionFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository accessed over a SSH connection using SFTP.
 */
public class SshRepository extends Repository {
    private final FileSystem fileSystem;

    /**
     * Constructs a new SSH repository class.
     * @param fileSystem SSH FileSystem for repository.
     */
    private SshRepository(FileSystem fileSystem) throws IOException {
        super(fileSystem.getPath(""));
        this.fileSystem = fileSystem;
    }

    /**
     * Connects to a remote SSH repository.
     * @return a Repository object if remote repository was found
     * @throws IOException
     * @throws JSchException
     * @throws URISyntaxException
     */
    public static SshRepository newRemote() throws IOException, JSchException, URISyntaxException {
        DefaultSessionFactory sessionFactory = new DefaultSessionFactory("shy", "localhost", 22);
        Map<String, Object> environment = new HashMap<>();
        environment.put("defaultSessionFactory", sessionFactory);

        // TODO: 21.04.16 allow password user input and keyboard-interactive

        URI uri = new URI("ssh.unix://" + sessionFactory.getUsername() + "@" + sessionFactory.getHostname() + ":" + sessionFactory.getPort() + "/home/shy/test");
        return new SshRepository(FileSystems.newFileSystem(uri, environment));
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
