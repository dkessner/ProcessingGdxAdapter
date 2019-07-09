# ProcessingGdx

This is the re-implementation of the Processing core classes on top of
libgdx, as a Java library project.

## Build the jar

```
gradlew build
```

## Usage

After creating a new libgdx project, you need to do the following to use the
ProcessingGdx library.

### add ProcessingGdx as dependency of the :core project

_<project_name>_`/build.gradle`:

```
project(":core") {
    dependencies {
        implementation files('../../ProcessingGdx/build/libs/ProcessingGdx-0.1.0.jar')
    }
}

```

### add ProcessingGdx as a dependency of the :html project

```
project(":html") {
    dependencies {
        implementation files('../../ProcessingGdx/build/libs/ProcessingGdx-0.1.0.jar')
    }
}
```

### tell GWT compiler where the ProcessingGdx source is
 
_<project_name>_`/html/src/io/github/dkessner/GdxDefinition.gwt.xml`:
```
        <inherits name='ProcessingGdx' />
        <source path="processing/core" />
```



## Notes 

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

