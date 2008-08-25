#!/bin/sh

[ -f pre-build.sh ] && source pre-build.sh
aoss32 /opt/wtk/bin/emulator -Xdevice:QwertyDevice -Xheapsize:4M -Xdescriptor:dpcgoban.jad &
#aoss32 /opt/wtk/bin/emulator -Xdevice:DefaultColorPhone -Xheapsize:4M -Xdescriptor:dpcgoban.jad &
#aoss32 /opt/wtk/bin/emulator -Xdevice:DefaultGrayPhone -Xheapsize:4M -Xdescriptor:dpcgoban.jad

