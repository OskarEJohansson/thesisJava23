package dev.oskarjohansson.service

import dev.oskarjohansson.model.ActivationToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class MailService()  {

    lateinit var mailSender: JavaMailSender
    lateinit var templateMessage: SimpleMailMessage

    private var LOG: Logger = LoggerFactory.getLogger(MailService::class.java)

    // TODO: For dev exploration only
    var tokenAddress = "http://localhost:8082/admin/v1/activate-account/"

    fun sendMail(activationToken: ActivationToken, address:String){

        val msg = SimpleMailMessage(this.templateMessage)
        msg.setTo(activationToken.email)
        msg.text = ("Please activate your account by clicking the link: $address, \n dev-address: $tokenAddress ${activationToken.token} ")

        runCatching {
            mailSender.send(msg)
        }.onFailure {
            LOG.debug("Failed to send mail ${it.message}")
        }.getOrThrow()
    }
}