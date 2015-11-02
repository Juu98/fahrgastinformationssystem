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
package contacts.web;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents the HTML form to create a new {@link Contact}.
 * @author Robert
 */
public interface ContactsForm {
	@NotBlank(message = "Please enter a first name.")
	public String getFirstName();
	
	@NotBlank(message = "Please enter a last name!")
	public String getLastName();
	
	@NotBlank(message = "Please enter an email address!")
	@Email(	regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
			flags = Pattern.Flag.CASE_INSENSITIVE,
			message = "This is not a valid email address!")
	public String getEmail();
}
