#!/bin/bash
# Install everything the build needs that is not on Maven Central.
#
# Adds two things CCF's own extlib/install.sh omits:
#   * repo-parent.pom, the declared parent of all eight vendored POMs. Without it every
#     vendored artifact is unreadable. (It is present in mkrepo.sh, commented out.)
#   * the reconstructed Spring Roo annotations, whose original artifact no longer exists.
set -euo pipefail
cd "$(dirname "$0")/.."
: "${JAVA_HOME:?set JAVA_HOME to a JDK 8 - see legacy/README.md for why 8}"
REPO_ARG=""
[ -n "${MAVEN_REPO_LOCAL:-}" ] && REPO_ARG="-Dmaven.repo.local=$MAVEN_REPO_LOCAL"

echo "==> repo-parent.pom (omitted by extlib/install.sh)"
mvn -B -q $REPO_ARG install:install-file \
    -Dfile=extlib/repo-parent.pom -DpomFile=extlib/repo-parent.pom

echo "==> the eight vendored extlib artifacts"
cd extlib
for f in sf_soap44_sdk sf_soap50_sdk sf_soap60_sdk tfapi integratedapps jaxen-1.1.2 hsqldb scrumworks_sdk; do
  mvn -B -q $REPO_ARG install:install-file -Dfile="$f.jar" -DpomFile="$f.pom.xml"
  echo "    $f"
done
cd ..

echo "==> reconstructed Spring Roo annotations"
mvn -B -q $REPO_ARG -f legacy/roo-annotations/pom.xml install

echo "==> done; 'mvn test' should now run"
