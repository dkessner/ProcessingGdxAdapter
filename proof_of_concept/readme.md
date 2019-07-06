This project is a proof-of-concept implementation of the Processing API on top
of libgdx.


## Run desktop application

```
$ gradlew desktop:run
```


## Run web app 

```
$ gradlew html:clean
$ gradlew html:build
$ gradlew html:superDev
```

Web app served here:
[http://localhost:8080/index.html](http://localhost:8080/index.html)


## Notes

* `ANDROID_HOME` environment variable will override settings in gradle project
  for Android sdk location

* problem: html project builds, but superDev doesn't seem to serve pages
    * can open `html/build/gwt/draftOut/index.html` locally in firefox
    * solved: must specify full filename `http://localhost:8080/index.html`

* error: `libgdx java.lang.NoSuchMethodError: com.google.gwt.util.regexfilter.RegexFilter`
    * fix: 'html/build.gradle': gwtVersion 2.8.2 -> 2.8.0

