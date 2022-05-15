
1. Build CodeRanking Pro. From root directory run: ./gradlew assemble

2. Build Hello example. From workspace/helloapp run: ./gradlew assemble

3. Unzip archive built as result of step 1

4. For static analysis run something like:

JAVA_OPTS="-Dconfig.file=config-samples/static.properties" ./CodeRanking-0.0.1/bin/CodeRanking

5. For dynamic analysis first run:

JAVA_OPTS="-Dconfig.file=config-samples/instrument.properties --add-opens java.base/java.lang=ALL-UNNAMED" ./CodeRanking-0.0.1/bin/CodeRanking

then run instrumented application and

JAVA_OPTS="-Dconfig.file=config-asmples/analyze.properties" ./CodeRanking-0.0.1/bin/CodeRanking 


