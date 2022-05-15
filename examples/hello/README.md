
1. Build CodeRanking Pro. From root directory run: ./gradlew assemble

2. Build Hello example. From examples/hello run: ./gradlew assemble

3. Unzip archive built as result of step 1

4. Run something like:

JAVA_OPTS="-Dconfig.file=config-samples/static.properties" ./CodeRanking-0.0.1/bin/CodeRanking


