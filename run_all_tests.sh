#!/usr/bin/env bash

DEFAULT_BROWSER=chrome
BROWSER_TYPE=$1
ENV=$2

if [ -z "$BROWSER_TYPE" ]; then
    echo "BROWSER_TYPE value not set, defaulting to $DEFAULT_BROWSER..."
    echo ""
fi

if [ "$NO_LINT" != "true" ];
then
  echo "Linting"
  sbt scalafmtAll
fi

sbt clean -Dbrowser="${BROWSER_TYPE:=$DEFAULT_BROWSER}" -Denvironment="${ENV:=local}" "test" -Dbrowser.usePreviousVersion=true testReport
