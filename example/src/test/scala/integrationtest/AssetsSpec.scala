package integrationtest

import skinny.controller._
import skinny.test.{ SkinnyFlatSpec, SkinnyTestSupport }

class AssetsSpec extends SkinnyFlatSpec with SkinnyTestSupport {

  addFilter(AssetsController, "/*")

  it should "show react jsx template resources" in {
    get("/assets/js/hello-react.js") {
      status should equal(200)
      header("Content-Type") should equal("application/javascript; charset=UTF-8")
      header("X-Content-Type-Options") should equal("nosniff")
      header("X-XSS-Protection") should equal("1; mode=block")

      body.replaceFirst("\n$", "") should equal(
        """/** @jsx React.DOM */
         |React.renderComponent(
         |  React.createElement("h1", null, "Hello, world!"),
         |  document.getElementById('example')
         |);
         |""".stripMargin)
    }
  }

  it should "return 304 for react jsx if If-Modified-Since specified" in {
    get(uri = "/assets/js/hello-react.js", headers = Map("If-Modified-Since" -> "Thu, 31 Dec 2037 12:34:56 GMT")) {
      status should equal(304)
    }
  }

  it should "show coffee script resources" in {
    get("/assets/js/echo.js") {
      status should equal(200)
      header("Content-Type") should equal("application/javascript; charset=UTF-8")
      body.replaceFirst("^// Generated by CoffeeScript .+\n", "").replaceFirst("\n$", "") should equal("""(function() {
  var echo;

  echo = function(v) {
    return console.log(v);
  };

  echo("foo");

}).call(this);""")
    }
  }
  it should "return 304 for coffee if If-Modified-Since specified" in {
    get(uri = "/assets/js/echo.js", headers = Map("If-Modified-Since" -> "Thu, 31 Dec 2037 12:34:56 GMT")) {
      status should equal(304)
    }
  }

  it should "show less resources" in {
    get("/assets/css/box.css") {
      status should equal(200)
      header("Content-Type") should equal("text/css; charset=UTF-8")
      body should equal(""".box {
  color: #fe33ac;
  border-color: #fdcdea;
}
""")
    }
  }
  it should "return 304 for less if If-Modified-Since specified" in {
    get(uri = "/assets/css/box.css", headers = Map("If-Modified-Since" -> "Thu, 31 Dec 2037 12:34:56 GMT")) {
      status should equal(304)
    }
  }

  it should "show scss resources" in {
    get("/assets/css/variables-in-scss.css") {
      status should equal(200)
      header("Content-Type") should equal("text/css; charset=UTF-8")
      body.replaceFirst("\n$", "") should equal("""body {
        |  font: 100% Helvetica, sans-serif; }""".stripMargin)
    }
  }
  it should "return 304 for scss if If-Modified-Since specified" in {
    get(uri = "/assets/css/variables-in-scss.css", headers = Map("If-Modified-Since" -> "Thu, 31 Dec 2037 12:34:56 GMT")) {
      status should equal(304)
    }
  }

  it should "show sass resources" in {
    get("/assets/css/indented-sass.css") {
      status should equal(200)
      header("Content-Type") should equal("text/css; charset=UTF-8")
      body.replaceFirst("\n$", "") should equal("""#main {
        |  color: blue;
        |  font-size: 0.3em; }""".stripMargin)
    }
  }
  it should "return 304 for sass if If-Modified-Since specified" in {
    get(uri = "/assets/css/indented-sass.css", headers = Map("If-Modified-Since" -> "Thu, 31 Dec 2037 12:34:56 GMT")) {
      status should equal(304)
    }
  }

  it should "return assets in sub directories" in {
    get("/assets/js/vendor/awesome.js") {
      status should equal(200)
    }
    get("/assets/js/vendor/lgtm.js") {
      status should equal(200)
    }
    get("/assets/css/vendor/awesome.css") {
      status should equal(200)
    }
    get("/assets/css/vendor/lgtm.css") {
      status should equal(200)
    }
  }

}
