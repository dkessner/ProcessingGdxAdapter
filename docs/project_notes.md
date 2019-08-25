# ProcessingGdxAdapter project notes

## Design Summary

ProcessingGdx is a Java library that re-implements key portions of the
Processing API on top of libgdx.

`PConstants:`  unchanged  
`PImage:` __re-implemented__  
`PGraphics:` __re-implemented__   
`PApplet:` __re-implemented__  
`PVector:` unchanged  
`PMatrix:` unchanged  
`PFont:`  _not available yet_  
`PShape:`  _not available yet_   
`PStyle:`  _not available yet_  


## Design Details

The Processing API looks like this to the client. `PGraphics` shares most of
its interface with `PApplet`.

```java
interface PConstants; // LEFT, RIGHT, CENTER, etc.

class PImage implements PConstants;

class PGraphics extends PImage;

class PApplet extends PGraphics; // PGraphics shares most of the PApplet API
```

The current [Processing](https://github.com/processing/processing)
implementation actually implements the relation PApplet -> PGraphics using
composition/delegation rather than inheritance:

```java
interface PConstants; // LEFT, RIGHT, CENTER, etc.

class PImage implements PConstants;

class PGraphics extends PImage;

class PApplet implements PConstants;
```

The ProcessingGdx library uses the first design, describing the shared API
between PApplet and PGraphics using inheritance.


