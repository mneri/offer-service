#!/bin/bash

chmod +x gradlew                && \
sh gradlew build                && \
mkdir -p build/dependency       && \
cd build/dependency             && \
jar -xf ../libs/*.jar           && \
cd ../..                        && \
docker build -t offer-service . ;