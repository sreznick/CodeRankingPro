
1. Build CodeRanking Pro. From root directory run: ```./gradlew assemble```

2. Build Hello example. From examples/hello-kotlin run: ```./gradlew assemble```

3. Unzip archive built as result of step 1

4. Run something like:

```JAVA_OPTS="-Dconfig.file=config-samples/kotlin-example.properties" ./CodeRanking-0.0.1/bin/CodeRanking```

or

```bash CodeRanking-0.0.1/bin/CodeRanking  examples/hello-kotlin/workspace/hello-kotlin/build/libs/hello-kotlin-1.0-SNAPSHOT.jar build/libs/CodeRanking-0.0.1.jar coderank.impl.kotlingraph.KotlinGraphBuilder dummy  static examples/hello-kotlin/ranking.properties```
