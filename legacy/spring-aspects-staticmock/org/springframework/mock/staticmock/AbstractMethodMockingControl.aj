/*
 * VENDORED, NOT WRITTEN HERE.
 *
 * Verbatim copy of org/springframework/mock/staticmock/AbstractMethodMockingControl.aj from
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

import java.util.Arrays;
import java.util.LinkedList;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import org.springframework.util.ObjectUtils;

/**
 * Abstract aspect to enable mocking of methods picked out by a pointcut.
 *
 * <p>Sub-aspects must define:
 * <ul>
 * <li>The {@link #mockStaticsTestMethod()} pointcut to indicate call stacks
 * when mocking should be triggered
 * <li>The {@link #methodToMock()} pointcut to pick out method invocations to mock
 * </ul>
 *
 * @author Rod Johnson
 * @author Ramnivas Laddad
 * @author Sam Brannen
 * @deprecated as of Spring 4.3, in favor of a custom aspect for such purposes
 */
@Deprecated
public abstract aspect AbstractMethodMockingControl percflow(mockStaticsTestMethod()) {

	private final Expectations expectations = new Expectations();

	private boolean recording = true;


	protected void expectReturnInternal(Object retVal) {
		if (!recording) {
			throw new IllegalStateException("Not recording: Cannot set return value");
		}
		expectations.expectReturn(retVal);
	}

	protected void expectThrowInternal(Throwable throwable) {
		if (!recording) {
			throw new IllegalStateException("Not recording: Cannot set throwable value");
		}
		expectations.expectThrow(throwable);
	}

	protected void playbackInternal() {
		recording = false;
	}

	protected void verifyInternal() {
		expectations.verify();
	}

	protected void resetInternal() {
		expectations.reset();
		recording = true;
	}


	private static enum CallResponse {
		undefined, return_, throw_
	};

	/**
	 * Represents a list of expected calls to methods.
	 */
	// Public to allow inserted code to access: is this normal??
	public class Expectations {

		/**
		 * Represents an expected call to a method.
		 */
		private class Call {

			private final String signature;
			private final Object[] args;

			private CallResponse responseType = CallResponse.undefined;
			private Object responseObject; // return value or throwable


			Call(String signature, Object[] args) {
				this.signature = signature;
				this.args = args;
			}

			boolean responseTypeAlreadySet() {
				return responseType != CallResponse.undefined;
			}

			void setReturnValue(Object retVal) {
				this.responseObject = retVal;
				responseType = CallResponse.return_;
			}

			void setThrowable(Throwable throwable) {
				this.responseObject = throwable;
				responseType = CallResponse.throw_;
			}

			Object returnValue(String lastSig, Object[] args) {
				checkSignature(lastSig, args);
				return responseObject;
			}

			Object throwException(String lastSig, Object[] args) {
				checkSignature(lastSig, args);
				throw (RuntimeException) responseObject;
			}

			private void checkSignature(String lastSig, Object[] args) {
				if (!signature.equals(lastSig)) {
					throw new IllegalArgumentException("Signatures do not match");
				}
				if (!Arrays.equals(this.args, args)) {
					throw new IllegalArgumentException("Arguments do not match");
				}
			}

			@Override
			public String toString() {
				return String.format("Call with signature [%s] and arguments %s", this.signature,
					ObjectUtils.nullSafeToString(args));
			}
		}


		/**
		 * The list of recorded calls.
		 */
		private final LinkedList<Call> calls = new LinkedList<Call>();

		/**
		 * The number of calls already verified.
		 */
		private int verified;


		public void verify() {
			if (verified != calls.size()) {
				throw new IllegalStateException("Expected " + calls.size() + " calls, but received " + verified);
			}
		}

		/**
		 * Validate the call and provide the expected return value.
		 */
		public Object respond(String lastSig, Object[] args) {
			Call c = nextCall();

			switch (c.responseType) {
				case return_: {
					return c.returnValue(lastSig, args);
				}
				case throw_: {
					return c.throwException(lastSig, args);
				}
				default: {
					throw new IllegalStateException("Behavior of " + c + " not specified");
				}
			}
		}

		private Call nextCall() {
			verified++;
			if (verified > calls.size()) {
				throw new IllegalStateException("Expected " + calls.size() + " calls, but received " + verified);
			}
			// The 'verified' count is 1-based; whereas, 'calls' is 0-based.
			return calls.get(verified - 1);
		}

		public void expectCall(String lastSig, Object[] lastArgs) {
			calls.add(new Call(lastSig, lastArgs));
		}

		public boolean hasCalls() {
			return !calls.isEmpty();
		}

		public void expectReturn(Object retVal) {
			Call c = calls.getLast();
			if (c.responseTypeAlreadySet()) {
				throw new IllegalStateException("No method invoked before setting return value");
			}
			c.setReturnValue(retVal);
		}

		public void expectThrow(Throwable throwable) {
			Call c = calls.getLast();
			if (c.responseTypeAlreadySet()) {
				throw new IllegalStateException("No method invoked before setting throwable");
			}
			c.setThrowable(throwable);
		}

		/**
		 * Reset the internal state of this {@code Expectations} instance.
		 */
		public void reset() {
			this.calls.clear();
			this.verified = 0;
		}
	}


	/**
	 * Pointcut that identifies call stacks when mocking should be triggered.
	 */
	protected abstract pointcut mockStaticsTestMethod();

	/**
	 * Pointcut that identifies which method invocations to mock.
	 */
	protected abstract pointcut methodToMock();

	@SuppressAjWarnings("adviceDidNotMatch")
	after() returning : mockStaticsTestMethod() {
		if (recording && (expectations.hasCalls())) {
			throw new IllegalStateException(
				"Calls have been recorded, but playback state was never reached. Set expectations and then call "
						+ this.getClass().getSimpleName() + ".playback();");
		}
		verifyInternal();
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	Object around() : methodToMock() && cflowbelow(mockStaticsTestMethod()) {
		if (recording) {
			expectations.expectCall(thisJoinPointStaticPart.toLongString(), thisJoinPoint.getArgs());
			// Return value doesn't matter
			return null;
		}
		else {
			return expectations.respond(thisJoinPointStaticPart.toLongString(), thisJoinPoint.getArgs());
		}
	}

}
