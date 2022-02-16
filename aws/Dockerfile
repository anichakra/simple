FROM python:3.11.0a5-alpine3.15

ARG CLI_VERSION=1.16.241

RUN apk -uv add --no-cache groff jq less && \
    pip install --no-cache-dir awscli==$CLI_VERSION

WORKDIR /aws

CMD sh

