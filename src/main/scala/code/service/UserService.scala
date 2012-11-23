package code.service

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Query
import java.sql.Connection
import ch.qos.logback.core.db.DBHelper
import code.model.rdb.DbHelper
import code.model.rdb.User
import java.lang.Exception
import scala.collection.mutable.ListBuffer
import code.util.Encrypt

object UserService {

  //添加用户
  def addUser(u: User): Boolean = {
    val con = DbHelper.conn
    val p = con.prepareStatement("insert into users(id,username,password,email) values (?,?,?,?)")
    val p2 = con.prepareStatement("insert into userrole(userid,roleid) values (?,'user')")
    println("insert into userrole(userid,roleid) values ('" + u.id + "','user')")
    p.setString(1, u.id)
    p.setString(2, u.username)
    p.setString(3, u.password)
    p.setString(4, u.email)
    p2.setString(1, u.id)
    con.setAutoCommit(false)
    var tag = false;
    try {
      val t1 = p.execute()
      val t2 = p2.execute()
      tag = t1 && t2
      if (tag) con.commit()
      tag
    } catch {
      case e: Exception =>
        con.rollback()
        false
    } finally {
      con.setAutoCommit(true)
      con.close()
    }
  }

  //登录验证
  def checkLogin(id: String, password: String): Option[User] = {
    val u = findUser(id)
    if (u != None) {
      val user = u.get
      println(user.id + "==" + id)
      if (user.password.equals(Encrypt.md5Ecrypt(password))) {
        Some(user)
      } else
        None
    } else
      None
  }

  //查找用户
  def findUser(id: String): Option[User] = {
    println("id="+id)
    val con = DbHelper.conn
    val p = con.prepareStatement("select * from users where id = ?")
    p.setString(1, id)
    try {
      val rs = p.executeQuery()
      if (rs.next()) {
        val u = new User(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"))
        Some(u)
      } else
        None
    } finally {
      p.close()
      con.close()
    }
  }

  def getRole(id: String): Boolean = {
    val con = DbHelper.conn
    val p = con.prepareStatement("select * from userrole where userid=?")
    p.setString(1, id)
    var tag = false
    try {
      val rs = p.executeQuery()
      if (rs.next()) {
        tag = rs.getString("roleid").equals("admin")
        tag
      } else
        tag
    } finally {
      p.close()
      con.close()
    }
  }

  def getAllUser(): ListBuffer[User] = {
    val userList = new ListBuffer[User]
    val con = DbHelper.conn
    val p = con.prepareStatement("select u.* from users u,userrole ur where u.id=ur.userid and ur.roleid='user'")
    try {
      val rs = p.executeQuery()
      while (rs.next()) {
        val u = new User(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"))
        userList += u
      }
      userList
    } finally {
      p.close()
      con.close()
    }
  }

  def updateUser(user: User): Boolean = {
    val con = DbHelper.conn
    val p = con.prepareStatement("update users set username=?,email=? where id=?")
    p.setString(1, user.username)
    p.setString(2, user.email)
    p.setString(3, user.id)
    try {
      p.executeUpdate()
      true
    } catch {
      case e: Exception =>
        e.printStackTrace()
        false
    } finally {
      p.close()
      con.close()
    }
  }

}
