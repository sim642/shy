package ee.shy.core;

import com.jcraft.jsch.JSchException;
import com.pastdev.jsch.DefaultSessionFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Collections;
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
     * @param remoteUri URI to SSH repository
     * @return a Repository object if remote repository was found
     * @throws IOException
     * @throws JSchException
     * @throws URISyntaxException
     */
    public static SshRepository newRemote(URI remoteUri) throws IOException, JSchException, URISyntaxException {
        if (!"ssh".equals(remoteUri.getScheme()))
            throw new RuntimeException("URI must have 'ssh' scheme"); // TODO: 22.04.16 throw better exception

        DefaultSessionFactory sessionFactory = new DefaultSessionFactory();

        Map<String, Object> environment = Collections.singletonMap("defaultSessionFactory", sessionFactory);

        // TODO: 21.04.16 allow password user input and keyboard-interactive

        URI uri = new URI("ssh.unix", remoteUri.getUserInfo(), remoteUri.getHost(), remoteUri.getPort(), remoteUri.getPath(), remoteUri.getQuery(), remoteUri.getFragment());
        return new SshRepository(FileSystems.newFileSystem(uri, environment));
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
