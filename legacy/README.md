# Making the 2011-2014 CCFMaster build again

Nothing here is modernization. This is the minimum needed to get the recovered project to
compile and run its tests at all, so that a modernization can be measured against a known
starting point. Every change is listed below with its reason.

The baseline is **JDK 8**, deliberately. `javax.xml.bind` was removed from the JDK in Java 11
(JEP 320), and supplying it as a dependency is *part of the migration being measured*, so the
baseline must be a JDK where it is still present.

## What blocked the build

### 1. Five of the eight declared repositories are dead

| Repository | Status |
|---|---|
| `spring-roo-repository.springsource.org` | dead |
| `maven.springframework.org/release` | dead |
| `maven.springframework.org/milestone` | dead |
| `opensource.kantega.no` | dead |
| `ccf.open.collab.net/mvnrepo` | dead |
| `repository.jboss.org` | alive |
| `download.java.net/maven/2` | alive (redirects) |
| `repository.apache.org` | alive |

Spring 3.0.5, Spring Security 3.0.2 and AspectJ are all on Maven Central, so only one
artifact was actually lost with those hosts: see below.

### 2. `org.springframework.roo.annotations:1.1.3.RELEASE` no longer exists

Its repository is dead and Maven Central carries only `2.0.0.RELEASE`. It is not vendored in
`extlib/`.

`legacy/roo-annotations/` reconstructs it. The sources use exactly seven annotations with four
attributes between them (`entity`, `path`, `formBackingObject`, `finders`), all
`@Retention(SOURCE)` and read only by the Roo tooling that generated the `.aj` ITDs - and
every one of those ITDs is checked in. The stub is therefore sufficient to compile the
original unchanged, and the 125 ITDs in `src/main/java` reference the annotations zero times.

### 3. `extlib/install.sh` never installs `repo-parent.pom`

All eight vendored POMs in `extlib/` declare `com.collabnet.ccf:repo-parent` as their parent,
but `install.sh` installs only the eight jars. Installing `repo-parent.pom` is present in
`mkrepo.sh` - commented out. So running CCF's own bootstrap leaves every vendored artifact
unreadable. `legacy/bootstrap.sh` installs it as well.

### 4. `aspectj-maven-plugin` 1.0 silently does nothing on Maven 3.9

It is a Maven 2-era plugin. It runs, logs two warnings about `compileSourceRoots`, and
produces zero class files, after which `maven-compiler-plugin` fails on the aspect types it
cannot see.

There is no combination of the original versions that works, because the constraints conflict:

- AspectJ 1.9.x, which supports modern JDKs, **requires a Java 17+ runtime** to run `ajc`.
- `javax.xml.bind` **requires Java 10 or older** to be present in the JDK.
- AspectJ 1.6.10 with plugin 1.0 **no-ops on Maven 3.9**.

The resolution is `org.codehaus.mojo:aspectj-maven-plugin:1.14.0`, which pairs with AspectJ
1.9.7 - the last line that still runs on a Java 8 runtime. It also needs
`<complianceLevel>8</complianceLevel>`, since the plugin defaults to 1.4 and rejects source 8.

`maven-compiler-plugin`'s `default-compile` and `default-testCompile` are bound to phase
`none`: `ajc` compiles the `.java` sources as well as the `.aj` ITDs, and `javac` cannot see
aspect types such as `ConfigSavingAspect` (which is a `.aj` file imported as a Java type by
`PersistableConfigItem`).

### 5. HEAD has never compiled against its own vendored TeamForge SDK

Three call sites use SDK members that do not exist in **any** `extlib/tfapi.jar` ever
committed - all four historical versions stop at `supports63()` and expose no icon methods:

- `Connection.supports65()` - `TFLandscapeCreationListenerFactory:34`, `IconUploader:40`
- `IntegratedApplicationClient.setIntegratedApplicationIcon(String,String,String,String)` -
  `IconUploader:82`

The TeamForge SDK was never published to a public repository, so there is nothing to upgrade
to. Excluding the two files is not possible either - `CreateIntegratedAppStrategy` and a
Spring context both reference them.

`com.collabnet.ccf.compat.TeamForgeCompat` calls both members reflectively. This is faithful
to the original intent rather than a workaround: `supports65()` was a **feature gate**, so on
an older SDK it is legitimately absent and the answer is `false`. The icon upload is reached
only from inside that gate, so with the shipped SDK it is unreachable, and the reflective
version throws a clear `UnsupportedOperationException` if it ever is reached.

## Baseline test results

`mvn test` on JDK 8: **486 tests, 472 passing**, 5 failures, 7 errors, 2 skipped.

None are caused by the changes above - `TFLandscapeCreationListenerFactoryTest`, which covers
the only behavioural change, is 4/4 green. The 12 are pre-existing:

| Count | Test | Cause |
|---|---|---|
| 5 | `StartCoresOnBootBeanTest` | JMockit; version-sensitive to the JDK |
| 3 | `ConversionResultTest` | XSLT output differences between JDK 6 and 8 |
| 2 | `CcfCoreStatusAPIIntegrationTest` | JMockit (`ExceptionInInitializerError`) |
| 1 | `CreateIntegratedAppStrategyTest` | NPE inside the vendored SDK's own constructor; the test was written against a different SDK build |
| 1 | `TimezoneTest.allTimezonesFromString` | **IANA tz database drift.** Asserts every timezone in a hardcoded list round-trips. `Asia/Riyadh87` was a solar-time zone removed from the tzdb; the JDK no longer knows it, verified directly |

`TimezoneTest` is the interesting one, and the direct analogue of the `Locale.CHINA` failure
in the KiGa 3000 project: not a code defect, but a hardcoded assumption about data that the
platform has since changed underneath it.

## Reproducing

```bash
./legacy/bootstrap.sh              # installs extlib + repo-parent + the Roo stub
export JAVA_HOME=/path/to/jdk8
mvn test
```
