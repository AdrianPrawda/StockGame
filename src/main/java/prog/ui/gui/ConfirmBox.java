package prog.ui.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	int height, width;
	boolean answer;
	Locale locale = Locale.getDefault();
	String resBundleName = "i10n";
	
	public ConfirmBox(){
		this(100,200);
	}
	
	public ConfirmBox(int height, int width){
		this.height = height;
		this.width = width;
	}
	
	/**
	 * Set local for this UI Element to support multiple languages.
	 * For this to work <b>ConfirmBox.yes</b> and <b>ConfirmBox.no</b> has to exist within the properties file. 
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
	
	/**
	 * Displays the confirmation window. 
	 * While it is active, other windows can't be accessed.
	 * 
	 * @param title		String displayed in the title bar of the window
	 * @param message	Message displayed to the user
	 * 
	 * @return	Returns whether the user has confirmed or denied as a boolean value
	 */
	public boolean display(String title, String message){
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(width);
		
		Label label = new Label();
		label.setText(message);
		
		//i18n support
		String yes,no;
		try{
			ResourceBundle bundle = ResourceBundle.getBundle(resBundleName, locale);
			yes = bundle.getString("ConfirmBox.yes");
			no = bundle.getString("ConfirmBox.no");
		}catch(MissingResourceException e){
			yes = "Yes";
			no = "No";
		}
		
		Button yesButton = new Button(yes);
		Button noButton = new Button(no);
		
		yesButton.setOnAction(e -> {
			answer = true;
			window.close();
		});
		
		noButton.setOnAction(e -> {
			answer = false;
			window.close();
		});
		
		VBox layout = new VBox(10);
		HBox hLayout = new HBox(10);
		
		hLayout.setAlignment(Pos.CENTER);
		hLayout.autosize();
		hLayout.getChildren().addAll(yesButton, noButton);
		
		layout.getChildren().addAll(label, hLayout);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
	
}
