#!/bin/bash
#
# run_gradle_init.sh
#
# Command line used to initialize the gradle java library project
#
# https://guides.gradle.org/building-java-libraries
#

gradle init --type java-library --dsl groovy --test-framework junit --project-name ProcessingGdx --package processing.core

