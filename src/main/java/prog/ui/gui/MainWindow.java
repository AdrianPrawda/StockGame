package prog.ui.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainWindow extends Application {
	Stage window;
	Locale locale = Locale.getDefault();
	String resBundleName = "i10n";
	
	/**
	 * Starts displaying the main window.
	 * 
	 * @param args	System arguments that change the behavior of the window.
	 */
	public static void main(String[] args) {
        Application.launch(args);
    }
	
	/**
	 * Set local for this UI Element to support multiple languages.
	 * For this to work the keys used by this class have to exist within the properties file. 
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

	@Override
    public void start(Stage primaryStage) throws Exception{
		//Load from fxml
		BorderPane root = FXMLLoader.load(MainWindow.class.getResource("MainWindow.fxml"));
		
		window = primaryStage;
		window.setTitle("Stock Game");
		
		Scene mainScene = new Scene(root);
		window.setScene(mainScene);
		window.show();
		
//		//i18n support
//		String title, inpCommand;
//		
//		try{
//			ResourceBundle bundle = ResourceBundle.getBundle(resBundleName, locale);
//			
//			title = bundle.getString("MainWindow.title");
//			inpCommand = bundle.getString("MainWindow.inputCommand");
//		}catch(MissingResourceException e){
//			title = "Title";
//			inpCommand = "Input new command";
//		}
//		
//		window = primaryStage;
//        window.setTitle(title);
//        
//        GridPane grid = new GridPane();
//        grid.setPadding(new Insets(10, 10, 10, 10));
//        grid.setVgap(8);
//        grid.setHgap(10);
//        
//        //Console output label
//        Label consoleOut = new Label("TEST LABEL");
//        GridPane.setConstraints(consoleOut, 0, 0);
//        
//        //Console input label
//        TextField consoleIn = new TextField();
//        consoleIn.setPromptText(inpCommand);
//        GridPane.setConstraints(consoleIn, 0, 1);
//        
//        grid.gridLinesVisibleProperty();
//        grid.getChildren().addAll(consoleOut, consoleIn);
//        
//        Scene mainScene = new Scene(grid, 300, 200);
//        window.setScene(mainScene);
//        
//        window.show();
    }
	
}
