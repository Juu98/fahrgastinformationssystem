/*
 * Copyright 2014-2015 the original author or authors.
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
package guestbook.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guestbook.search;
import guestbook.searchEntry;

/**
 * A controller to handle web requests to manage {@link searchEntry}s
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Controller
public class GuestbookController {

	// A special header sent with each AJAX request
	private static final String IS_AJAX_HEADER = "X-Requested-With=XMLHttpRequest";

	private final search search;
	
	

	/**
	 * Creates a new {@link GuestbookController} using the given {@link search}. The {@link Autowired} causes the
	 * Spring container to try to find a Spring bean of type {@link search} and use it to create an instance of
	 * {@link GuestbookController}.
	 * 
	 * @param search must not be {@literal null}.
	 */
	@Autowired
	public GuestbookController(search search) {

		Assert.notNull(search, "Guestbook must not be null!");
		this.search = search;
	}

	/**
	 * Handles requests to the application root URI. Note, that you can use {@code redirect:} as prefix to trigger a
	 * browser redirect instead of simply rendering a view.
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "redirect:/guestbook";
	}

	/**
	 * Handles requests to access the guestbook. Obtains all currently available {@link searchEntry}s and puts them
	 * into the {@link Model} that's used to render the view.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/guestbook", method = RequestMethod.GET)
	public String guestBook(Model model, GuestbookForm form) {

		model.addAttribute("entries", search.findAll());
		model.addAttribute("form", form);

		return "guestbook";
	}
	

	/**
	 * Handles requests to create a new {@link searchEntry}. Uses the fields {@code name} and {@code text} from the
	 * HTML form via the {@link RequestParam} annotations. The mapping also supports other types than {@link String}, see
	 * {@link #removeEntry(Long)}.
	 * <p>
	 * For the sake of simplicity we don't do any validation here. Spring has support for that kind of stuff but we leave
	 * that for the VideoShop example to cover.
	 * 
	 * @param name the name of the person that made the entry
	 * @param text the actual text of the entry
	 * @return
	 */
	@RequestMapping(value = "/guestbook", method = RequestMethod.POST)
	public String addEntry(@Valid GuestbookForm form, Errors errors, Model model) {

		if (errors.hasErrors()) {
			return guestBook(model, form);
		}

		search.save(new searchEntry(form.getName(), form.getText()));
		return "redirect:/guestbook";
	}

	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String searchEntries(String term, Errors erros, Model model){
		
		return "search";
	}
	
	/**
	 * Handles AJAX requests to create a new {@link searchEntry}.
	 * 
	 * @param name the name of the person that made the entry
	 * @param text the actual text of the entry
	 * @param model
	 * @return
	 * @see #addEntry(String, String)
	 */
	@RequestMapping(value = "/guestbook", method = RequestMethod.POST, headers = IS_AJAX_HEADER)
	public String addEntry(@Valid GuestbookForm form, Model model) {

		model.addAttribute("entry", search.save(new searchEntry(form.getName(), form.getText())));
		model.addAttribute("index", search.count());
		return "guestbook :: entry";
	}

	/**
	 * Deletes a {@link searchEntry}. Note how the path variable used in the {@link RequestMapping} annotation is bound
	 * to the controller method using the {@link PathVariable} annotation.
	 * 
	 * @param id the id of the {@link searchEntry} to delete.
	 * @return
	 */
	@RequestMapping(value = "/guestbook/{id}", method = RequestMethod.DELETE)
	public String removeEntry(@PathVariable Long id) {
		search.delete(id);
		return "redirect:/guestbook";
	}

	/**
	 * Handles AJAX requests to delete {@link searchEntry}s.
	 * 
	 * @param id the id of the {@link searchEntry} to delete.
	 * @return
	 */
	@RequestMapping(value = "/guestbook/{id}", method = RequestMethod.DELETE, headers = IS_AJAX_HEADER)
	public HttpEntity<?> removeEntryJS(@PathVariable Long id) {

		return search.findOne(id).map(e -> {
			search.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
}
