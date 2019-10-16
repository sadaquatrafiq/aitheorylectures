package com.mazeBuilder.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface IAlgoithmModel {

	public IntegerProperty nodeCountProperty();
	public IntegerProperty pathCountProperty();
	public StringProperty statusProperty();
}
