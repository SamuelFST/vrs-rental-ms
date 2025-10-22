#!/bin/bash

NETWORK_NAME="vrs-network"

if [ -z "$(docker network ls -q --filter name=${NETWORK_NAME})" ]; then
  echo "Network '${NETWORK_NAME}' not found. Creating..."
  docker network create ${NETWORK_NAME}
else
  echo "Network '${NETWORK_NAME}' already exists. No action required."
fi
