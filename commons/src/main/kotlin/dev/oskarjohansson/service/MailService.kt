package dev.oskarjohansson.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val templateMessage: SimpleMailMessage
) {


    private var LOG: Logger = LoggerFactory.getLogger(MailService::class.java)

    // TODO: For dev exploration only
    var tokenAddress = "http://localhost:8080/admin/v1/activate-account/"

    fun sendMail(activationToken: String, address: String) {

        val msg = SimpleMailMessage(templateMessage)
        msg.setTo(address)
        msg.text =

            ("Please activate your account by clicking the link: \n dev-address: $tokenAddress$activationToken ")


        runCatching {
            mailSender.send(msg)
        }.onFailure {
            LOG.debug("Failed to send mail ${it.message}")
        }.getOrThrow()
    }
}