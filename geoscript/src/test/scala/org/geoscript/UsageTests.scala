package org.geoscript

import org.opengis.feature.simple.SimpleFeature

import org.specs._

import geometry._
import layer._
import projection._

class UsageTests extends Specification with GeoScript {
  "geometries" should { 
    "work like on the geoscript homepage" in { 
      var p = Point(-111, 45.7)
      var p2 = Projection("epsg:4326") to Projection("epsg:26912") apply p
      var poly = p.buffer(100)

      p2.x must beCloseTo(499999.0, 0.5)
      p2.y must beCloseTo(5060716.0, 0.5)
      poly.area must beCloseTo(31214.45, 0.01)
    }

    "linestrings should be easy" in { 
      LineString((10.0, 10.0), (20.0, 20.0), (30.0, 40.0)) 
    }

    "polygon should be easy" in { 
      Polygon(
        Seq((10d, 10d),(10d, 20d),(20d, 20d), (20d, 15d), (10d, 10d)),
        Seq.empty
      )
    }

    "multi point should be easy" in {
      MultiPoint(Seq((20.0,20.0),(10.0,10.0))) 
    } 
  }  

  "Layers" should {
    val statesPath = "geoscript/src/test/resources/data/states.shp"
    "be able to read shapefiles" in {
      val shp = Shapefile(statesPath)
      val (xMin, yMin, xMax, yMax, proj) = shp.bounds

      shp.name must_== "states"
      shp.count must_== 49

      xMin must beCloseTo (-124.731422, 1d)
      yMin must beCloseTo (24.955967, 1d)
      xMax must beCloseTo (-66.969849, 1d)
      yMax must beCloseTo (49.371735, 1d)
      proj must_== "EPSG:4326"
    }

    "support search" in {
      val shp = Shapefile(statesPath)
      shp.features.find(_.getID == "states.1") must beSome[SimpleFeature]
    }

    "provide access to schema information" in {
      val shp = Shapefile(statesPath)
      shp.schema.name must_== "states"
      val field = shp.schema("STATE_NAME")
      field.name must_== "STATE_NAME"
      // the type of field.binding is hard to spell,
      // just compare toString's for now
      field.binding.getName must_== classOf[java.lang.String].getName
    }

    "provide access to the containing workspace" in {
      val shp = Shapefile(statesPath)
      shp.workspace must haveSuperClass[workspace.Workspace]
    }
  }
}