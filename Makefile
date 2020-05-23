V=0.1-SNAPSHOT

.PHONY: run
run :
	mvn package
	cp ./target/visibly-happy-$(V).jar .
	java -jar visibly-happy-$(V).jar


.PHONY: clean
clean :
	-rm *.jar
	find . -name '*~' -delete
	find . -name '#*' -delete
	mvn clean
