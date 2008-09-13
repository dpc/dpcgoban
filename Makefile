all:
	[ -f pre-build.sh ] && source pre-build.sh; ant -q -emacs 2> /dev/null

doc:
	doxygen Doxyfile
