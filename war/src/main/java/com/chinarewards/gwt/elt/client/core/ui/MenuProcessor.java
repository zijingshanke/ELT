package com.chinarewards.gwt.elt.client.core.ui;

import com.google.gwt.user.client.ui.Panel;

public interface MenuProcessor {

	void add(MenuItem item);

	MenuItem getMenuItem(String menuId);

	void render(Panel container);

}
