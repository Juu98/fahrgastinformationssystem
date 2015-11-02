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

import contacts.Contact;
import contacts.ContactManager;
import contacts.ContactsView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The application's controller to listen for http requests.
 * @author Robert
 */
@Controller
public class ContactsController {
	private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private final ContactManager manager;
	
	/**
	 * Default Constructor using Spring's magic annotations...
	 * @param manager must not be {@literal null}
	 */
	@Autowired
	public ContactsController(ContactManager manager){
		Assert.notNull(manager, "You need to specify a Contact Manager!");
		this.manager = manager;
	}
	
	/**
	 * Redirects requests from landing page to the manager page.
	 * @return 
	 */
	@RequestMapping("/")
	public String index(){
		return "redirect:/manager";
	}
	
	/**
	 * Handles reading requests on the manager page.
	 * @param model representing all {@links Contact}s
	 * @return 
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	String contactManager(Model model, ContactsForm form){
		model.addAttribute("entries", new ContactsView(manager.findAll()));
		model.addAttribute("form", form);
		model.addAttribute("alphabet", alphabet);
		
		return "manager";
	}
	
	/**
	 * Adds a new {@link Contact} by handling the data sent via the HTML form.
	 * @param form
	 * @param errors
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.POST)
	String addContact(@Valid ContactsForm form, Errors errors, Model model){
		if (errors.hasErrors()){
			return contactManager(model, form);
		}
		
		manager.save(new Contact(form.getFirstName(), form.getLastName(), form.getEmail()));
		return "redirect:/manager";
	}
	
	/**
	 * Deletes a {@link Contact} from the repository.
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/manager/{id}", method = RequestMethod.DELETE)
	String removeEntry(@PathVariable Long id){
		manager.delete(id);
		return "redirect:/manager";
	}
}
