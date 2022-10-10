#!/bin/bash

SCRIPT_DIR=`dirname $0`

echo $SCRIPT_DIR
JARNAME=$SCRIPT_DIR/hev.jar
MAIN=cnuphys.hev.frame.Hev
VARGS="-Dsun.java2d.pmoffscreen=false -Xmx1024M -Xss512k"
java $VARGS -jar $JARNAME $MAIN
