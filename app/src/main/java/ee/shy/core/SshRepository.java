package ee.shy.core;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.pastdev.jsch.DefaultSessionFactory;

import java.io.Console;
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
     * @throws URISyntaxException
     */
    public static SshRepository newRemote(URI remoteUri) throws IOException, URISyntaxException {
        if (!"ssh".equals(remoteUri.getScheme()))
            throw new RuntimeException("URI must have 'ssh' scheme"); // TODO: 22.04.16 throw better exception

        DefaultSessionFactory sessionFactory = new DefaultSessionFactory();

        Map<String, Object> environment = Collections.singletonMap("defaultSessionFactory", sessionFactory);

        sessionFactory.setUserInfo(new ConsoleUserInfo());
        sessionFactory.setConfig("server_host_key", "ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521,ssh-rsa,ssh-dss"); // OpenSSH algorithm order for better known_hosts compatibility
        sessionFactory.setConfig("HashKnownHosts", "yes"); // only applies to adding hosts

        URI uri = new URI("ssh.unix", remoteUri.getUserInfo(), remoteUri.getHost(), remoteUri.getPort(), remoteUri.getPath(), remoteUri.getQuery(), remoteUri.getFragment());
        return new SshRepository(FileSystems.newFileSystem(uri, environment));
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }

    private static class ConsoleUserInfo implements UserInfo, UIKeyboardInteractive {
        private final Console console = System.console();

        private String passphrase;
        private String password;

        @Override
        public String getPassphrase() {
            return passphrase;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String message) {
            char[] password = console.readPassword(message + ": ");
            if (password == null)
                return false;

            this.password = new String(password);
            return true;
        }

        @Override
        public boolean promptPassphrase(String message) {
            char[] passphrase = console.readPassword(message + ": ");
            if (passphrase == null)
                return false;

            this.passphrase = new String(passphrase);
            return true;
        }

        @Override
        public boolean promptYesNo(String message) {
            String choice = console.readLine(message + ": ");
            return choice != null && choice.trim().toLowerCase().startsWith("y"); // y or yes
        }

        @Override
        public void showMessage(String message) {
            console.printf(message);
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            console.printf(instruction);

            String[] answers = new String[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                String answer;
                if (echo[i]) {
                    answer = console.readLine(prompt[i] + ": ");
                    if (answer == null)
                        return null;
                }
                else {
                    char[] answerChars = console.readPassword(prompt[i] + ": ");
                    if (answerChars == null)
                        return null;
                    answer = new String(answerChars);
                }
                answers[i] = answer;
            }
            return answers;
        }
    }
}
