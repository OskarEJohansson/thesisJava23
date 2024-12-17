package dev.oskarjohansson.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val templateMessage: SimpleMailMessage,
) {

    private var LOG: Logger = LoggerFactory.getLogger(MailService::class.java)

    fun sendMail(activationToken: String, userEmailAddress: String, hostAddress: String, moduleAddress:String) {

        LOG.info("Activation token address: $hostAddress/$moduleAddress/$activationToken")
        val msg = SimpleMailMessage(templateMessage)
        msg.setTo(userEmailAddress)
        msg.text =

            ("Please activate your account by clicking the link: $hostAddress/$moduleAddress/$activationToken ")

        runCatching {
            mailSender.send(msg)
        }.onFailure {
            LOG.debug("Failed to send mail ${it.message}")
        }.getOrThrow()
    }
}