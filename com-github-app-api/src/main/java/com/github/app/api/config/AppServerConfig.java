package com.github.app.api.config;

public class AppServerConfig {
    private int apiPort = 8080;
    /** TLS **/
    private boolean tlsEnabled = false;
    private String tlsCertificateFilePath;
    private String tlsCertificatePassword;
    /** jwt token **/
    private String jwtCertificateFilePath;
    private String jwtCertificatePassword;
    /** datasource **/
    private boolean dataSourceAutoInit = false;
    private String dataSourceDriverClassName;
    private String dataSourceUrl;
    private String dataSourceUserName;
    private String dataSourcePassword;
    /** vertx cache directory **/
    private String vertxCacheDir;

    /** slow sql performance monitor **/
    private boolean slowSqlMonitorEnabled = true;
    private long slowSqlTimeInMs = 3000;

    public int getApiPort() {
        return apiPort;
    }

    public void setApiPort(int apiPort) {
        this.apiPort = apiPort;
    }

    public boolean isTlsEnabled() {
        return tlsEnabled;
    }

    public void setTlsEnabled(boolean tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
    }

    public String getTlsCertificateFilePath() {
        return tlsCertificateFilePath;
    }

    public void setTlsCertificateFilePath(String tlsCertificateFilePath) {
        this.tlsCertificateFilePath = tlsCertificateFilePath;
    }

    public String getTlsCertificatePassword() {
        return tlsCertificatePassword;
    }

    public void setTlsCertificatePassword(String tlsCertificatePassword) {
        this.tlsCertificatePassword = tlsCertificatePassword;
    }

    public String getJwtCertificateFilePath() {
        return jwtCertificateFilePath;
    }

    public void setJwtCertificateFilePath(String jwtCertificateFilePath) {
        this.jwtCertificateFilePath = jwtCertificateFilePath;
    }

    public String getJwtCertificatePassword() {
        return jwtCertificatePassword;
    }

    public void setJwtCertificatePassword(String jwtCertificatePassword) {
        this.jwtCertificatePassword = jwtCertificatePassword;
    }

    public String getDataSourceDriverClassName() {
        return dataSourceDriverClassName;
    }

    public void setDataSourceDriverClassName(String dataSourceDriverClassName) {
        this.dataSourceDriverClassName = dataSourceDriverClassName;
    }

    public String getDataSourceUrl() {
        return dataSourceUrl;
    }

    public void setDataSourceUrl(String dataSourceUrl) {
        this.dataSourceUrl = dataSourceUrl;
    }

    public String getDataSourceUserName() {
        return dataSourceUserName;
    }

    public void setDataSourceUserName(String dataSourceUserName) {
        this.dataSourceUserName = dataSourceUserName;
    }

    public String getDataSourcePassword() {
        return dataSourcePassword;
    }

    public void setDataSourcePassword(String dataSourcePassword) {
        this.dataSourcePassword = dataSourcePassword;
    }

    public String getVertxCacheDir() {
        return vertxCacheDir;
    }

    public void setVertxCacheDir(String vertxCacheDir) {
        this.vertxCacheDir = vertxCacheDir;
    }

    public boolean isSlowSqlMonitorEnabled() {
        return slowSqlMonitorEnabled;
    }

    public void setSlowSqlMonitorEnabled(boolean slowSqlMonitorEnabled) {
        this.slowSqlMonitorEnabled = slowSqlMonitorEnabled;
    }

    public long getSlowSqlTimeInMs() {
        return slowSqlTimeInMs;
    }

    public void setSlowSqlTimeInMs(long slowSqlTimeInMs) {
        this.slowSqlTimeInMs = slowSqlTimeInMs;
    }

    public boolean isDataSourceAutoInit() {
        return dataSourceAutoInit;
    }

    public void setDataSourceAutoInit(boolean dataSourceAutoInit) {
        this.dataSourceAutoInit = dataSourceAutoInit;
    }

    @Override
    public String toString() {
        return "AppServerConfig{" +
                "apiPort=" + apiPort +
                ", tlsEnabled=" + tlsEnabled +
                ", tlsCertificateFilePath='" + tlsCertificateFilePath + '\'' +
                ", tlsCertificatePassword='" + tlsCertificatePassword + '\'' +
                ", jwtCertificateFilePath='" + jwtCertificateFilePath + '\'' +
                ", jwtCertificatePassword='" + jwtCertificatePassword + '\'' +
                ", dataSourceAutoInit=" + dataSourceAutoInit +
                ", dataSourceDriverClassName='" + dataSourceDriverClassName + '\'' +
                ", dataSourceUrl='" + dataSourceUrl + '\'' +
                ", dataSourceUserName='" + dataSourceUserName + '\'' +
                ", dataSourcePassword='" + dataSourcePassword + '\'' +
                ", vertxCacheDir='" + vertxCacheDir + '\'' +
                ", slowSqlMonitorEnabled=" + slowSqlMonitorEnabled +
                ", slowSqlTimeInMs=" + slowSqlTimeInMs +
                '}';
    }
}
