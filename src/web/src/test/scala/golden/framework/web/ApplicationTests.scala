package golden.framework.web

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scalaj.http.{Http, MultiPart}

class ApplicationTests extends AnyFunSuite with Matchers with BeforeAndAfterEach:

  var app: Application = _

  test("simple request should be handled") {
    anAppWith {
      _.addGetHandler("/", _.result("HELLO"))
    }

    val result = Http("http://localhost:7070/").asString

    result.body shouldBe "HELLO"
  }

  test("path parameter should be retrieve properly") {
    anAppWith {
      _.addGetHandler("/hello/{name}", ctx => ctx.result(s"HELLO ${ctx.pathParam("name").get}"))
    }

    val result = Http("http://localhost:7070/hello/ali").asString

    result.body shouldBe "HELLO ali"
  }

  test("query parameter should be retrieve properly") {
    anAppWith {
      _.addGetHandler("/hello", ctx => ctx.result(s"HELLO ${ctx.queryParam("name").get}"))
    }

    val result = Http("http://localhost:7070/hello?name=ali").asString

    result.body shouldBe "HELLO ali"
  }

  test("form parameter should be retrieve properly") {
    anAppWith {
      _.addPostHandler("/hello", ctx => ctx.result(s"HELLO ${ctx.formParam("name").get}"))
    }

    val result = Http("http://localhost:7070/hello").postForm(Seq("name" -> "ali")).asString

    result.body shouldBe "HELLO ali"
  }

  test("header parameter should be retrieve properly") {
    anAppWith {
      _.addGetHandler("/hello", ctx => ctx.result(s"HELLO ${ctx.header("name").get}"))
    }

    val result = Http("http://localhost:7070/hello").header("name", "ali").asString

    result.body shouldBe "HELLO ali"
  }

  test("file parameter should be retrieve by name properly") {
    anAppWith {
      _.addPostHandler("/hello",
        ctx => ctx.result(s"HELLO ${ctx.uploadedFile("test").map(f => new String(f.content.readAllBytes)).get}"))
    }

    val result = Http("http://localhost:7070/hello").postMulti(
      MultiPart("test", "test.txt", "text/plain", "ali".getBytes))
      .asString

    result.body shouldBe "HELLO ali"
  }

  test("file parameter should be retrieve by index properly") {
    anAppWith {
      _.addPostHandler("/hello",
        ctx => ctx.result(s"HELLO ${ctx.uploadedFile(index = 1).map(f => new String(f.content.readAllBytes)).get}"))
    }

    val result = Http("http://localhost:7070/hello").postMulti(
      MultiPart("test1", "test1.txt", "text/plain", "ali".getBytes),
      MultiPart("test2", "test2.txt", "text/plain", "reza".getBytes))
      .asString

    result.body shouldBe "HELLO reza"
  }

  override protected def afterEach(): Unit = {
    Option(app).foreach(_.stop())
  }

  private def anAppWith(setup: ApplicationBuilder => ?): Unit = {
    val builder = ApplicationBuilder.create().logging(false)
    setup.apply(builder)
    app = builder.build().start(7070)
  }
