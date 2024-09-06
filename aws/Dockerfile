FROM python:3.13.0rc1-alpine3.20

ARG CLI_VERSION=1.16.241

RUN apk -uv add --no-cache groff jq less && \
    pip install --no-cache-dir awscli==$CLI_VERSION

WORKDIR /aws

CMD sh

