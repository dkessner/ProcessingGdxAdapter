# Bounce

This is the Bounce program, implemented as a libgdx project depending on the
ProcessingGdx library.

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

