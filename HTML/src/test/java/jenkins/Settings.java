package jenkins;

import java.util.Properties;

public class Settings {
    private String jenkinsURL;
    private String login;
    private String password;
    private String jobName;
    private int buildNumber;
    private boolean isParseExceptions;

    public Settings(Properties properties) {
        this.jenkinsURL = properties.getProperty("jenkinsURL");
        this.login = properties.getProperty("loginToJenkins");
        this.password = properties.getProperty("passwordToJenkins");
        this.jobName = properties.getProperty("jobName");
        this.buildNumber = Integer.parseInt(properties.getProperty("buildNumber"));
        this.isParseExceptions = Boolean.parseBoolean(properties.getProperty("isParseExceptions"));
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

    @Override
    public String toString() {
        return "Settings{" +
                "jenkinsURL='" + jenkinsURL + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", jobName='" + jobName + '\'' +
                ", buildNumber=" + buildNumber +
                ", isParseExceptions=" + isParseExceptions +
                '}';
    }
}
