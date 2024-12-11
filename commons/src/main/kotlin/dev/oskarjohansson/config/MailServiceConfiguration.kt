package dev.oskarjohansson.config

import dev.oskarjohansson.service.MailService
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
            host = "smtp.gmail.com"
            port = 587
        }
    }

    @Bean
    fun templateMessage() = SimpleMailMessage().apply {
        from = "tokenService@mycompany.example"
        subject = "Activation Token"
    }

    @Bean
    fun mailService(javaMailSender: JavaMailSender, simpleTemplateMessage: SimpleMailMessage) = MailService().apply {
        mailSender = javaMailSender
        templateMessage = simpleTemplateMessage
    }
}