#!/bin/bash
# Install everything the build needs that is not on Maven Central.
#
# Adds three things:
#   * repo-parent.pom, the declared parent of all eight vendored POMs. Without it every
#     vendored artifact is unreadable. (It is present in mkrepo.sh, commented out.)
#   * the reconstructed Spring Roo annotations, whose original artifact no longer exists.
#   * spring-mock-staticmock, the org.springframework.mock.staticmock package carved out of
#     spring-aspects 3.2.18.RELEASE. See the comment on that step for why.
set -euo pipefail
cd "$(dirname "$0")/.."
: "${JAVA_HOME:?set JAVA_HOME to a JDK - 25 after the migration, 8 for the baseline}"
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

# ---------------------------------------------------------------------------------------
# spring-mock-staticmock
#
# Nine Roo-generated entity tests (DirectionTest, LandscapeTest, ...) are annotated
# @MockStaticEntityMethods and drive AnnotationDrivenStaticEntityMockingControl. Both live in
# org.springframework.mock.staticmock, which shipped inside spring-aspects up to 3.2 and was
# deleted in Spring 4.0 with no successor anywhere - Roo 2 dropped the feature and no other
# artifact publishes it.
#
# So the package is carved out of the last release that has it, spring-aspects
# 3.2.18.RELEASE, and installed under its own coordinates. Only two of its five classes are
# taken: AbstractMethodMockingControl (the abstract aspect that holds all the record/playback
# logic) and the @MockStaticEntityMethods annotation. The concrete subaspect,
# AnnotationDrivenStaticEntityMockingControl, is deliberately left out and supplied from
# source in src/test/java instead, because its pointcut names @javax.persistence.Entity and
# the entities are now annotated @jakarta.persistence.Entity - which is precisely the
# migration this project is doing.
#
# The only Spring API the extracted classes touch is ObjectUtils.nullSafeToString(Object[]),
# which still exists in Spring 6.
# ---------------------------------------------------------------------------------------
echo "==> spring-mock-staticmock (carved out of spring-aspects 3.2.18.RELEASE)"
SM_DIR=target/legacy/spring-mock-staticmock
SM_JAR="$PWD/target/legacy/spring-mock-staticmock-3.2.18.RELEASE.jar"
rm -rf "$SM_DIR"
mkdir -p "$SM_DIR"
mvn -B -q $REPO_ARG dependency:copy \
    -Dartifact=org.springframework:spring-aspects:3.2.18.RELEASE \
    -DoutputDirectory="$SM_DIR" -Dmdep.stripVersion=true
(cd "$SM_DIR" \
  && "$JAVA_HOME/bin/jar" xf spring-aspects.jar \
        org/springframework/mock/staticmock/AbstractMethodMockingControl.class \
        'org/springframework/mock/staticmock/AbstractMethodMockingControl$Expectations.class' \
        'org/springframework/mock/staticmock/AbstractMethodMockingControl$Expectations$Call.class' \
        'org/springframework/mock/staticmock/AbstractMethodMockingControl$CallResponse.class' \
        org/springframework/mock/staticmock/MockStaticEntityMethods.class \
  && rm spring-aspects.jar \
  && "$JAVA_HOME/bin/jar" cf "$SM_JAR" org)
mvn -B -q $REPO_ARG install:install-file -Dfile="$SM_JAR" \
    -DgroupId=com.collabnet.ccf.ccfmaster.legacy -DartifactId=spring-mock-staticmock \
    -Dversion=3.2.18.RELEASE -Dpackaging=jar

echo "==> done; 'mvn test' should now run"
