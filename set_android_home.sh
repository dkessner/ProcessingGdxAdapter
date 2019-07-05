#!/bin/bash
#
# set_android_home.sh
#
# Darren Kessner
#

if [ "$(uname)" = "Darwin" ]
then
    export ANDROID_HOME="$HOME/Library/Android/sdk"
elif [ $(uname) = "Linux" ]
then
    export ANDROID_HOME="$HOME/Android/Sdk"
else
    echo Unknown system
fi

echo uname: $(uname)
echo ANDROID_HOME: $ANDROID_HOME

