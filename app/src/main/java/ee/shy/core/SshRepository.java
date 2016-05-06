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

        // TODO: 21.04.16 allow password user input and keyboard-interactive
        sessionFactory.setUserInfo(new ConsoleUserInfo());
        //sessionFactory.setConfig("HashKnownHosts", "yes"); // useless because jsch only uses this for writing new hosts, not checking existing ones

        URI uri = new URI("ssh.unix", remoteUri.getUserInfo(), remoteUri.getHost(), remoteUri.getPort(), remoteUri.getPath(), remoteUri.getQuery(), remoteUri.getFragment());
        return new SshRepository(FileSystems.newFileSystem(uri, environment));
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }

    private static class ConsoleUserInfo implements UserInfo, UIKeyboardInteractive {
        private Console console = System.console();

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
            System.out.println(message + ": ");

            char[] password = console.readPassword();
            if (password == null)
                return false;

            this.password = new String(password);
            return true;
        }

        @Override
        public boolean promptPassphrase(String message) {
            System.out.println(message + ": ");

            char[] passphrase = console.readPassword();
            if (passphrase == null)
                return false;

            this.passphrase = new String(passphrase);
            return true;
        }

        @Override
        public boolean promptYesNo(String message) {
            System.out.println(message + ": ");
            return console.readLine().trim().toLowerCase().startsWith("y"); // y or yes
        }

        @Override
        public void showMessage(String message) {
            System.out.println(message);
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            System.out.println(instruction);

            String[] answers = new String[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                System.out.println(prompt[i] + ": ");

                String answer;
                if (echo[i]) {
                    answer = console.readLine();
                    if (answer == null)
                        return null;
                }
                else {
                    char[] answerChars = console.readPassword();
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
