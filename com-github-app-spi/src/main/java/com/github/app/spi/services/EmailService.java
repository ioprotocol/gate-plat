package com.github.app.spi.services;

import org.apache.commons.mail.EmailAttachment;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface EmailService {

    /**
     * @param subject
     * @param message
     * @param toEmailAddress
     * @return
     */
    CompletableFuture<Void> sendEmail(String subject, String message, String... toEmailAddress);

    /**
     * @param subject
     * @param message
     * @param attachment
     * @param toEmailAddress
     * @return
     */
    CompletableFuture<Void> sendEmail(String subject, String message, EmailAttachment attachment, String... toEmailAddress);

    /**
     * @param subject
     * @param html
     * @param datasourceResolver
     * @param toEmailAddress
     * @return
     */
    CompletableFuture<Void> sendEmail(String subject, String html, URL datasourceResolver, String... toEmailAddress);
}
