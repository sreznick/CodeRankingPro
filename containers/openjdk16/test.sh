apt-get update
apt-get install -y git unzip
git clone https://github.com/sreznick/CodeRankingPro/
cd CodeRankingPro/examples/helloapp/workspace/helloapp/
./gradlew build
cd ../../../../
./gradlew build
cd ./build/distributions
unzip CodeRanking-0.0.1.zip
cd ../..

echo "going to run instrumentor..."


JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED" ./build/distributions/CodeRanking-0.0.1/bin/CodeRanking examples/helloapp/workspace/helloapp/app/build/libs/app.jar build/distributions/CodeRanking-0.0.1/lib/CodeRanking-0.0.1.jar coderank.impl.javagraph.Graph examples/helloapp/workspace/helloapp/app/build/libs/app.jar instrument_dynamic examples/hello/ranking.properties --add-opens java.base/java.lang=ALL-UNNAMED && echo OK



