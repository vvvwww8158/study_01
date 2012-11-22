package code.model.rdb

import java.sql._

import scala.Array
object DbHelper {

  Class.forName("org.postgresql.Driver")

  def conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/study_01", "devuser", "devpass")

//  def main(args: Array[String]) {
//    try {
//     
//      val ps = conn.prepareStatement("insert into users(id,username,password,email) values ('admini','Tom','123456','aaa@qq.com')")
//      ps.execute()
//    } finally {
//    	conn.close()
//    }
//
//  }

}