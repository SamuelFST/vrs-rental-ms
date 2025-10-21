#!/bin/bash

check_service() {
    echo "Verifying if Localstack S3 is ready..."
    local url="http://localhost:4566/health"
    local max_attempts=30
    local attempt=0

    while [ $attempt -lt $max_attempts ]; do
        if curl --fail --silent $url; then
            echo "LocalStack is ready"
            return 0
        fi
        echo "Waiting localstack S3 startup... Attempt $((attempt + 1)) of $max_attempts"
        sleep 2
        attempt=$((attempt + 1))
    done

    echo "Error: LocalStack S3 is not responding."
    return 1
}

check_service || exit 1

echo "Starting S3 Bucket Setup"

awslocal s3 mb s3://rentals-bucket --endpoint-url http://localhost:4566

echo "S3 Bucket Setup Complete."