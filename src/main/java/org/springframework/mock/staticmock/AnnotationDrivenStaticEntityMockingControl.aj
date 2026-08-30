/*
 * Reconstruction of the concrete sub-aspect that shipped in spring-aspects up to 3.2 and was
 * deleted in Spring 4.0. It is here rather than in the jar carved out of spring-aspects
 * 3.2.18.RELEASE (see legacy/bootstrap.sh) for exactly one reason: the original
 * methodToMock() pointcut names @javax.persistence.Entity, and the entities in this project
 * are now annotated @jakarta.persistence.Entity, so the compiled 2016 aspect no longer
 * matches anything. A .class file's pointcut cannot be retargeted without rewriting its
 * AspectJ attributes, so the aspect is recompiled from source with the jakarta annotation.
 *
 * Everything else is as it was: the three static entry points delegate to the *Internal
 * methods on AbstractMethodMockingControl, which is taken unmodified from the 3.2.18 jar and
 * holds all of the record/playback logic. Nine Roo-generated entity tests
 * (DirectionTest, LandscapeTest, ParticipantTest, ...) depend on this and are green again
 * with it in place.
 *
 * It sits under src/main rather than src/test on purpose. methodToMock() is an
 * execution() pointcut on the entities' static methods, so the around advice has to be
 * woven into the entity classes themselves, which happens during the main ajc compile - a
 * test-compile run only weaves test classes. At the baseline spring-aspects 3.0.5 was an
 * ordinary compile-scope aspectLibrary and did exactly this, so the entity classes have
 * always carried this advice.
 *
 * Original: org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl,
 * Spring Framework 3.2.18.RELEASE, Apache License 2.0.
 */
package org.springframework.mock.staticmock;

import jakarta.persistence.Entity;

public aspect AnnotationDrivenStaticEntityMockingControl extends
        AbstractMethodMockingControl {

    public static void expectReturn(Object retVal) {
        AnnotationDrivenStaticEntityMockingControl.aspectOf()
                .expectReturnInternal(retVal);
    }

    public static void expectThrow(Throwable throwable) {
        AnnotationDrivenStaticEntityMockingControl.aspectOf()
                .expectThrowInternal(throwable);
    }

    public static void playback() {
        AnnotationDrivenStaticEntityMockingControl.aspectOf().playbackInternal();
    }

    protected pointcut mockStaticsTestMethod() : execution(public * (@MockStaticEntityMethods *).*(..));

    protected pointcut methodToMock() : execution(public static * (@Entity *).*(..));
}
