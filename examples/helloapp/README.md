
1. Build CodeRanking Pro. From root directory run: ./gradlew assemble

2. Build Hello example. From workspace/helloapp run: ./gradlew assemble

3. Unzip archive built as result of step 1

4. For static check run something like:

bash CodeRanking-0.0.1/bin/CodeRanking  examples/helloapp/workspace/helloapp/lib/build/libs/app.jar build/libs/CodeRanking-0.0.1.jar coderank.impl.javagraph.Graph dummy  static examples/helloapp/ranking.properties 
