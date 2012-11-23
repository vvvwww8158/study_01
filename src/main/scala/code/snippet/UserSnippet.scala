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

object UserSnippet {

  private object reqUserID extends RequestVar[String]("")
  private object reqUsername extends RequestVar[String]("")
  private object reqEmail extends RequestVar[String]("")

  def showAllUser(nodeSeq: NodeSeq): NodeSeq = {
    val userList = UserService.getAllUser
    val cssSel =
      "#manage" #>
        userList.map { user =>
          <tr>
            <td>{ user.id }</td>
            <td>{ user.username }</td>
            <td>{ user.email }</td>
            <td>
              { <a href={ "edit?id=" + user.id }>编辑</a> }
              { <a href={ "delete?id=" + user.id } style="color:red;">删除</a> }
            </td>
          </tr>
        }
    cssSel(nodeSeq)
  }
  def delete(nodeSeq: NodeSeq): NodeSeq = {
    val reqId = S.param("id").map(urlDecode(_))
    println("reqId=" + reqId)
    NodeSeq.Empty
  }

  def edit(nodeSeq: NodeSeq): NodeSeq = {

    val reqId = S.param("id").map(urlDecode(_))
    val user = (for (id <- reqId; user <- UserService.findUser(id)) yield user) getOrElse new User("", "", "", "")
    val cssSel = "@userId" #> SHtml.text(user.id, user.id = _) &
      "@realName" #> SHtml.text(user.username, user.username = _) &
      "@email" #> SHtml.text(user.email, user.email = _) &
      "@save" #> SHtml.submit("保存", () => {
        println("username=" + reqUsername.is)
        val tag = UserService.updateUser(user)
        if (tag) {
          println("username=" + reqUsername.is)
          S.notice("修改成功")
          S.redirectTo("edit?id=" + user.id)
        } else {
          S.error("未能修改成功")
          S.redirectTo("edit?id=" + user.id)
        }
      })
    cssSel(nodeSeq)
  }
}