package com.chinarewards.gwt.elt.client.gift.presenter;

import java.util.Date;
import com.chinarewards.gwt.elt.client.mvp.Display;
import com.chinarewards.gwt.elt.client.mvp.Presenter;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;

public interface GiftPresenter extends Presenter<GiftPresenter.GiftDisplay> {

	public static interface GiftDisplay extends Display {

		public HasValue<String> getName();

		public HasValue<String> getExplains();

		public String getType();

		public HasValue<String> getSource();

		public HasValue<String> getBusiness();

		public HasValue<String> getAddress();

		public HasValue<String> getTell();

		public HasValue<String> getPhoto();

		public Image getGiftImage();

		public HasValue<String> getStock();

		public HasValue<String> getPhone();

		public HasValue<Boolean> getStatus();

		public HasValue<Boolean> getDeleted();

		public HasValueChangeHandlers<Date> getIndate();

		public HasValueChangeHandlers<Date> getRecorddate();

		public HasValue<String> getRecoduser();

		public HasValueChangeHandlers<Date> getUpdatetime();

		// -----------------------------------------
		public FormPanel getPhotoForm();

		public FileUpload getPhotoUpload();

		public HasClickHandlers getUploadClick();

		public HasClickHandlers getSaveClick();

		public void clear();

	}
}