package jenkins;

import java.util.Properties;

public class Settings {
    private static volatile Settings instance;
    // properties
    private final String jenkinsURL;
    private final String login;
    private final String password;
    private final String jobName;
    private final int buildNumber;
    private final boolean isParseExceptions;
    // output
    private final String outputDir;
    private final String dbOutputDir;
    private final String dvn;

    public static Settings getInstance(Properties properties) {
        if (instance == null) {
            synchronized (Settings.class) {
                if (instance == null) {
                    instance = new Settings(properties);
                }
            }
        }
        return instance;
    }

    private Settings(Properties properties) {
        // jenkins
        this.jenkinsURL = properties.getProperty("jenkinsURL");
        this.login = properties.getProperty("loginToJenkins");
        this.password = properties.getProperty("passwordToJenkins");
        this.jobName = properties.getProperty("jobName");
        this.buildNumber = Integer.parseInt(properties.getProperty("buildNumber"));
        this.isParseExceptions = Boolean.parseBoolean(properties.getProperty("isParseExceptions"));
        // output
        this.outputDir = properties.getProperty("outputDir");
        this.dbOutputDir = properties.getProperty("dbOutputDir");
        this.dvn = properties.getProperty("dvn");
    }

    public String getJenkinsURL() {
        return jenkinsURL;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getJobName() {
        return jobName;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getDbOutputDir() {
        return dbOutputDir;
    }

    public String getDvn() {
        return dvn;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "jenkinsURL='" + jenkinsURL + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", jobName='" + jobName + '\'' +
                ", buildNumber=" + buildNumber +
                ", isParseExceptions=" + isParseExceptions +
                // output
                ", outputDir=" + outputDir +
                ", dbOutputDir=" + dbOutputDir +
                ", dvn=" + dvn +
                '}';
    }
}
