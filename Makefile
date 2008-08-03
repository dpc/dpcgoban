all:
	[ -f pre-build.sh ] && source pre-build.sh; ant

doc:
	doxygen Doxyfile
