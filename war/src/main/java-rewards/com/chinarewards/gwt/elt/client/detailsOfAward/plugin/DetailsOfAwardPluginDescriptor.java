/**
 * 
 */
package com.chinarewards.gwt.elt.client.detailsOfAward.plugin;

import java.util.HashSet;
import java.util.Set;

import com.chinarewards.gwt.elt.client.core.Extension;
import com.chinarewards.gwt.elt.client.core.ExtensionPoint;
import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.core.Plugin;
import com.chinarewards.gwt.elt.client.core.PluginDescriptor;
import com.chinarewards.gwt.elt.client.core.ui.MenuItem;
import com.chinarewards.gwt.elt.client.detailsOfAward.editor.DetailsOfAwardEditorDescriptor;
import com.chinarewards.gwt.elt.client.plugin.MenuConstants;
import com.chinarewards.gwt.elt.client.plugin.PluginConstants;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

/**
 * @author Cream
 * @since 0.0.1 2010-09-19
 */
public class DetailsOfAwardPluginDescriptor implements PluginDescriptor {

	final static Set<Extension> ext = new HashSet<Extension>();
	final DetailsOfAwardPlugin detailsOfAwardPlugin;
	final DetailsOfAwardEditorDescriptor detailsOfAwardEditorDescriptor;

	@Inject
	public DetailsOfAwardPluginDescriptor(
			final DetailsOfAwardEditorDescriptor detailsOfAwardEditorDescriptor) {
		this.detailsOfAwardEditorDescriptor = detailsOfAwardEditorDescriptor;
		detailsOfAwardPlugin = new DetailsOfAwardPlugin(this);

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
						return MenuConstants.MENU_ORDER_DETAILSOFAWARD_SEARCH;
					}

					@Override
					public String getMenuId() {
						return DetailsOfAwardConstants.MENU_DETAILSOFAWARD_SEARCH;
					}

					@Override
					public String getParentMenuId() {
						return null;
					}

					@Override
					public String getTitle() {
						return "获奖详细";
					}

					@Override
					public void execute() {
						Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										DetailsOfAwardConstants.MENU_DETAILSOFAWARD_SEARCH,
										"EDITOR_DETAILSOFAWARD_SEARCH_DO_ID", null);
					}

					@Override
					public Image getIcon() {
						return null;
					}

				};
			}

			@Override
			public PluginDescriptor getPluginDescriptor() {
				return DetailsOfAwardPluginDescriptor.this;
			}

		});

		ext.add(new Extension() {

			@Override
			public String getExtensionPointId() {
				return PluginConstants.EDITOR;
			}

			@Override
			public Object getInstance() {
				return detailsOfAwardEditorDescriptor;
			}

			@Override
			public PluginDescriptor getPluginDescriptor() {
				return DetailsOfAwardPluginDescriptor.this;
			}

		});
	}

	@Override
	public String getPluginId() {
		return DetailsOfAwardConstants.PLUGIN_DETAILSOFAWARD;
	}

	@Override
	public Plugin getInstance() {
		return detailsOfAwardPlugin;
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
