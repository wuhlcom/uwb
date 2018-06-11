##Polygon


###Build Polygon

This is the simple version of the polygon. You can build both types of polygons: concave and non-convex
```
Polygon polygon = Polygon.Builder()
        .addVertex(new Point(1, 3))
        .addVertex(new Point(2, 8))
        .addVertex(new Point(5, 4))
        .addVertex(new Point(5, 9))
        .addVertex(new Point(7, 5))
        .addVertex(new Point(6, 1))
        .addVertex(new Point(3, 1))
        .build();
```
###Build Polygon with Holes

First build all the border sides of the polygon. Close the borders and then you can start adding holes into the polygon. You can add multiple number of holes, just remember to close() after each added hole.
```
Polygon polygon = Polygon.Builder()
        .addVertex(new Point(1, 2)) // polygon
        .addVertex(new Point(1, 6))
        .addVertex(new Point(8, 7))
        .addVertex(new Point(8, 1))
        .close() 
        .addVertex(new Point(2, 3)) // hole one
        .addVertex(new Point(5, 5))
        .addVertex(new Point(6, 2))
        .close() 
        .addVertex(new Point(6, 6)) // hole two
        .addVertex(new Point(7, 6))
        .addVertex(new Point(7, 5))
        .build();
```
 ###Check if the point inside
 ```
 point = new Point(4.5f, 7);
 boolean contains = polygon.contains(point);
 ```