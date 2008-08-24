#!/bin/sh

[ -f pre-build.sh ] && source pre-build.sh
aoss32 /opt/wtk/bin/emulator -Xdevice:QwertyDevice -Xheapsize:1M -Xdescriptor:/home/dpc/lab/dpcgoban-git/dpcgoban.jad &
aoss32 /opt/wtk/bin/emulator -Xdevice:DefaultColorPhone -Xheapsize:1M -Xdescriptor:/home/dpc/lab/dpcgoban-git/dpcgoban.jad &
aoss32 /opt/wtk/bin/emulator -Xdevice:DefaultGrayPhone -Xheapsize:1M -Xdescriptor:/home/dpc/lab/dpcgoban-git/dpcgoban.jad

