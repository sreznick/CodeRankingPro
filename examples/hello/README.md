
1. Build CodeRanking Pro. From root directory run: ./gradlew assemble

2. Build Hello example. From examples/hello run: ./gradlew assemble

3. Unzip archive built as result of step 1

4. Run something like:

bash CodeRanking-0.0.1/bin/CodeRanking  examples/hello/workspace/hello/lib/build/libs/lib.jar build/libs/CodeRanking-0.0.1.jar coderank.impl.javagraph.Graph dummy  static examples/hello/ranking.properties 

