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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Wrapper class to group contacts by first character of last name.
 * @author Robert
 */
public class ContactsView extends HashMap<Character, ArrayList<Contact>>{
	public ContactsView(){
		super(26);
	}
	
	public ContactsView(Iterable<Contact> contacts){
		super(26);
		for (Contact c : contacts) this.add(c);
	}
	
	public void add(Contact c){
		Character fc = c.getFirstChar();
		if (!this.containsKey(fc)){
			this.put(fc, new ArrayList<Contact>());
		}
		this.get(fc).add(c);
	}
	
	public ArrayList<Contact> get(Character chr){
		ArrayList<Contact> ret = super.get(chr);
		Collections.sort(ret);
		return ret;
	}
	
	public int count(){
		int sum = 0;
		for (ArrayList<Contact> cl : this.values()){
			sum += cl.size();
		}
		return sum;
	}
	
	@Override
	public boolean isEmpty(){
		return (this.count() == 0);
	}
}
