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

import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.util.Assert;

/**
 * A single contact entity.
 * @author Robert
 */
@Entity
public class Contact implements Comparable<Contact>{
	private @Id @GeneratedValue long id;
	private final String firstName, lastName, email;
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
			Pattern.CASE_INSENSITIVE);
	
	/**
	 * Creates a new contact with given information
	 * @param firstName first name, must not be {@literal null} or empty.
	 * @param lastName last name, must not be {@literal null} or empty.
	 * @param email email address, must not be {@literal null} or empty.
	 */
	public Contact(String firstName, String lastName, String email){
		// not null or empty
		Assert.hasText(firstName, "First name must contain text.");
		Assert.hasText(lastName, "Last Name must contain text.");
		Assert.hasText(email, "Email address must contain text.");
		
		// valid email
		Assert.isTrue(EMAIL_PATTERN.matcher(email).matches(), "Not a valid email address.");
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	/**
	 * empty constructor.
	 */
	public Contact(){
		this.firstName = null;
		this.lastName = null;
		this.email = null;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}
	
	@Override
	public String toString() {
		return this.lastName + ", " + this.firstName;
	}
	
	public char getFirstChar(){
		return Character.toUpperCase(this.lastName.charAt(0));
	}

	@Override
	public int compareTo(Contact o) {
		return this.toString().compareTo(o.toString());
	}
}
