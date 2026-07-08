#!/bin/sh

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)

if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
elif command -v java >/dev/null 2>&1; then
  JAVA_EXE=java
else
  echo "ERROR: Java was not found. Set JAVA_HOME before running Gradle." >&2
  exit 1
fi

exec "$JAVA_EXE" "-Xmx64m" "-Xms64m" ${JAVA_OPTS:-} ${GRADLE_OPTS:-} \
  -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
  org.gradle.wrapper.GradleWrapperMain "$@"
