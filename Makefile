SHELL:=/bin/bash -O globstar
.PHONY: all backup clean_copies
.SUFFIXES: .java .class

.java.class:
	javac -Xlint:unchecked -Werror $<

all_with_backup: all backup

dir=${PWD}
backup: all clean_copies
	tar zcf ${dir}.tar.gz ${dir}

clean_copies:
	rm -f *~
	rm -f .*.sw*

all: Main.class

Activity.class: TimeInterval.class ActivityType.class

Main.class: Activity.class InteractiveMessage.class
