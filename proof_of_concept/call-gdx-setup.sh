#!/bin/bash
#
# call-gdx-setup.sh
#

source set_android_home.sh
echo Ready to create project.
echo Enter to continue.
read response

java -jar ~/local/gdx-setup.jar --dir . --name Bounce --package io.github.dkessner --mainClass ProcessingGdxAdapter --sdkLocation $ANDROID_HOME

