package dev.oskarjohansson.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val templateMessage: SimpleMailMessage,
    @Value(value = "\${domain.host.address}") private val hostAddress:String
) {


    // TODO:  FIGURE OUT HOW TO WRITE ADDRESS AS A CONFIG MAP


    private var LOG: Logger = LoggerFactory.getLogger(MailService::class.java)

    fun sendMail(activationToken: String, address: String) {

        val msg = SimpleMailMessage(templateMessage)
        msg.setTo(address)
        msg.text =

            ("Please activate your account by clicking the link: \n dev-address: $hostAddress/v1/activate-account/$activationToken ")


        runCatching {
            mailSender.send(msg)
        }.onFailure {
            LOG.debug("Failed to send mail ${it.message}")
        }.getOrThrow()
    }
}