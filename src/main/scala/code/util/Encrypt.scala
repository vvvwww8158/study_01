package code.util

import java.security.MessageDigest

object Encrypt {

  def md5Ecrypt(s: String): String = {
    val digest = MessageDigest.getInstance("MD5")
    digest.digest(s.getBytes()).map("%2x".format(_)).mkString
  }
}