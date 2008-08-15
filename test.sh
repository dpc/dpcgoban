#!/bin/sh

[ -f pre-build.sh ] && source pre-build.sh
/opt/wtk/bin/emulator -Xdevice:DefaultColorPhone -Xheapsize:1M -Xdescriptor:/home/dpc/lab/dpcgoban-git/dpcgoban.jad &
/opt/wtk/bin/emulator -Xdevice:DefaultColorPhone -Xheapsize:1M -Xdescriptor:/home/dpc/lab/dpcgoban-git/dpcgoban.jad &
/opt/wtk/bin/emulator -Xdevice:DefaultColorPhone -Xheapsize:1M -Xdescriptor:/home/dpc/lab/dpcgoban-git/dpcgoban.jad

