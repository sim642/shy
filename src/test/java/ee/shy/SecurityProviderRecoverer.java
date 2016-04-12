package ee.shy;

import org.junit.rules.ExternalResource;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link org.junit.rules.TestRule} which recovers registered security providers after each test.
 */
public class SecurityProviderRecoverer extends ExternalResource {
    /**
     * Providers before running the test.
     */
    private List<Provider> oldProviders;

    @Override
    protected void before() throws Throwable {
        oldProviders = Arrays.asList(Security.getProviders());
    }

    @Override
    protected void after() {
        List<Provider> newProviders = Arrays.asList(Security.getProviders());

        // add all providers removed during the test
        List<Provider> removedProviders = new ArrayList<>(oldProviders);
        removedProviders.removeAll(newProviders);
        for (Provider removedProvider : removedProviders) {
            Security.addProvider(removedProvider);
        }

        // remove all providers added during the test
        List<Provider> addedProviders = new ArrayList<>(newProviders);
        addedProviders.removeAll(oldProviders);
        for (Provider addedProvider : addedProviders) {
            Security.removeProvider(addedProvider.getName());
        }
    }
}
