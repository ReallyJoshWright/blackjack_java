JAVAC = javac
JFLAGS = -g -Xlint

SOURCES = $(wildcard *.java)
CLASSES = $(SOURCES:.java=.class)
TARGET = blackjack

all: $(CLASSES) $(TARGET)

%.class: %.java
	$(JAVAC) $(JFLAGS) $<
$(TARGET):
	@echo -e "#!/bin/sh" > temp
	@echo "java Blackjack" >> temp
	install -m 755 temp blackjack
	rm -f temp
.PHONY: clean
clean:
	rm -f $(CLASSES) $(TARGET)
