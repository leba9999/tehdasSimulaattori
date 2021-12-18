module tehdasSimulaattoriJavaFX {

	exports simu.view;
	exports simu.framework;
	exports simu.model;
	exports simu;
	
	requires javafx.controls;
	requires java.desktop;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.xml;
	requires java.sql;
	requires java.sql.rowset;
	requires javafx.base;
	requires java.prefs;
	requires org.junit.jupiter.api;

	opens simu to javafx.graphics, javafx.fxml;
	opens simu.view to javafx.graphics, javafx.fxml;
	opens simu.model to java.xml.bind;
	opens simu.framework to java.xml.bind;
}
