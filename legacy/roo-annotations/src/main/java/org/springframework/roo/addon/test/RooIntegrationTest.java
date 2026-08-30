// Reconstructed stub. See RECONSTRUCTION.md - the real artifact
// org.springframework.roo:org.springframework.roo.annotations:1.1.3.RELEASE is
// unobtainable: spring-roo-repository.springsource.org is dead and Maven Central
// carries only 2.0.0.RELEASE.
package org.springframework.roo.addon.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooIntegrationTest {
    Class<?> entity();
}
