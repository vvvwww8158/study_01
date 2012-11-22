package code.snippet

import scala.xml.{ NodeSeq, Text }
import net.liftweb.common.{ Box, Full, Empty, Failure, Loggable }
import net.liftweb.http.{ SHtml, SessionVar, RequestVar, S, DispatchSnippet }
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.http.js.{ JsCmds, JE }
import net.liftweb.http.js.jquery.JqJsCmds
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._
import code.service.LoginService
import code.model.rdb.Login

object LoginSnippet {

  private object reqSigninId extends RequestVar[String]("")
  private object reqPassword extends RequestVar[String]("")
  private object reqaaa extends RequestVar[String]("")
  private object reqRemember extends RequestVar[Boolean](false)
  private object theAccount extends SessionVar[Option[Login]](None)

  def login(nodeSeq: NodeSeq): NodeSeq = {

    def performSignin() = {
     
    }

    val cssSel =
      "@username" #> SHtml.text(reqSigninId.is, reqSigninId(_)) &
        "@password" #> SHtml.password(reqPassword.is, reqPassword(_)) &
        "@check" #> SHtml.checkbox(reqRemember.is, reqRemember(_)) &
        "@sub" #> SHtml.submit("登录", performSignin)
    cssSel(nodeSeq)
  }

  def showName(nodeSeq: NodeSeq): NodeSeq = {
    val cssSel =
      "id=name" #> theAccount.is.map(_.username)
    cssSel(nodeSeq)
  }
}