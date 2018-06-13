package com.github.app.spi.config;

public class AppServerConfig {
    private int apiPort = 8080;
    /**
     * TLS
     **/
    private boolean tlsEnabled = false;
    private String tlsCertificateFilePath;
    private String tlsCertificatePassword;
    /**
     * jwt token
     **/
    private String jwtCertificateFilePath;
    private String jwtCertificatePassword;
    /**
     * datasource
     **/
    private boolean dataSourceAutoInit = false;
    private String dataSourceDriverClassName;
    private String dataSourceUrl;
    private String dataSourceUserName;
    private String dataSourcePassword;
    /**
     * vertx cache directory
     **/
    private String vertxCacheDir;

    /**
     * slow sql performance monitor
     **/
    private boolean slowSqlMonitorEnabled = true;
    private long slowSqlTimeInMs = 3000;
    /**
     * jwt token expires time in minutes
     */
    private int jwtTokenExpiresInMinutes = 3 * 60;

    /**
     * captcha configuration
     */
    private boolean captchaEnabled = true;
    private int captchaLength = 5;
    private String captchaModel = "letter";

    /**
     * email configuration
     */
    private String emailHostname;
    private int emailSmtpPort;
    private String emailUserName;
    private String emailPassword;
    private boolean emailSslOnConnect;
    private String emailFrom;
    private String emailFromName;

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

    public int getJwtTokenExpiresInMinutes() {
        return jwtTokenExpiresInMinutes;
    }

    public void setJwtTokenExpiresInMinutes(int jwtTokenExpiresInMinutes) {
        this.jwtTokenExpiresInMinutes = jwtTokenExpiresInMinutes;
    }

    public boolean isCaptchaEnabled() {
        return captchaEnabled;
    }

    public void setCaptchaEnabled(boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

    public int getCaptchaLength() {
        return captchaLength;
    }

    public void setCaptchaLength(int captchaLength) {
        this.captchaLength = captchaLength;
    }

    public String getCaptchaModel() {
        return captchaModel;
    }

    public void setCaptchaModel(String captchaModel) {
        this.captchaModel = captchaModel;
    }

    public String getEmailHostname() {
        return emailHostname;
    }

    public void setEmailHostname(String emailHostname) {
        this.emailHostname = emailHostname;
    }

    public int getEmailSmtpPort() {
        return emailSmtpPort;
    }

    public void setEmailSmtpPort(int emailSmtpPort) {
        this.emailSmtpPort = emailSmtpPort;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public void setEmailUserName(String emailUserName) {
        this.emailUserName = emailUserName;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public boolean isEmailSslOnConnect() {
        return emailSslOnConnect;
    }

    public void setEmailSslOnConnect(boolean emailSslOnConnect) {
        this.emailSslOnConnect = emailSslOnConnect;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailFromName() {
        return emailFromName;
    }

    public void setEmailFromName(String emailFromName) {
        this.emailFromName = emailFromName;
    }
}
