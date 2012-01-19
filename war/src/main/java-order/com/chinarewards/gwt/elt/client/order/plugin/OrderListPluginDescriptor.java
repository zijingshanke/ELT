/**
 * 
 */
package com.chinarewards.gwt.elt.client.order.plugin;

import java.util.HashSet;
import java.util.Set;

import com.chinarewards.gwt.elt.client.core.Extension;
import com.chinarewards.gwt.elt.client.core.ExtensionPoint;
import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.core.Plugin;
import com.chinarewards.gwt.elt.client.core.PluginDescriptor;
import com.chinarewards.gwt.elt.client.core.ui.MenuItem;
import com.chinarewards.gwt.elt.client.order.plugin.OrderListConstants;
import com.chinarewards.gwt.elt.client.order.plugin.OrderListPlugin;
import com.chinarewards.gwt.elt.client.order.plugin.OrderListPluginDescriptor;
import com.chinarewards.gwt.elt.client.order.editor.OrderListEditorDescriptor;
import com.chinarewards.gwt.elt.client.plugin.MenuConstants;
import com.chinarewards.gwt.elt.client.plugin.PluginConstants;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

/**
 * @author nicho
 * @since
 */
public class OrderListPluginDescriptor implements PluginDescriptor {

	final static Set<Extension> ext = new HashSet<Extension>();
	final OrderListPlugin OrderListPlugin;
	final OrderListEditorDescriptor orderListEditorDescriptor;

	@Inject
	public OrderListPluginDescriptor(
			final OrderListEditorDescriptor orderListEditorDescriptor) {
		this.orderListEditorDescriptor = orderListEditorDescriptor;
		OrderListPlugin = new OrderListPlugin(this);

		/**
		 * Search user menu
		 */
		ext.add(new Extension() {

			@Override
			public String getExtensionPointId() {
				return PluginConstants.MENU;
			}

			@Override
			public Object getInstance() {
				return new MenuItem() {

					@Override
					public int getOrder() {
						return MenuConstants.MENU_ORDER_GIFTLIST_SEARCH;
					}

					@Override
					public String getMenuId() {
						return OrderListConstants.MENU_GIFTLIST_SEARCH;
					}

					@Override
					public String getParentMenuId() {
						return null;
					}

					@Override
					public String getTitle() {
						return "礼品列表";
					}

					@Override
					public void execute() {

						Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										OrderListConstants.EDITOR_GIFTLIST_SEARCH,
										"EDITOR_REWARDSLIST_SEARCH_DO_ID", null);
					}

					@Override
					public Image getIcon() {
						return null;
					}

				};
			}

			@Override
			public PluginDescriptor getPluginDescriptor() {
				return OrderListPluginDescriptor.this;
			}

		});


		ext.add(new Extension() {

			@Override
			public String getExtensionPointId() {
				return PluginConstants.EDITOR;
			}

			@Override
			public Object getInstance() {
				return orderListEditorDescriptor;
			}

			@Override
			public PluginDescriptor getPluginDescriptor() {
				return OrderListPluginDescriptor.this;
			}

		});
	}

	@Override
	public String getPluginId() {
		return OrderListConstants.PLUGIN_GIFTLIST;
	}

	@Override
	public Plugin getInstance() {
		return OrderListPlugin;
	}

	@Override
	public Set<ExtensionPoint> getExtensionPoints() {
		return null;
	}

	@Override
	public Set<Extension> getExtensions() {
		return ext;
	}

}
