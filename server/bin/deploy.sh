#!/bin/sh -e
# push the image
docker login --username "$DOCKER_REGISTRY_USER" -p "$DOCKER_REGISTRY_PASSWORD" registry.mobilejazz.com || exit
DOCKER_IMAGE_TAG="$(git rev-parse --short HEAD)"
docker push registry.mobilejazz.com/colloc/server:"$DOCKER_IMAGE_TAG"

# copy docker-compose.yml, set the version in the .env file and docker-compose up
scp docker-compose.prod.yml "$SERVER_USER"@"$SERVER_ADDRESS":docker-compose.yml
ssh "$SERVER_USER"@"$SERVER_ADDRESS" "sh -c 'echo COLLOC_VERSION=${DOCKER_IMAGE_TAG}' > .env && docker-compose up -d --remove-orphans"
