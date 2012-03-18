name := "geocss"

libraryDependencies <++= gtVersion { v =>
  Seq(
    "org.geotools" % "gt-main" % v,
    "org.geotools" % "gt-cql" % v
  )
}

libraryDependencies +=
  "org.specs2" %% "specs2" % "1.7.1" % "test"

initialCommands += """
import org.geoscript.geocss._
import collection.JavaConversions._
val kb = dwins.logic.Knowledge.Oblivion(SelectorsAreSentential)
def in(path: String) = new java.io.FileReader(new java.io.File(path))
def load(path: String) = CssParser.parseAll(CssParser.styleSheet, in(path)).get
val tx = new Translator()
"""
