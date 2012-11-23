package code.snippet

import scala.xml.{ NodeSeq, Text }
import net.liftweb.common.{ Box, Full, Empty, Failure, Loggable }
import net.liftweb.http.{ SHtml, SessionVar, RequestVar, S, DispatchSnippet }
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.http.js.{ JsCmds, JE }
import net.liftweb.http.js.jquery.JqJsCmds
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._
import code.service.UserService
import code.model.rdb.User
import net.liftweb.http.js.JsCmds.Alert

object LoginSnippet {

  private object reqSigninId extends RequestVar[String]("")
  private object reqPassword extends RequestVar[String]("")
  private object reqaaa extends RequestVar[String]("")
  private object reqRemember extends RequestVar[Boolean](false)
  private object theAccount extends SessionVar[Option[User]](None)

  def login(nodeSeq: NodeSeq): NodeSeq = {
    def performSignin() = {
      val ret = UserService.checkLogin(reqSigninId.is, reqPassword.is)
      ret match {
        case Some(u) =>
          theAccount(Some(u))
          S.redirectTo("/static/user.html")
        case None =>
          S.error("用户名或密码错误")
      }
    }

    val cssSel =
      "@username" #> SHtml.text(reqSigninId.is, reqSigninId(_)) &
        "@password" #> SHtml.password(reqPassword.is, reqPassword(_)) &
        "@check" #> SHtml.checkbox(reqRemember.is, reqRemember(_)) &
        "@sub" #> SHtml.submit("登录", performSignin) &
        "@reg" #> SHtml.button("注册", () => S.redirectTo("/static/regist.html"))
    cssSel(nodeSeq)
  }

  def showName(nodeSeq: NodeSeq): NodeSeq = {
    val cssSel =
      "id=name" #> theAccount.is.map(_.username)
    cssSel(nodeSeq)
  }

  def showManage(nodeSeq: NodeSeq): NodeSeq = {
    if (UserService.getRole(theAccount.is.get.id)) {
      val cssSel =
        "a" #> SHtml.a(() => S.redirectTo("/static/userManage.html"), Text("管理用户"))
      cssSel(nodeSeq)
    } else {
      val cssSel =
        "a" #> SHtml.a(() => Alert("You clicked me!"), Text(""))
      cssSel(nodeSeq)
    }

  }
}