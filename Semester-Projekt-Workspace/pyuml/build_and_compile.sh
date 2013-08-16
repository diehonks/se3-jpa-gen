#!/bin/bash
python3 pyuml_main.py
javac src_gen/*/*/*/*/*.java
diff -y src_gen/src/main/persistence/entities/ ../../"JPA Example Project"/src/persistence/entities/
