@ECHO OFF
:Loop
IF "%1"=="" GOTO Continue
	extract_features_32bit.exe -haraff -sift -i %1 -DE
SHIFT
GOTO Loop
:Continue
