package prog.ui.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	int height, width;
	Locale locale = Locale.getDefault();
	String resBundleName = "i10n";
	
	public AlertBox(){
		this(100,200);
	}
	
	public AlertBox(int height, int width){
		this.height = height;
		this.width = width;
	}
	
	/**
	 * Set local for this UI Element to support multiple languages.
	 * For this to work <b>AlertBox.confirm</b> has to exist within the properties file. 
	 * Otherwise standard values will be displayed. If no locale is given, the default locale will be used.
	 * 
	 * @param locale	Set locale for i18n support
	 */
	public void setLocale(Locale locale){
		this.locale = locale;
	}
	
	/**
	 * Sets the name of the resource bundle that should be used for localization.
	 * If no name is set, it will default to i10n
	 * 
	 * @param name		Name of the resource bundle that should be used
	 */
	public void setResourceBundleName(String name){
		resBundleName = name;
	}
	

	public void display(String title, String message){
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(width);
		
		Label label = new Label();
		label.setText(message);
		
		//i18n support
		String ok;
		try{
			ResourceBundle bundle = ResourceBundle.getBundle(resBundleName, locale);
			ok = bundle.getString("AlertBox.confirm");
		}catch(MissingResourceException e){
			ok = "OK";
		}
		
		Button button = new Button(ok);
		button.setOnAction(e -> window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, button);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
