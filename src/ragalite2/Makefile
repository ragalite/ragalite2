# horrible hack for java make, should be using ant instead
.PHONY: all backup clean_copies
.SUFFIXES: .java .class

CLASSDIR=~/ragalite2/classes/

.java.class:
	javac -Xlint:unchecked -Werror -d ~/ragalite2/classes -cp .. $<


all: ~/ragalite2/classes/ragalite2/Main.class

all_with_backup: all backup

dir=${PWD}
backup: all clean_copies
	tar zcf ${dir}.tar.gz ${dir}

clean_copies:
	rm -f *~
	rm -f .*.sw*

$(CLASSDIR)ragalite2/Activity.class: ~/ragalite2/classes/ragalite2/TimeInterval.class 

$(CLASSDIR)ragalite2/Main.class: ~/ragalite2/classes/ragalite2/Activity.class \
                       ~/ragalite2/classes/ragalite2/InteractiveMessage.class

$(CLASSDIR)ragalite2/%.class : %.java
	javac -Xlint:unchecked -Werror -d $(CLASSDIR) -cp .. $<
