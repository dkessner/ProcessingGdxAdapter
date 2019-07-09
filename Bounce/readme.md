# Bounce

This is the Bounce program as a project depending on the ProcessingGdx library.

## Notes on html build

Main issue: the GWT compiler needs java source.


### library needs to include source in jar file

`ProcessingGdx/build.gradle`:
```
jar {
    from sourceSets.main.allSource
}
 
```

`ProcessingGdx/src/main/java/ProcessingGdx.gwt.xml`:
```
<module>
    <source path="processing/core" />
</module>
```

### html project needs the path to the source

`Bounce/build.gradle`:
```
project(":html") {
        implementation files('../../ProcessingGdx/build/libs/ProcessingGdx-0.1.0.jar')
     }
```
 
`Bounce/html/src/io/github/dkessner/GdxDefinition.gwt.xml`:
```
        <inherits name='ProcessingGdx' />
        <source path="processing/core" />
```
