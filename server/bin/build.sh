#!/bin/bash -e

# get the commit id for the latest commit in HEAD
DOCKER_IMAGE_TAG="$(git rev-parse --short HEAD)"
DOCKER_BUILDKIT=1 docker build -t registry.mobilejazz.com/colloc/server .
docker tag registry.mobilejazz.com/colloc/server registry.mobilejazz.com/colloc/server:"$DOCKER_IMAGE_TAG"
