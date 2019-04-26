package cz.scholz.rhsummit2019amqstreams.pricer;

public class PricerConfig {
    private final String bootstrapServers;
    private final String leftSourceTopic;
    private final String rightSourceTopic;
    private final String targetTopic;
    private final String trustStorePassword;
    private final String trustStorePath;
    private final String keyStorePassword;
    private final String keyStorePath;
    private final String username;
    private final String password;

    public PricerConfig(String bootstrapServers, String leftSourceTopic, String rightSourceTopic, String targetTopic, String trustStorePassword, String trustStorePath, String keyStorePassword, String keyStorePath, String username, String password) {
        this.bootstrapServers = bootstrapServers;
        this.leftSourceTopic = leftSourceTopic;
        this.rightSourceTopic = rightSourceTopic;
        this.targetTopic = targetTopic;
        this.trustStorePassword = trustStorePassword;
        this.trustStorePath = trustStorePath;
        this.keyStorePassword = keyStorePassword;
        this.keyStorePath = keyStorePath;
        this.username = username;
        this.password = password;
    }

    public static PricerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String leftSourceTopic = System.getenv("LEFT_SOURCE_TOPIC");
        String rightSourceTopic = System.getenv("RIGHT_SOURCE_TOPIC");
        String targetTopic = System.getenv("TARGET_TOPIC");
        String trustStorePassword = System.getenv("TRUSTSTORE_PASSWORD") == null ? null : System.getenv("TRUSTSTORE_PASSWORD");
        String trustStorePath = System.getenv("TRUSTSTORE_PATH") == null ? null : System.getenv("TRUSTSTORE_PATH");
        String keyStorePassword = System.getenv("KEYSTORE_PASSWORD") == null ? null : System.getenv("KEYSTORE_PASSWORD");
        String keyStorePath = System.getenv("KEYSTORE_PATH") == null ? null : System.getenv("KEYSTORE_PATH");
        String username = System.getenv("USERNAME") == null ? null : System.getenv("USERNAME");
        String password = System.getenv("PASSWORD") == null ? null : System.getenv("PASSWORD");

        return new PricerConfig(bootstrapServers, leftSourceTopic, rightSourceTopic, targetTopic, trustStorePassword, trustStorePath, keyStorePassword, keyStorePath, username, password);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getLeftSourceTopic() {
        return leftSourceTopic;
    }

    public String getRightSourceTopic() {
        return rightSourceTopic;
    }

    public String getTargetTopic() {
        return targetTopic;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
