.SUFFIXES: .java .class

.java.class:
	javac $<

all: Main.class

Activity.class: TimeInterval.class ActivityType.class

Main.class: Activity.class

