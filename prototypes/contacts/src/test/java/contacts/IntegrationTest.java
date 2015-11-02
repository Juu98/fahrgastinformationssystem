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

import javax.transaction.Transactional;
import static org.hamcrest.CoreMatchers.hasItem;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for the {@link ContactManager}
 * @author Robert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class IntegrationTest {
	@Autowired ContactManager repository;
	private Contact entry;

	@Before
	public void setUp(){
		entry = new Contact("Max", "Mustermann", "max.mustermann@abc.de");
		repository.save(entry);
	}
	
	@Test
	public void persistsGuestbookEntry() {
		assertThat(repository.findAll(), hasItem(entry));
	}
	
	@Test
	public void removeItem(){
		repository.delete(entry.getId());
		Assert.assertEquals(repository.count(), 0);
	}
}
