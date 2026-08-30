/*
 * VENDORED, NOT WRITTEN HERE.
 *
 * Verbatim copy of org/springframework/mock/staticmock/MockStaticEntityMethods.java from
 * org.springframework:spring-aspects:4.3.30.RELEASE (sources jar), Apache License 2.0.
 *
 * Why it is here: this package - Spring Roo's record/playback static mocking for JPA
 * @Entity finder methods - was deprecated in Spring 4.3 and deleted in Spring 5. Nine
 * test classes in src/test (DirectionTest, LandscapeTest, ParticipantTest,
 * ParticipantConfigTest, ExternalAppTest, HospitalEntryTest, IdentityMappingTest,
 * RepositoryMappingTest, RepositoryMappingDirectionTest) are written against it and
 * cannot compile without it. Spring offers no successor.
 *
 * The ONLY change from the original is the namespace of the JPA @Entity annotation the
 * pointcut matches on: javax.persistence.Entity -> jakarta.persistence.Entity. Without
 * that, the aspect compiles but matches nothing, because the entities it has to advise
 * are jakarta-annotated after this migration.
 *
 * Compiled as a main source root (see build-helper-maven-plugin in pom.xml) rather than a
 * test one, because its around() advice attaches to the *execution* of the entities'
 * static finder methods, which live in src/main. This mirrors the baseline, where the
 * same aspect arrived on ajc's aspect path from the compile-scoped spring-aspects jar.
 */
/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mock.staticmock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate a test class for whose {@code @Test} methods
 * static methods on JPA-annotated {@code @Entity} classes should be mocked.
 *
 * <p>See {@link AnnotationDrivenStaticEntityMockingControl} for details.
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @deprecated as of Spring 4.3, in favor of a custom aspect for such purposes
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MockStaticEntityMethods {

}
