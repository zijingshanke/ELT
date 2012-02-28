package com.chinarewards.gwt.elt.client.rewards.plugin;

import java.util.HashSet;
import java.util.Set;

import com.chinarewards.gwt.elt.client.core.Extension;
import com.chinarewards.gwt.elt.client.core.ExtensionPoint;
import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.core.Plugin;
import com.chinarewards.gwt.elt.client.core.PluginDescriptor;
import com.chinarewards.gwt.elt.client.core.ui.MenuItem;
import com.chinarewards.gwt.elt.client.plugin.MenuConstants;
import com.chinarewards.gwt.elt.client.plugin.PluginConstants;
import com.chinarewards.gwt.elt.client.rewards.editor.RewardsListStaffEditorDescriptor;
import com.chinarewards.gwt.elt.model.rewards.RewardsPageClient;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

/**
 * @author yanrui
 */
public class RewardsListStaffPluginDescriptor implements PluginDescriptor {

	final static Set<Extension> ext = new HashSet<Extension>();
	final RewardsListStaffPlugin RewardsListPlugin;
	final RewardsListStaffEditorDescriptor rewardsListEditorDescriptor;

	@Inject
	public RewardsListStaffPluginDescriptor(
			final RewardsListStaffEditorDescriptor rewardsListEditorDescriptor) {
		this.rewardsListEditorDescriptor = rewardsListEditorDescriptor;
		RewardsListPlugin = new RewardsListStaffPlugin(this);

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
						return MenuConstants.MENU_ORDER_REWARDSLIST_STAFF_SEARCH;
					}

					@Override
					public String getMenuId() {
						return RewardsListStaffConstants.MENU_REWARDSLIST_STAFF_SEARCH;
					}

					@Override
					public String getParentMenuId() {
						return null;
					}

					@Override
					public String getTitle() {
						return "我的获奖历史";
					}

					@Override
					public void execute() {
						RewardsPageClient rpc = new RewardsPageClient();
						Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										RewardsListStaffConstants.EDITOR_REWARDSLIST_STAFF_SEARCH,
										"EDITOR_REWARDSLIST_STAFF_SEARCH_DO_ID",
										rpc);
					}

					@Override
					public Image getIcon() {
						return null;
					}

				};
			}

			@Override
			public PluginDescriptor getPluginDescriptor() {
				return RewardsListStaffPluginDescriptor.this;
			}

		});

		ext.add(new Extension() {

			@Override
			public String getExtensionPointId() {
				return PluginConstants.EDITOR;
			}

			@Override
			public Object getInstance() {
				return rewardsListEditorDescriptor;
			}

			@Override
			public PluginDescriptor getPluginDescriptor() {
				return RewardsListStaffPluginDescriptor.this;
			}

		});
	}

	@Override
	public String getPluginId() {
		return RewardsListStaffConstants.PLUGIN_REWARDSLIST_STAFF;
	}

	@Override
	public Plugin getInstance() {
		return RewardsListPlugin;
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
