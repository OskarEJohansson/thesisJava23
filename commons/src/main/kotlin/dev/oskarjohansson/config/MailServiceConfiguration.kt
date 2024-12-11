package dev.oskarjohansson.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl


@Configuration
class MailServiceConfiguration {

    @Bean
    fun mailSender(): JavaMailSender {
        return JavaMailSenderImpl().apply{
            username = "oskarcodeexplorer@gmail.com"
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