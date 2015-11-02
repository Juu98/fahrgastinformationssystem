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

import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * Repository to manage the {@link Contact}s.
 * @author Robert
 */
public interface ContactManager extends Repository<Contact, Long> {
	
	/**
	 * Gets the number of {@link Contact}s in the repository.
	 * @return the number
	 */
	int count();
	
	/**
	 * Adds a {@link Contact} to the repository.
	 * @param entry the {@link Contact} to add
	 * @return the {@link Contact}
	 */
	Contact save(Contact entry);
	
	/**
	 * Gets a {@link Contact} by its ID.
	 * @param id the {@link Contact}'s ID
	 * @return the {@link Contact}
	 */
	Optional<Contact> findOne(Long id);
	
	/**
	 * Gets all {@link Contact}s
	 * @return the {@link Iterable}
	 */
	Iterable<Contact> findAll();
	
	/**
	 * Deletes a {@link Contact} from the repository.
	 * @param id the {@link Contact}'s ID
	 */
	void delete(Long id);
}
