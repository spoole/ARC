/*******************************************************************************
 * Copyright 2012 Paweł Psztyć
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.rest.client.ui;


import java.util.List;

import org.rest.client.storage.store.objects.RequestObject;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
/**
 * View for "Saved requests" place
 * @author jarrod
 *
 */
public interface SavedView extends IsWidget {
	/**
	 * Presenter interface
	 * @author Paweł Psztyć
	 *
	 */
	public interface Presenter {
		/**
		 * Change place
		 * @param place
		 */
		void goTo(Place place);
		/**
		 * Get next results page and set in view
		 */
		void getNextItemsPage();
	}
	/**
	 * Sets presenter for this view
	 * @param listener
	 */
	void setPresenter(Presenter listener);
	/**
	 * Set store data
	 * @param data
	 */
	void addData(List<RequestObject> data);
	
	/**
	 * Set information that there is no more items in datastore
	 */
	void setNoMoreItems();
}
