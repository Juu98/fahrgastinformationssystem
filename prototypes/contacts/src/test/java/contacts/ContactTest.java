/*
 * Copyright 2015 TU Dresden.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package contacts;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for a {@link Contact}
 * @author Robert
 */
public class ContactTest {
	private final String fn = "Max", ln = "Mustermann";
	private final String em = "max.mustermann@abc.de";
	private final Contact max = new Contact(fn, ln, em);
	
	// First Name
	@Test(expected=IllegalArgumentException.class)
	public void firstNameNull(){
		new Contact(null, ln, em);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void firstNameEmpty(){
		new Contact("", ln, em);
	}
	
	@Test
	public void firstNameTest(){
		Assert.assertEquals("First name wrong", fn, max.getFirstName());
	}
	
	// Last Name	
	@Test(expected=IllegalArgumentException.class)
	public void lastNameNull(){
		new Contact(fn, null, em);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void lastNameEmpty(){
		new Contact(fn, "", em);
	}
	
	@Test
	public void lastNameTest(){
		Assert.assertEquals("Last name wrong", ln, max.getLastName());
	}
	
	// E-Mail
	@Test(expected=IllegalArgumentException.class)
	public void emailNull(){
		new Contact(fn, ln, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void emailEmpty(){
		new Contact(fn, ln, "");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void emailInvalid(){
		new Contact(fn, ln, "foo-bar.com");
	}
	
	@Test
	public void emailTest(){
		Assert.assertEquals("Email wrong", em, max.getEmail());
	}
}
