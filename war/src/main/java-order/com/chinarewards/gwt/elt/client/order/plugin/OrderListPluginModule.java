/**
 * 
 */
package com.chinarewards.gwt.elt.client.order.plugin;

import com.chinarewards.gwt.elt.client.gift.plugin.GiftListPluginDescriptor;
import com.chinarewards.gwt.elt.client.order.editor.OrderListEditor;
import com.chinarewards.gwt.elt.client.order.editor.OrderListEditorDescriptor;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author NIcho
 * @since 2011年12月9日 
 */
public class OrderListPluginModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(GiftListPluginDescriptor.class).in(Singleton.class);

		bind(OrderListEditorDescriptor.class).in(Singleton.class);
		bind(OrderListEditor.class);
	}

}
