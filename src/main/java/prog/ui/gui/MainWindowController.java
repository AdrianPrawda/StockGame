package prog.ui.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import prog.core.AccountManagerImpl;
import prog.core.Player;
import prog.core.provider.HistoricalStockPriceProvider;
import prog.core.provider.StockPriceProvider;
import prog.interfaces.AccountManager;
import prog.ui.CommandProcessor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MainWindowController implements Initializable{
	
	@FXML
	private Button loadPlayerButton;
	
    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox menuBarContainer;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private Menu optionsMenu;

    @FXML
    private MenuItem changeRefTimeMenuItem;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private GridPane mainPane;

    @FXML
    private TextArea consoleOutputField;

    @FXML
    private HBox topRow;

    @FXML
    private Label selectedPlayerLabel;

    @FXML
    private ChoiceBox<String> playerList;

    @FXML
    private Button newPlayerButton;

    @FXML
    private VBox playerCapitalContainer;

    @FXML
    private Label capitalLabel;

    @FXML
    private TextField playerCapital;

    @FXML
    private Label capitalProfit;

    @FXML
    private VBox PlayerAccountContainer;

    @FXML
    private Label accountLabel;

    @FXML
    private TextField playerAccount;

    @FXML
    private Label accountProfit;

    @FXML
    private VBox playerSharesContainer;

    @FXML
    private Label sharesLabel;

    @FXML
    private TextArea playerSharesField;

    @FXML
    private VBox toggleButtonContainer;

    @FXML
    private HBox agentContainer;

    @FXML
    private Label agentLabel;

    @FXML
    private ToggleButton agentToggleButton;

    @FXML
    private HBox debugContainer;

    @FXML
    private Label debugLabel;

    @FXML
    private ToggleButton debugToggleButton;

    @FXML
    private HBox playerInputContainer;

    @FXML
    private TextField playerInput;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane stockPane;

    @FXML
    private VBox stockPaneVBox;
    
    private StockPriceProvider provider = new HistoricalStockPriceProvider();
    private AccountManagerImpl accountManager = new AccountManagerImpl(provider);
    
    private CommandProcessor commandProcessor = new CommandProcessor(accountManager, AccountManager.class);
    
    private boolean debugFlag = false;
	private ResourceBundle bundle;
	private Map<String,String> dict = new HashMap<String,String>();

	void updatePlayerList(){
		String selectedItem = playerList.getSelectionModel().getSelectedItem();
		
		ObservableList<String> pList = FXCollections.observableArrayList();
		accountManager.getPlayers().keySet().iterator().forEachRemaining(e -> pList.add(e));
		
		playerList.setItems(pList);
		
		playerList.getSelectionModel().select(selectedItem);
		Player activePlayer = accountManager.getPlayers().get(selectedItem);
		
		playerCapital.setText(String.valueOf(activePlayer.getCashAccount().value()));
		playerAccount.setText(String.valueOf(activePlayer.getCashAccount().value()));
		
		playerSharesField.setText("BLA");
	}
	
	@FXML
	void loadPlayer(ActionEvent event){
		String activePlayerName = playerList.getSelectionModel().getSelectedItem();
		Player activePlayer = accountManager.getPlayers().get(activePlayerName);
		
		playerCapital.setText(String.valueOf(activePlayer.getCashAccount().value()));
		playerAccount.setText(String.valueOf(activePlayer.getCashAccount().value()));
		
		playerSharesField.setText("BLA");
//		String shareList = activePlayer.getShareDepositAccount().getAllSharesAsSnapshot()
	}
	
    //Currently not used
    @FXML
    void closeApplication(ActionEvent event) {
    	event.consume();
    	
    	ConfirmBox confirmBox = new ConfirmBox();
    	boolean exit = confirmBox.display("Close Application?", "Do you really want to close StockGame?");
    	
    	if(exit){
    		Stage stage = (Stage) borderPane.getScene().getWindow();
    		stage.close();
    		System.exit(0);
    	}else{
    		print("Exiting aborted");
    	}
    }

    @FXML
    void openAboutMenu(ActionEvent event) {
    	System.out.println(event.toString());
    	
    	AlertBox about = new AlertBox();
    	about.display("About", "StockGame Project SS '15");
    }

    @FXML
    void openNewPlayerMenu(ActionEvent event) {
    	System.out.println(event.toString());
    }

    @FXML
    void openRefTimeMenu(ActionEvent event) {
    	System.out.println(event.toString());
    }

    @FXML
    void sendPlayerInput(ActionEvent event) {
    	String input = playerInput.getText();
    	String consoleText = consoleOutputField.getText();
    	
    	Object result = commandProcessor.process(input.replaceAll("\n", ""));
    	String newConsoleText = "";
    	
    	if(result == null){
    		newConsoleText = consoleText + "\n> " + input;
    	}else{
    		newConsoleText = consoleText + "\n> " + input + "\n" + result;
    	}
    	
    	//Set text and clean user input
    	consoleOutputField.setText(newConsoleText);
    	playerInput.setText("");
    	
    	updatePlayerList();
    }

    @FXML
    void toggleDebugMode(ActionEvent event) {
    	if(dict.isEmpty())
    		fillDict();
    	
    	if(playerList.getSelectionModel().getSelectedItem() == null){
    		print("No player loaded");
    		return;
    	}
    	
    	Player player = accountManager.getPlayers().get(playerList.getSelectionModel().getSelectedItem());
    	
    	if(debugToggleButton.isSelected()){
    		debugToggleButton.setText(dict.get("UI.Button.On"));
    		debugFlag = true;
    		
    		print(dict.get("UI.SystemMessage.ToggleDebugModeOn"));
    		player.getPlayerAgent().startTrading();
    	}else{
    		System.out.println("Is not selected");
    		debugToggleButton.setText(dict.get("UI.Button.Off"));
    		debugFlag = false;
    		
    		print(dict.get("UI.SystemMessage.ToggleDebugModeOff"));
    		player.getPlayerAgent().dismiss();
    	}
    }

    @FXML
    void togglePlayerAgent(ActionEvent event) {
    	if(dict.isEmpty())
    		fillDict();
    	
    	if(agentToggleButton.isSelected()){
    		agentToggleButton.setText(dict.get("UI.Button.On"));
    		
    		print(dict.get("UI.SystemMessage.ToggleDebugModeOn"));
    	}else{
    		System.out.println("Is not selected");
    		agentToggleButton.setText(dict.get("UI.Button.Off"));
    		
    		print(dict.get("UI.SystemMessage.ToggleDebugModeOff"));
    	}
    }
    
    private void print(String mssg){
    	String consoleText = consoleOutputField.getText();
    	String newConsoleText = consoleText + "\n" + mssg;
    	consoleOutputField.setText(newConsoleText);
    }
    
    private void fillDict(){
    	dict.put("UI.Button.Off", "Off");
		dict.put("UI.Button.On", "On");
		dict.put("UI.Button.Buy", "Buy");
		dict.put("UI.Button.Sell", "Sell");
		
		dict.put("UI.SystemMessage.ToggleDebugModeOn", "Debug mode is now on");
		dict.put("UI.SystemMessage.ToggleDebugModeOff", "Debug mode is now off");
    }
    
    private void setUp(){
    	//Create Players
    	accountManager.createPlayer("Player1");
    	accountManager.createPlayer("Player2");
    	accountManager.createPlayer("Player3");
    	
    	//Create a few shares
    	provider.createShare("BMW", 11075);
		provider.createShare("Siemens", 10050);
		provider.createShare("Volkswagen", 12398);
		provider.createShare("Osram", 8863);
		provider.createShare("Commerzbank", 1226);
		provider.createShare("BASF", 8918);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("MainWindowController initializing...");
		setUp();
		
		ObservableList<String> pList = FXCollections.observableArrayList();
		accountManager.getPlayers().keySet().iterator().forEachRemaining(e -> pList.add(e));
		
		playerList.setItems(pList);
		
		System.out.println("MainWindowController initializing, Step 2...");
		
		bundle = resources;
		if(bundle == null)
			return;
		
		//Translate all objects, all keys have to be present or the loading will fail (all values that have been loaded to this point will remain loaded)
		try{
			System.out.println("MainWindowController try to initialize");
			
			fileMenu.setText(bundle.getString("UI.Menu.File"));
			optionsMenu.setText(bundle.getString("UI.Menu.Options"));
			helpMenu.setText(bundle.getString("UI.Menu.Help"));
			
			selectedPlayerLabel.setText(bundle.getString("UI.Label.SelectedPlayer"));
			capitalLabel.setText(bundle.getString("UI.Label.Capital"));
			accountLabel.setText(bundle.getString("UI.Label.Account"));
			sharesLabel.setText(bundle.getString("UI.Label.Shares"));
			agentLabel.setText(bundle.getString("UI.Label.Agent"));
			debugLabel.setText(bundle.getString("UI.Label.Debug"));
			
			newPlayerButton.setText(bundle.getString("UI.Button.NewPlayer"));
			sendButton.setText(bundle.getString("UI.Button.Send"));
			
			dict.put("UI.Button.Off", bundle.getString("UI.Button.Off"));
			dict.put("UI.Button.On", bundle.getString("UI.Button.On"));
			dict.put("UI.Button.Sell", bundle.getString("UI.Button.Sell"));
			dict.put("UI.Button.Buy", bundle.getString("UI.Button.Buy"));
			
			agentToggleButton.setText(bundle.getString("UI.Button.Off"));
			debugToggleButton.setText(bundle.getString("UI.Button.Off"));
			
			dict.put("UI.SystemMessage.ToggleDebugModeOn", bundle.getString("UI.SystemMessage.ToggleDebugModeOn"));
			dict.put("UI.SystemMessage.ToggleDebugModeOff", bundle.getString("UI.SystemMessage.ToggleDebugModeOff"));
			
		}catch(MissingResourceException e){
			System.out.println("Failed to properly initialize MainWindowController");
			
			dict = new HashMap<String,String>();
			
			dict.put("UI.Button.Off", "Off");
			dict.put("UI.Button.On", "On");
			dict.put("UI.Button.Buy", "Buy");
			dict.put("UI.Button.Sell", "Sell");
			
			dict.put("UI.SystemMessage.ToggleDebugModeOn", "Debug mode is now on");
			dict.put("UI.SystemMessage.ToggleDebugModeOff", "Debug mode is now off");
		}
		
		System.out.println("Done initializing MainWindowController");
	}

}
