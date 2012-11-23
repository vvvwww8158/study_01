package code.snippet

import scala.xml._
import net.liftweb.http.DispatchSnippet
import net.liftweb.http.RequestVar
import net.liftweb.http.SHtml
import net.liftweb.http.SessionVar
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds.SetHtml
import code.model.rdb.User
import code.service.UserService
import net.liftweb.http.S
import code.util.Encrypt

object RegistSnippet {
  private object reqUserId extends RequestVar[String]("")
  private object reqPassword extends RequestVar[String]("")
  private object reqRePassword extends RequestVar[String]("")
  private object reqRealName extends RequestVar[String]("")
  private object reqEmail extends RequestVar[String]("")

  def regist(nodeSeq: NodeSeq): NodeSeq = {
    def reg() = {
      val u = UserService.findUser(reqUserId)
      u match {
        case None =>
          val encryptPass = Encrypt.md5Ecrypt(reqPassword.is)
          val user = new User(reqUserId.is, reqRealName.is, encryptPass, reqEmail.is)
          val b = UserService.addUser(user)
          if (b)
            S.notice("注册成功")
            S.redirectTo("/index")
        case Some(u) =>
          S.notice("已存在该用户")
      }

      /* if(reqUserId.is.trim().equals(""))
	     {
	       var message = "Null"
	      "#message" #> <p>message</p>
	     }*/
    }
    val cssSel =
      "@userId" #> SHtml.text(reqUserId.is, reqUserId(_)) &
        "@password" #> SHtml.password("", reqPassword(_)) &
        "@repass" #> SHtml.password("", reqRePassword(_)) &
        "@realName" #> SHtml.text(reqRealName.is, reqRealName(_)) &
        "@email" #> SHtml.text(reqEmail.is, reqEmail(_)) &
        "@reg" #> SHtml.submit("注册", reg)
    cssSel(nodeSeq)
  }
}