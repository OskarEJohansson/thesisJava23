package dev.oskarjohansson.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl


@Configuration
@PropertySource("classpath:application.properties")
class MailServiceConfiguration(
    @Value("\${smtp.password}")
    private val pass: String
) {

    private val LOG: Logger = LoggerFactory.getLogger(MailServiceConfiguration::class.java)

    @Bean
    fun mailSender(): JavaMailSender {
        LOG.debug("Password in MailServiceConfig ${pass}")
        return JavaMailSenderImpl().apply {
            username = "oskarcodeexplorer@gmail.com"
            password = pass
            host = "smtp.gmail.com"
            port = 587

            javaMailProperties.apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.transport.protocol", "smtp")
            }
        }
    }

    @Bean
    fun templateMessage() = SimpleMailMessage().apply {
        from = "activateYourAccountt@gmail.com"
        subject = "Activation Token"
    }

}