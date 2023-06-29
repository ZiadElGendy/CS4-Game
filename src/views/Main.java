package views;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.characters.Direction;
import model.characters.Hero;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import engine.Game;

public class Main extends Application {
	
	Hero selectedHero;
	GridPane gameMap;
	GridPane selectedHeroInventory;
	GridPane selectedHeroStats;
	GridPane otherHerosStats;
	
	Media introMedia = new Media(getClass().getResource("intromusic.mp3").toExternalForm());
    MediaPlayer introMediaPlayer = new MediaPlayer(introMedia);
    AudioClip ButtonMedia = new AudioClip(getClass().getResource("selectButton.mp3").toExternalForm());
    //MediaPlayer ButtonMediaPlayer = new MediaPlayer(ButtonMedia);
    
	public void start(Stage stage) throws Exception {
	 try {
 
			//stage
		 	stage.setTitle("Last of Us");
		 	BorderPane root1 = new BorderPane();
		 	
            introMediaPlayer.setAutoPlay(true);
	        
			//2- opening scene --> root and scene 1
		  	
			//a) create a button and add it to root
			
			Image openbutton = new Image(getClass().getResourceAsStream("button.png"));
			ImageView openingButton = new ImageView(openbutton);
	        root1.setBottom(new StackPane(openingButton));
	        openingButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	        	
				heroSelect(stage);
			});
	        
	        //b)add a background
	        
	        Image image = new Image(getClass().getResourceAsStream("openingBackground.png"));
	        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
	        BackgroundImage backgroundimage = new BackgroundImage(image, 
	                                             BackgroundRepeat.NO_REPEAT, 
	                                             BackgroundRepeat.NO_REPEAT, 
	                                             BackgroundPosition.CENTER, 
	                                             backgroundSize);
	        Background background = new Background(backgroundimage);
	        root1.setBackground(background);    
	        
	        
	        
	        Scene scene1 = new Scene(root1,1600,900);             
            stage.setScene(scene1);
            stage.setResizable(false);
            stage.show();
        }
  
        catch (Exception e) {
        	e.printStackTrace();
            System.out.println(e.getMessage());
        }
	
	 
} 
	
	public void heroSelect(Stage stage){
		
		GridPane root2 = new GridPane();
		
		Image chartacterselectImage = new Image(getClass().getResourceAsStream("characterselectbackground4.jpg"));
        BackgroundSize charsize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundimage1 = new BackgroundImage(chartacterselectImage, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundPosition.CENTER, 
                                             charsize);
        Background chartacterselectbackground = new Background(backgroundimage1);
        root2.setBackground(chartacterselectbackground);
        
        Font f = Font.font("Consolas", FontWeight.NORMAL, 20);
		
	 	try {
			Game.loadHeroes("Heros.csv");
		} catch (IOException e) {
			errorPopup(e);
		}
	 	
		root2.setAlignment(Pos.CENTER);
        root2.setPadding(new Insets(10));
        root2.setHgap(100);
        root2.setVgap(50);
        
        selectedHero = Game.availableHeroes.get(0);
        
        Label selectedLabel = new Label("Selected Hero: " + selectedHero.getName());
        
        //joel miller
        Image joel = new Image(getClass().getResourceAsStream("joel.png"));
		ImageView b0 = new ImageView(joel);
		b0.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(0);
        	
        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});
		
		Label t0 = new Label(Game.availableHeroes.get(0).getName()+
				"\nClass: " +Game.availableHeroes.get(0).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(0).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(0).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(0).getMaxActions());
        
        //Ellie Williams
        Image ellie = new Image(getClass().getResourceAsStream("ellie.png"));
		ImageView b1 = new ImageView(ellie);
		b1.setOnMouseClicked(event -> {
			selectedHero = Game.availableHeroes.get(1);

			selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

		Label t1 = new Label(Game.availableHeroes.get(1).getName()+
				"\nClass: " +Game.availableHeroes.get(1).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(1).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(1).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(1).getMaxActions());
        
        //Tess
		Image tess = new Image(getClass().getResourceAsStream("tess.png"));
		ImageView b2 = new ImageView(tess);
        b2.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(2);

        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

        Label t2 = new Label(Game.availableHeroes.get(2).getName()+
				"\nClass: " +Game.availableHeroes.get(2).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(2).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(2).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(2).getMaxActions());
        
        //Riley Abel
		Image riley = new Image(getClass().getResourceAsStream("riley.png"));
		ImageView b3 = new ImageView(riley);
        b3.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(3);

        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

        Label t3 = new Label(Game.availableHeroes.get(3).getName()+
				"\nClass: " +Game.availableHeroes.get(3).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(3).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(3).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(3).getMaxActions());
        
        //Tommy Miller
		Image tommy = new Image(getClass().getResourceAsStream("tommy.png"));
		ImageView b4 = new ImageView(tommy);
        b4.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(4);

        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

        Label t4 = new Label(Game.availableHeroes.get(4).getName()+
				"\nClass: " +Game.availableHeroes.get(4).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(4).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(4).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(4).getMaxActions());
        
        //Bill
		Image bill = new Image(getClass().getResourceAsStream("bill.png"));
		ImageView b5 = new ImageView(bill);
        b5.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(5);

        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

        Label t5 = new Label(Game.availableHeroes.get(5).getName()+
				"\nClass: " +Game.availableHeroes.get(5).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(5).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(5).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(5).getMaxActions());
        
        //David
		Image david = new Image(getClass().getResourceAsStream("david.png"));
		ImageView b6 = new ImageView(david);
        b6.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(6);

        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

        Label t6 = new Label(Game.availableHeroes.get(6).getName()+
				"\nClass: " +Game.availableHeroes.get(6).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(6).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(6).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(6).getMaxActions());
        
        //Henry Burell
		Image henry = new Image(getClass().getResourceAsStream("henry.png"));
		ImageView b7 = new ImageView(henry);
        b7.setOnMouseClicked(event -> {
        	selectedHero = Game.availableHeroes.get(7);

        	selectedLabel.setText("Selected Hero; " + selectedHero.getName());
		});

        Label t7 = new Label(Game.availableHeroes.get(7).getName()+
				"\nClass: " +Game.availableHeroes.get(7).getClass().getSimpleName()+
				"\nMax HP: " + Game.availableHeroes.get(7).getMaxHp()+
				"\nAttack Damage: " + Game.availableHeroes.get(7).getAttackDmg()+
				"\nMax AP: " + Game.availableHeroes.get(7).getMaxActions());
        
        //choose a hero
		/*Image chooseHeroImage = new Image(getClass().getResourceAsStream("button2"));
		ImageView chooseHero = new ImageView(chooseHeroImage);
		chooseHero.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        	
			gameScreen(stage);
		});*/
        Button chooseHero = new Button("Start Game");
        chooseHero.setOnAction(event -> {
			Game.startGame(selectedHero);
			gameScreen(stage);
			//instructionScreen(stage);
		});
        /*chooseHero.setFitHeight(80);
        chooseHero.setFitWidth(80);*/
        
        
        selectedLabel.setFont(f);
        selectedLabel.setTextFill(Color.WHITE);
        
        t0.setFont(f);
        t0.setTextFill(Color.WHITE);
        
        t1.setFont(f);
        t1.setTextFill(Color.WHITE);
        
        t2.setFont(f);
        t2.setTextFill(Color.WHITE);
        
        t3.setFont(f);
        t3.setTextFill(Color.WHITE);
        
        t4.setFont(f);
        t4.setTextFill(Color.WHITE);
        
        t5.setFont(f);
        t5.setTextFill(Color.WHITE);
        
        t6.setFont(f);
        t6.setTextFill(Color.WHITE);
        
        t7.setFont(f);
        t7.setTextFill(Color.WHITE);


        root2.add(b0,0,0);
        root2.add(b1,1,0);
        root2.add(b2,2,0);
        root2.add(b3,3,0);

		root2.add(t0,0,1);
		root2.add(t1,1,1);
		root2.add(t2,2,1);
		root2.add(t3,3,1);

        root2.add(b4,0,2);
        root2.add(b5,1,2);
        root2.add(b6,2,2);
        root2.add(b7,3,2);

		root2.add(t4,0,3);
		root2.add(t5,1,3);
		root2.add(t6,2,3);
		root2.add(t7,3,3);

        root2.add(chooseHero,1,4);
        root2.add(selectedLabel, 1 , 5);
        
        Scene scene2 = new Scene(root2,1600,900);
        stage.setScene(scene2);
        stage.setResizable(false);
        stage.show();
		
	}
		
	public void instructionPopup() {

		Stage infoStage = new Stage();
		BorderPane infoRoot = new BorderPane();
		
		Label header = new Label("HOW TO PLAY:");
		header.setFont(Font.font("Impact", FontWeight.BOLD, 20));
		header.setTextFill(Color.WHITE);
		
		Label body = new Label("Move around with arrow keys or by pressing the buttons on the compass" +
				"\nPerform actions by pressing the buttons on the bottom bar" +
				"\nSelect your target by clicking them on the map" +
				"\nSelect your hero by clicking them from the left pane");
		body.setFont(Font.font("Impact", FontWeight.NORMAL, 15));
		body.setTextFill(Color.WHITE);

		infoRoot.setTop(header);
		infoRoot.setCenter(body);

		Image image = new Image(getClass().getResourceAsStream("instructionbackground1.jpg"));
		BackgroundSize backgroundSize = new BackgroundSize(514, 285, true, true, true, false);
		BackgroundImage backgroundimage = new BackgroundImage(image,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				backgroundSize);
		Background background = new Background(backgroundimage);
		infoRoot.setBackground(background);

		Scene infoScene = new Scene(infoRoot,514,285);
		infoStage.setScene(infoScene);
		infoStage.show();
	}
	
	public void gameScreen(Stage stage){
		
		
		introMediaPlayer.stop();
		//Set background
	    Pane root3 = new Pane();        
        Image image = new Image(getClass().getResourceAsStream("acgamebg.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundimage = new BackgroundImage(image, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundPosition.CENTER, 
                                             backgroundSize);
        Background background = new Background(backgroundimage);
        root3.setBackground(background);    
        
        
        //Set Map
		gameMap = new GridPane();
    	
		gameMap.setAlignment(Pos.TOP_CENTER);
        gameMap.setHgap(2);
        gameMap.setVgap(9);
        gameMap.setLayoutX(500);
        
        root3.getChildren().add(gameMap);
        updateMap();
        
        
        //Set Movement Buttons
        Image north = new Image(getClass().getResourceAsStream("north.png"));
		ImageView northButton = new ImageView(north);
        northButton.setOnMouseClicked(event -> {
			move(Direction.UP);
		});
        northButton.setLayoutX(1360);
        northButton.setLayoutY(530);
        northButton.setFitHeight(80);
		northButton.setFitWidth(80);
        
        Image south = new Image(getClass().getResourceAsStream("south.png"));
		ImageView southButton = new ImageView(south);
        southButton.setOnMouseClicked(event -> {
			move(Direction.DOWN);
		});
        southButton.setLayoutX(1370);
        southButton.setLayoutY(820);
        southButton.setFitHeight(80);
        southButton.setFitWidth(80);
        
        Image east = new Image(getClass().getResourceAsStream("east.png"));
		ImageView eastButton = new ImageView(east);
		eastButton.setOnMouseClicked(event -> {
			move(Direction.LEFT);
		});
		eastButton.setLayoutX(1510);
		eastButton.setLayoutY(670);
		eastButton.setFitHeight(80);
		eastButton.setFitWidth(80);
		
        Image west = new Image(getClass().getResourceAsStream("west.png"));
		ImageView westButton = new ImageView(west);
		westButton.setOnMouseClicked(event -> {
			move(Direction.RIGHT);
		});
		westButton.setLayoutX(1220);
		westButton.setLayoutY(670);
		westButton.setFitHeight(80);
		westButton.setFitWidth(80);
		
		root3.getChildren().addAll(northButton,southButton,eastButton,westButton);


        //Set Action Buttons
		Image attack = new Image(getClass().getResourceAsStream("attackbutton.png"));
		ImageView AttackButton = new ImageView(attack);
		AttackButton.setOnMouseClicked(event -> {
			ButtonMedia.play();
			attackButton();
		});
		AttackButton.setLayoutX(400);
		AttackButton.setLayoutY(770);
		AttackButton.setFitHeight(120);
		AttackButton.setFitWidth(120);
		
		Image useSpecial = new Image(getClass().getResourceAsStream("specialbutton.png"));
		ImageView UseSpecialButton = new ImageView(useSpecial);
		UseSpecialButton.setOnMouseClicked(event -> {
			ButtonMedia.play();
			useSpecialButton();
		});
		UseSpecialButton.setLayoutX(600);
		UseSpecialButton.setLayoutY(770); 
		UseSpecialButton.setFitHeight(120);
		UseSpecialButton.setFitWidth(120);
		
		Image Cure = new Image(getClass().getResourceAsStream("vaccinebutton.png"));
		ImageView CureButton = new ImageView(Cure);
		CureButton.setOnMouseClicked(event -> {
			ButtonMedia.play();
			cureButton();
		});
		CureButton.setLayoutX(800);
		CureButton.setLayoutY(770); 
		CureButton.setFitHeight(120);
		CureButton.setFitWidth(120);
		
		Image endturn = new Image(getClass().getResourceAsStream("Endbutton.png"));
		ImageView EndTurnButton = new ImageView(endturn);
		EndTurnButton.setOnMouseClicked(event -> {
			ButtonMedia.play();
			endTurn(stage);
		});
		EndTurnButton.setLayoutX(1000);
		EndTurnButton.setLayoutY(770); 
		EndTurnButton.setFitHeight(120);
		EndTurnButton.setFitWidth(120);
		
		root3.getChildren().addAll(AttackButton,UseSpecialButton,CureButton,EndTurnButton);
    
		//Set Inventories
		selectedHeroInventory = new GridPane();
		selectedHeroInventory.setLayoutX(1270);
		selectedHeroInventory.setLayoutY(40);
		selectedHeroInventory.getColumnConstraints().add(new ColumnConstraints(180));
		selectedHeroInventory.getColumnConstraints().add(new ColumnConstraints(180));
		
		selectedHeroInventory.getRowConstraints().add(new RowConstraints(70));
		selectedHeroInventory.getRowConstraints().add(new RowConstraints(70));
		selectedHeroInventory.getRowConstraints().add(new RowConstraints(70));
		selectedHeroInventory.getRowConstraints().add(new RowConstraints(70));
		selectedHeroInventory.getRowConstraints().add(new RowConstraints(70));

		
		updateInventory();
		
		root3.getChildren().add(selectedHeroInventory);

		//Set Hero Stats
        selectedHeroStats = new GridPane();
		selectedHeroStats.setLayoutX(100);
		selectedHeroStats.setLayoutY(100);

		otherHerosStats = new GridPane();
		otherHerosStats.setLayoutX(75);
		otherHerosStats.setLayoutY(360);
		otherHerosStats.getColumnConstraints().add(new ColumnConstraints(100));
		otherHerosStats.getColumnConstraints().add(new ColumnConstraints(100));
		otherHerosStats.getColumnConstraints().add(new ColumnConstraints(100));

		updateHeroes();
		
		root3.getChildren().addAll(selectedHeroStats,otherHerosStats);

        Scene scene3 = new Scene(root3,1600,900);

		scene3.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.UP){
				move(Direction.UP);
			}
			else if(event.getCode() == KeyCode.DOWN){
				move(Direction.DOWN);
			}
			else if(event.getCode() == KeyCode.LEFT){
				move(Direction.LEFT);
			}
			else if(event.getCode() == KeyCode.RIGHT){
				move(Direction.RIGHT);
			}
			else if(event.getCode() == KeyCode.A){
				attackButton();
			}
			else if(event.getCode() == KeyCode.S){
				useSpecialButton();
			}
			else if(event.getCode() == KeyCode.C){
				cureButton();
			}
			else if(event.getCode() == KeyCode.E){
				endTurn(stage);
			}
				});

        stage.setScene(scene3);
        stage.setResizable(false);
        stage.show();	
        instructionPopup();
	}
	
	
	public void move(Direction dir){
		try {
			
			selectedHero.setActionsAvailable(100);
			
			if(selectedHero.getActionsAvailable()>0) {
			
				int x = selectedHero.getLocation().x;
				int y = selectedHero.getLocation().y;
				
				switch (dir) {
				case DOWN:
					x--;
					break;
				case LEFT:
					y--;
					break;
				case RIGHT:
					y++;
					break;
				case UP:
					x++;
					break;
				}
				
				if(Cell.isTrapCell(Game.map[x][y])){
					errorPopup("You stepped on a trap! You lost " + 
							((TrapCell)Game.map[x][y]).getTrapDamage() + " HP!");
				}
			
			}
			
			selectedHero.move(dir);
			updateMap();
			updateInventory();
			updateHeroes();
		} 
		
		catch (ArrayIndexOutOfBoundsException e) {
			errorPopup("You can only move within map!");
		}
		
		catch (Exception e) {
			errorPopup(e);
		}
		
	}
	
	
	public void attackButton() {
		try{
			selectedHero.attack();
			
			if (!Game.heroes.contains(selectedHero)) {
				selectedHero = Game.heroes.get(0);
			}
			updateMap();
			updateInventory();
			updateHeroes();
		}
		catch(Exception e){
			errorPopup(e);
		}
		
	}
	
	public void useSpecialButton(){
		try{
			selectedHero.useSpecial();
			updateMap();
			updateInventory();
			updateHeroes();
		}
		catch(Exception e){
			errorPopup(e);
		}
	}
	
	public void cureButton(){
		try{
			selectedHero.cure();
			updateHeroes();
			updateMap();
			updateInventory();
			
		}
		catch(Exception e){
			e.printStackTrace();
			errorPopup(e);
		}
	}
	
	public void endTurn(Stage stage){
		try{
			Game.endTurn();
			
			updateMap();
			updateInventory();
			updateHeroes();
			
			if(Game.checkGameOver()){
				if(Game.checkWin()){
					displayWin(stage);
				}
				else{
					displayLose(stage);
				}
				
			}
			
			/*if (!Game.heroes.contains(selectedHero)) {
				selectedHero = Game.heroes.get(0);
			}*/
		}
		catch(Exception e){
			e.printStackTrace();
			errorPopup(e);
		}
	}

	

	public void updateInventory(){
		
		selectedHeroInventory.getChildren().clear();
		int iconSize = 50;
		
		ImageView empty0 = new ImageView(new Image(getClass().getResourceAsStream("empty.png")));
		selectedHeroInventory.add(empty0,0 , 0);
		ImageView empty1 = new ImageView(new Image(getClass().getResourceAsStream("empty.png")));
		selectedHeroInventory.add(empty1,1 , 0);
		
		int i = 0;
		for(Vaccine vaccine : selectedHero.getVaccineInventory()) {
			ImageView vaccineImage = new ImageView(new Image(getClass().getResourceAsStream("vaccine.png")));
			vaccineImage.setFitHeight(iconSize);
			vaccineImage.setFitWidth(iconSize);
			selectedHeroInventory.add(vaccineImage, 0, 1 + i++);
		}
		i = 0;
		for(Supply supply : selectedHero.getSupplyInventory()) {
			ImageView supplyImage = new ImageView(new Image(getClass().getResourceAsStream("supply.png")));
			supplyImage.setFitHeight(iconSize);
			supplyImage.setFitWidth(iconSize);
			selectedHeroInventory.add(supplyImage, 1, 1 + i++);
		}

	}
	
	public void updateMap() {
		
		gameMap.getChildren().clear();
		
	    int cellSize = 35;
	    
	    Image notVisibleImage = new Image(getClass().getResourceAsStream("grey.png"));
	    Image zombieImage = new Image(getClass().getResourceAsStream("zombie.png"));
	    Image emptyImage = new Image(getClass().getResourceAsStream("empty.png"));
	    Image supplyImage = new Image(getClass().getResourceAsStream("supply.png"));
	    Image vaccineImage = new Image(getClass().getResourceAsStream("vaccine.png"));

		for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
            	
                Rectangle cell = new Rectangle(cellSize, cellSize);
                cell.setStroke(Color.TRANSPARENT);
                cell.setStrokeWidth(2);
                Image boxImage = null;
                Cell mapCell = Game.map[col][row];
                
                
                if(mapCell.isVisible()) {
                	if(Cell.isTrapCell(mapCell)) {
                		boxImage = emptyImage;
                	}
                	if (Cell.isCharacterCell(mapCell))
                	{
                		if(((CharacterCell)mapCell).getCharacter() instanceof Zombie ) {
                			boxImage = zombieImage;
                			
                			cell.setOnMouseClicked(event -> {
                            	selectedHero.setTarget(((CharacterCell)mapCell).getCharacter());
                            	updateMap();
                    		});

							if(((CharacterCell)mapCell).getCharacter() == selectedHero.getTarget() ) {
								cell.setStroke(Color.BLUE);
							}
                		}
                		
                		else if(((CharacterCell)mapCell).getCharacter() instanceof Hero ) {
                			
                			boxImage = getHeroImage((Hero)((CharacterCell)mapCell).getCharacter());
                			
                			if(((CharacterCell)mapCell).getCharacter() == selectedHero ) {
                				cell.setStroke(Color.GOLD);
                			}
                			
                			cell.setOnMouseClicked(event -> {
                            	selectedHero.setTarget(((CharacterCell)mapCell).getCharacter());
                            	updateMap();
                    		});

							if(((CharacterCell)mapCell).getCharacter() == selectedHero.getTarget() ) {
								cell.setStroke(Color.BLUE);
							}
                		}
                		
                		else {boxImage = emptyImage;}
                		
                	}
                	
                	if (Cell.isCollectibleCell(mapCell)) {
                		
                		if(((CollectibleCell)mapCell).getCollectible() instanceof Vaccine ) {
                			boxImage = vaccineImage;
                		}
                		
                		if(((CollectibleCell)mapCell).getCollectible() instanceof Supply ) {
                			boxImage = supplyImage;
                		}
                		
                	}
                	
                }
                
                else boxImage = notVisibleImage;

                cell.setFill(new ImagePattern(boxImage));
                gameMap.add(cell,row,15-col);
            }
        }
	}

	public void updateHeroes(){
		
		Font fb = Font.font("Consolas", FontWeight.NORMAL, 15);
		Font fs = Font.font("Consolas", FontWeight.NORMAL, 12);
		
		selectedHeroStats.getChildren().clear();

		ImageView selectedHeroImage = new ImageView(getHeroImage(selectedHero));
		selectedHeroImage.setFitHeight(100);
		selectedHeroImage.setFitWidth(100);

		Label selectedHeroInfo = new Label(selectedHero.getName()+
				"\nClass: " +selectedHero.getClass().getSimpleName()+
				"\nCurrent Health: " + selectedHero.getCurrentHp()+
				"\nAttack Damage: " + selectedHero.getAttackDmg()+
				"\nAction Points: " + selectedHero.getActionsAvailable());
		
		selectedHeroInfo.setFont(fb);
		selectedHeroInfo.setTextFill(Color.LIGHTCYAN);

		selectedHeroStats.add(selectedHeroImage, 0, 0);
		selectedHeroStats.add(selectedHeroInfo, 1, 0);

		otherHerosStats.getChildren().clear();

		int i = 0;
		for(Hero hero : Game.heroes){
		    if(hero != selectedHero){
		        ImageView heroImage = new ImageView(getHeroImage(hero));
		        heroImage.setUserData(hero);
		        heroImage.setFitHeight(50);
		        heroImage.setFitWidth(50);
		        heroImage.setOnMouseClicked(event -> {
		            selectedHero = (Hero) heroImage.getUserData();
		            updateMap();
		            updateInventory();
		            updateHeroes();
		        });
		        Label heroInfo = new Label(hero.getName()+
		            "\n" +hero.getClass().getSimpleName()+
		            "\nHP: " + hero.getCurrentHp()+
		            "\nDmg: " + hero.getAttackDmg()+
		            "\nMax AP: " + hero.getMaxActions());
		        
		        heroInfo.setFont(fs);
		        heroInfo.setTextFill(Color.LIGHTCYAN);
		        
		        if(i < 3) {
		            otherHerosStats.add(heroImage, i, 0);
		            otherHerosStats.add(heroInfo, i++, 1);
		        }
		        else {
		            otherHerosStats.add(heroImage, i - 3 , 2);
		            otherHerosStats.add(heroInfo, i++ - 3, 3);
		        }
		    }
		}

	}

	public Image getHeroImage(Hero h){
		switch(h.getName()){
			case "Joel Miller":
				return new Image(getClass().getResourceAsStream("joel.png"));
			case "Ellie Williams":
				return new Image(getClass().getResourceAsStream("ellie.png"));
			case "Tess":
				return new Image(getClass().getResourceAsStream("tess.png"));
			case "Riley Abel":
				return new Image(getClass().getResourceAsStream("riley.png"));
			case "Tommy Miller":
				return new Image(getClass().getResourceAsStream("tommy.png"));
			case "Bill":
				return new Image(getClass().getResourceAsStream("bill.png"));
			case "David":
				return new Image(getClass().getResourceAsStream("david.png"));
			case "Henry Burell":
				return new Image(getClass().getResourceAsStream("henry.png"));
		}
		return null;
	}
	
	public void displayWin(Stage stage){
		BorderPane win = new BorderPane();
		/*Label l = new Label("You slayed");
		win.getChildren().addAll(l);*/
		Image image = new Image(getClass().getResourceAsStream("win.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundimage = new BackgroundImage(image, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundPosition.CENTER, 
                                             backgroundSize);
        Background background = new Background(backgroundimage);
        win.setBackground(background);    
	 	Scene WinningScene = new Scene(win,1600,900);
	 	stage.setScene(WinningScene);
	}

	public void displayLose(Stage stage){
		BorderPane lose = new BorderPane();
		/*Label l = new Label("You lose weak ass bitch");
		lose.getChildren().addAll(l);
*/		
		Image image = new Image(getClass().getResourceAsStream("gameover.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundimage = new BackgroundImage(image, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundRepeat.NO_REPEAT, 
                                             BackgroundPosition.CENTER, 
                                             backgroundSize);
        Background background = new Background(backgroundimage);
        lose.setBackground(background);    
		
		
	 	Scene LosingScene = new Scene(lose,1600,900);
	 	stage.setScene(LosingScene);
	}

	
	public void errorPopup(Exception e) {
		
		Stage errorStage = new Stage();
		BorderPane errorRoot = new BorderPane();
		Label l = new Label(e.getMessage());
		
		errorRoot.setCenter(l);
		Scene errorScene = new Scene(errorRoot);
		errorStage.setScene(errorScene);
		errorStage.show();
		
		e.printStackTrace();
		
	}

	public void errorPopup(String str) {

		Stage errorStage = new Stage();
		BorderPane errorRoot = new BorderPane();
		Label l = new Label(str);

		errorRoot.setCenter(l);
		Scene errorScene = new Scene(errorRoot);
		errorStage.setScene(errorScene);
		errorStage.show();

	}



	public static void main(String[] args){
		launch(args);
	}
	

}