sh gradlew build          && \
mkdir -p build/dependency && \
cd build/dependency       && \
jar -xf ../libs/*.jar     && \
docker build -t offer-service .