#!/bin/bash

if [ $1 ]; then

	if [ -z "$2" ]; then TRANSFORMER="../scxml-to-dot.xsl"; else TRANSFORMER="$2"; fi

	echo "Transforming SCXML..."
	xsltproc "$TRANSFORMER" "$1" > output.dot

	echo "Dumping DOT file..."
	cat output.dot

	echo "Generating image..."
	cat output.dot | dot -Tpng > test.png

	echo "Done."
else
	echo "Please supply .scxml file path."
fi
