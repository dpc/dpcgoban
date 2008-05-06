# Makefile for Markdown + Make demonstration

.SUFFIXES: .text .html

SRCS := *.text
DSTS := ${SRCS:.text=.text.html}

.PHONY: all

all: ${DSTS}

${DSTS}: %.html: % Makefile
	md2html $<
