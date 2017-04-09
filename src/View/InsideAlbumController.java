package View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InsideAlbumController {
	@FXML Button add;
	@FXML Button back;
	@FXML Button logout;
	@FXML Button view;
	@FXML Button quit;
	@FXML TextField captionText;
	@FXML TextField tagname;
	@FXML TextField tagvalue;
	@SuppressWarnings("rawtypes")
	@FXML ChoiceBox choice;
	private UserAlbum userAlbum;
	private String user_name,album_name;
	private static final String filename = "userAlbums.dat";
	private ArrayList<Picture> pics;
	private int option;
	Stage stage;
	Scene scene;
	BorderPane pane;
	@SuppressWarnings("unchecked")
	public void start(String user, String album, BorderPane root, Stage stage1){
		this.user_name = user;
		this.album_name = album;
		option = 0;
		//System.out.println(choice);
		choice.setItems(FXCollections.observableArrayList("Options", "Caption", "View", "Remove", "Move", "Copy", "Add tag", "Remove tag"));
		choice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
			public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, Number value, Number new_value){
				option = new_value.intValue();
			}
		});
		openAlbums();
		pane = root;
		//System.out.println(add);
		//Label l = new Label("wow");
		//l.setPrefSize(20,20);
		stage = (Stage) add.getScene().getWindow();
		ScrollPane scroll = new ScrollPane();
        TilePane tile = new TilePane();
        root.setStyle("-fx-background-color: DAE6F3;");
        //tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(1500);
        tile.setVgap(25);
        //tile.set
        pics = userAlbum.getPics(user_name, album_name);
        if(pics == null)
        	return;
      
        if(user.equals("stock"))
        {
        	 for(int i = 0;i < pics.size();i++){
             	//System.out.println(pics.get(i));
             	ImageView imageView;
             	HBox hbox;
                // imageView = createImageView(new File(pics.get(i)));
             	hbox= createHbox(new File(pics.get(i).getPath()), new Label(pics.get(i).getCaption()));
                 tile.getChildren().addAll(hbox);
             }
        }
        for(int i = 0;i < pics.size();i++){
        	//System.out.println(pics.get(i));
        	ImageView imageView;
        	HBox hbox;
           // imageView = createImageView(new File(pics.get(i)));
        	hbox= createHbox(new File(pics.get(i).getPath()), new Label(pics.get(i).getCaption()));
            tile.getChildren().addAll(hbox);
        }
        
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        scroll.setFitToWidth(true);
        scroll.setContent(tile);
        root.setCenter(scroll);
        //stage1.setScene(null);
       try{
        	scene = new Scene(pane);
        	stage.setScene(scene);
        }catch(Exception e){
        	//do nothing
        }
        //stage.setScene(scene);
	}
	public void add(ActionEvent e){
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().add(extentionFilter);
		fileChooser.getExtensionFilters().add(jpg);
		//Set to user directory or go to default if cannot access
		String userDirectoryString = System.getProperty("user.home");
		//System.out.println("user directory string is: " + userDirectoryString);
		File userDirectory = new File(userDirectoryString);
		if(!userDirectory.canRead()) {
		    userDirectory = new File("c:/");
		}
		fileChooser.setInitialDirectory(userDirectory);
		//Choose the file
		File chosenFile = fileChooser.showOpenDialog(null);
		//Make sure a file was selected, if not return default
		String path;
		
		if(chosenFile != null) {
		    path = chosenFile.getPath();
		   // System.out.println(path);
		    if(userAlbum != null){
		    	userAlbum.addPic(this.user_name, this.album_name, path);
		    }
		} else {
		    //default return value
		    path = null;
		}
		saveAlbums();
		ScrollPane scroll = new ScrollPane();
        TilePane tile = new TilePane();
        pane.setStyle("-fx-background-color: DAE6F3;");
        //tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(1500);
        tile.setVgap(25);
        pics = userAlbum.getPics(user_name, album_name);
        if(pics == null)
        	System.out.println("oh");
        if(pics == null)
        	return;
        for(int i = 0;i < pics.size();i++){
        	//System.out.println(pics.get(i));
        	ImageView imageView;
        	HBox hbox;
           // imageView = createImageView(new File(pics.get(i)));
        	long lastModif = new File(pics.get(i).getPath()).lastModified();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		String modif = sdf.format(lastModif);
    		pics.get(i).setDateAndTime(modif);
    		pics.get(i).calendar(modif);
        	hbox= createHbox(new File(pics.get(i).getPath()), new Label(pics.get(i).getCaption()));
        	//System.out.println(modif);
            tile.getChildren().addAll(hbox);
        }
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        scroll.setFitToWidth(true);
        scroll.setContent(tile);
        pane.setCenter(scroll);
        if(scene != null)
        	scene.setRoot(null);
        try{
        	scene = new Scene(pane);
        	stage.setScene(scene);
        }catch(Exception e1){
        	
        }
	}
	public void back(ActionEvent e)
	{
		try{
			handle(e, "/View/nonadminpage.fxml");
		}catch(IOException e1)
		{
			//do nothing
		}
	}
	public void logout(ActionEvent e)
	{
		try{
			saveAlbums();
			handle(e,"/View/loginpage.fxml");
		}catch(IOException e1)
		{
			//do nothing
		}
	}
	public void quit(ActionEvent e)
	{
			saveAlbums();
			Platform.exit();
			System.exit(0);
	}
	/*public void view(ActionEvent e)
	{
		 ImageView imageView = null;
	        try {
	        	
	            final Image image = new Image(new FileInputStream(imageFile), 105, 0, true,
	                    true);
	            imageView = new ImageView(image);
	            imageView.setFitWidth(105);
	            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

	                @Override
	                public void handle(MouseEvent mouseEvent) {
	                	
	                	if(mouseEvent.getButton().equals(view))
	                	{
	                		try{
	                		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/loginpage.fxml"));
	                		
	                		//System.out.println("loader obtained" + loader);
	                		BorderPane root = (BorderPane)loader.load();
	                		Stage stage = (Stage) back.getScene().getWindow();
	                		Scene scene = new Scene(root);
	                		stage.setScene(scene);
	                		LoginController photo = new LoginController();
	                		photo.start();
	                		stage.show();
	                		}catch(IOException e1)
	                		{
	                			//do nothing
	                		}
	                	}
	                }*/
	/*public void search(ActionEvent e)
	{
		try{
			handle(e, "/View/searchedPhotos.fxml");
		}catch(IOException e1)
		{
			//do nothing
		}
	}*/
	private void handle(ActionEvent e, String path) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		
		//System.out.println("loader obtained" + loader);
		BorderPane root = (BorderPane)loader.load();
		Stage stage = (Stage) back.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		if (path.equals("/View/nonadminpage.fxml"))
		{
			NonAdminController nonadmin = loader.getController();
			nonadmin.start(user_name);
		}
		if (path.equals("/View/loginpage.fxml"))
		{
			LoginController login = loader.getController();
			login.start();
		}
		if (path.equals("/View/insideAlbumPage.fxml"))
		{
			InsideAlbumController ins = loader.getController();
			ins.start(user_name, album_name, root, stage);
		}
		/*if (path.equals("/View/searchedPhotos.fxml"))
		{
			SearchedPhotosController search = loader.getController();
			/*ArrayList<Picture> result = new ArrayList<Picture>();
			for (int i = 0; i< pics.size(); i++)	//traversing through all the pictures
			{
				if (pics.get(i).getTags()!=null && pics.get(i).getTags().containsKey(tagname.getText()))
				{
					HashMap<String,String> pictags = pics.get(i).getTags();
					for (HashMap.Entry<String, String> entry : pictags.entrySet())
					{
						if (entry.getKey().equals(tagname.getText())&&entry.getValue().equals(tagvalue.getText()))
						{
							result.add(pics.get(i));
						}
					}
				}
			}
			search.start(pics);
		}*/
		stage.show();
	}
	private void saveAlbums(){
		try {
			@SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(filename));
			oos.writeObject(userAlbum);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			//problem
		}
		
	}
	private void openAlbums(){
		try{
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(filename));
			userAlbum = (UserAlbum)ois.readObject();
			if(userAlbum == null)
				userAlbum = new UserAlbum();
		}catch(Exception e){
			//no users in system yet aside from Admin
			userAlbum = new UserAlbum();
		}
	}
	private HBox createHbox(final File imageFile, Label l)
	{
		HBox hbox = new HBox();
		//Label l = new Label("search");
		  ImageView imageView = null;
	        try {
	        	
	            final Image image = new Image(new FileInputStream(imageFile), 105, 0, true,
	                    true);
	           // Label l = new Label("search");
	           
	            imageView = new ImageView(image);
	            
	            imageView.setFitWidth(105);
	            hbox.setOnMouseClicked(new EventHandler<MouseEvent>(){
	            //imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

	                @Override
	                public void handle(MouseEvent mouseEvent) {
	                	
	                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
	                    	if(mouseEvent.getClickCount() ==1){
	                    		if (option == 1)	//caption
	                    		{
	                    			ScrollPane scroll = new ScrollPane();
	                    	        TilePane tile = new TilePane();
	                    			//System.out.println(captionText.getText());
	                    			//Picture pic = new Picture(imageFile.getAbsolutePath(), " ");
	                    	   
	                    			for (int i = 0; i<pics.size();i++)
	                    			{
	                    				//System.out.println(imageFile.getAbsolutePath()+" and "+pics.get(i).getPath(user_name,i)+" i is "+i);
	                    				//if(imageFile.getAbsolutePath().contains(pics.get(i).getPath(user_name,i))){
	                    				if (pics.get(i).getPath().equals(imageFile.getAbsolutePath())){
	                    				
	                    					//System.out.println("entered if " + i);
	                    					pics.get(i).setCaption(captionText.getText());;
	                    					break;
	                    				}
	                    			}
	                    			saveAlbums();
	                    			 if(pics == null)
		                    	        	System.out.println("oh");
		                    	        if(pics == null)
		                    	        	return;
		                    	   
		                    	        for(int i = 0;i < pics.size();i++){
		                    	        	//System.out.println(pics.get(i));
		                    	        	//System.out.println(pics.get(i).getCaption());
		                    	        	ImageView imageView;
		                    	        	HBox hbox;
		                    	           // imageView = createImageView(new File(pics.get(i)));
		                    	        	/*if (user_name.equals("stock"))
		                    	        	{
		                    	        		hbox= createHbox(new File(pics.get(i).getPath(user_name,i)), new Label(pics.get(i).getCaption()));
		                    	        	}*/
		                    	        	hbox= createHbox(new File(pics.get(i).getPath()), new Label(pics.get(i).getCaption()));
		                    	        	//System.out.println(hbox);
		                    	        	//System.out.println(pics.get(i).getCaption());
		                    	            tile.getChildren().addAll(hbox);
		                    	        }
		                    	        /*try{
		                    	        scene = new Scene(pane);
	                    	        	stage.setScene(scene);
		                    	        }catch(Exception e1)
		                    	        {
		                    	        	
		                    	        }*/
		                    	      /*  scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
		                    	        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
		                    	        scroll.setFitToWidth(true);
		                    	        scroll.setContent(tile);
		                    	        pane.setCenter(scroll);
		                    	        if(scene !=null)
		                    	        	scene.setRoot(null);
		                    	        try{
		                    	        	scene = new Scene(pane);
		                    	        	stage.setScene(scene);
		                    	        }catch(Exception e){
		                    	        	
		                    	        }*/
	                    			
	                    			//InsideAlbumController ins = new InsideAlbumController();
	                    			//ins.start(user_name, album_name, pane, stage);
	                    			//pic.setCaption(captionText.getText());
	                    		}
	                    		if(option == 2)	
	                    		{	
	                    			//have to go to new screen (photo display screen)
	                    			try{
	                    				FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/photodisplay.fxml"));
	                    				
	                    				//System.out.println("loader obtained" + loader);
	                    				BorderPane root = (BorderPane)loader.load();
	                    				Stage stage = (Stage) back.getScene().getWindow();
	                    				Scene scene = new Scene(root);
	                    				stage.setScene(scene);
	                    				//PhotoDisplayController photo = new PhotoDisplayController();
	                    				PhotoDisplayController photo = loader.getController();
	                    				photo.start(imageFile.getAbsolutePath(),user_name, album_name, root, pics);
	                    				//photo.start();
	                    				stage.show();
	                    			}catch(IOException e1)
	                    			{
	                    				//do nothing
	                    			}
	                    		}
	                    		if(option == 3){ //deleting
	                    			userAlbum.deletePic(user_name, album_name, imageFile.getPath());
	                    			ScrollPane scroll = new ScrollPane();
	                    	        TilePane tile = new TilePane();
	                    	        pane.setStyle("-fx-background-color: DAE6F3;");
	                    	        //tile.setPadding(new Insets(15, 15, 15, 15));
	                    	        tile.setHgap(1500);
	                    	        tile.setVgap(25);
	                    	        pics = userAlbum.getPics(user_name, album_name);
	                    	        if(pics == null)
	                    	        	System.out.println("oh");
	                    	        if(pics == null)
	                    	        	return;
	                    	        for(int i = 0;i < pics.size();i++){
	                    	        	//System.out.println(pics.get(i));
	                    	        	ImageView imageView;
	                    	        	HBox hbox;
	                    	           // imageView = createImageView(new File(pics.get(i)));
	                    	        	hbox= createHbox(new File(pics.get(i).getPath()), new Label(pics.get(i).getCaption()));
	                    	            tile.getChildren().addAll(hbox);
	                    	        }
	                    	        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
	                    	        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
	                    	        scroll.setFitToWidth(true);
	                    	        scroll.setContent(tile);
	                    	        pane.setCenter(scroll);
	                    	        if(scene !=null)
	                    	        	scene.setRoot(null);
	                    	        try{
	                    	        	scene = new Scene(pane);
	                    	        	stage.setScene(scene);
	                    	        }catch(Exception e){
	                    	        	
	                    	        }
	                    	        saveAlbums();
	                    	        //stage.setScene(scene);
	                    			
	                    		}
	                    		if(option == 6){ //Adding tag
	                    			//System.out.println(pics.get(1).getTags().get(0));
	                    			//System.out.println(pics.get(0).getTags().containsKey("name"));
	                    			
	                    			HashMap<String,String> tags = new HashMap<String,String>();
	                    			/*for (HashMap.Entry<String, String> entry : pics.get(0).getTags().entrySet())
	                    			{
	                    			    System.out.println(entry.getKey() + "/" + entry.getValue());
	                    			}*/
	                    			for (int i = 0; i<pics.size();i++)
	                    			{
	                    				//System.out.println(pics.get(i).getTags());
	                    				if (pics.get(i).getPath().equals(imageFile.getAbsolutePath()))
	                    				{
	                    					if (pics.get(i).getTags() == null)
	                    					{
	                    					tags.put(tagname.getText(), tagvalue.getText());
	                    					pics.get(i).setTags(tags);
	                    					}
	                    					else
	                    					{
	                    					//System.out.println("entered");
	                    					pics.get(i).getTags().put(tagname.getText(),tagvalue.getText());
	                    					}
	                    					break;
	                    				}
	                    			}
	                    		}
	                    			if (option == 7)	//Removing tag
	                    			{
	                    				//System.out.println("entering option 7");
	                    				for (int i = 0; i<pics.size();i++)
		                    			{
		                    				//System.out.println(pics.get(i).getTags());
	                    					//System.out.println("Path: " + pics.get(i).getPath());
		                    				if (pics.get(i).getPath().equals(imageFile.getAbsolutePath()))
		                    				{
		                    					//System.out.println("entered delete");
		                    					pics.get(i).getTags().remove(tagname.getText(), tagvalue.getText());
		                    					break;
		                    				}
		                    			}
	                    			}
	                    			saveAlbums();
	                    		}
	                    	}
	                        if(mouseEvent.getClickCount() == 2){
	                            try {
	                                BorderPane borderPane = new BorderPane();
	                                ImageView imageView = new ImageView();
	                                Image image = new Image(new FileInputStream(imageFile));
	                                imageView.setImage(image);
	                                imageView.setStyle("-fx-background-color: BLACK");
	                                imageView.setFitHeight(stage.getHeight() - 10);
	                                imageView.setPreserveRatio(true);
	                                imageView.setSmooth(true);
	                                imageView.setCache(true);
	                                borderPane.setCenter(imageView);
	                                borderPane.setStyle("-fx-background-color: BLACK");
	                                Stage newStage = new Stage();
	                                newStage.setWidth(stage.getWidth());
	                                newStage.setHeight(stage.getHeight());
	                                newStage.setTitle(imageFile.getName());
	                                Scene scene = new Scene(borderPane,Color.BLACK);
	                                newStage.setScene(scene);
	                                newStage.show();
	                            } catch (FileNotFoundException e) {
	                                e.printStackTrace();
	                            }

	                        }
	                    }
	                });
	        } catch (FileNotFoundException ex) {
	            ex.printStackTrace();
	        }
	      //  System.out.println(l.getText());
	        l.setGraphic(imageView);
	        hbox.setSpacing(10);
	        hbox.getChildren().add((l));
	        return hbox;
	}
	private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
        	
            final Image image = new Image(new FileInputStream(imageFile), 105, 0, true,
                    true);
          //  Label l = new Label("search");
           
            imageView = new ImageView(image);
           // l.setGraphic(imageView);
            imageView.setFitWidth(105);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {
                	
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    	if(mouseEvent.getClickCount() ==1){
                    		if(option == 1)	//caption
                    		{
                    			
                    		}
                    		if(option == 2)	//for now this code is for viewing but option 2 is actually for copy
                    		{	
                    			//have to go to new screen (photo display screen)
                    			try{
                    				FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/photodisplay.fxml"));
                    				
                    				//System.out.println("loader obtained" + loader);
                    				BorderPane root = (BorderPane)loader.load();
                    				Stage stage = (Stage) back.getScene().getWindow();
                    				Scene scene = new Scene(root);
                    				stage.setScene(scene);
                    				//PhotoDisplayController photo = new PhotoDisplayController();
                    				PhotoDisplayController photo = loader.getController();
                    				photo.start(imageFile.getAbsolutePath(),user_name, album_name, root,pics);
                    				//photo.start();
                    				stage.show();
                    			}catch(IOException e1)
                    			{
                    				//do nothing
                    			}
                    		}
                    		if(option == 3){ //deleting
                    			userAlbum.deletePic(user_name, album_name, imageFile.getPath());
                    			ScrollPane scroll = new ScrollPane();
                    	        TilePane tile = new TilePane();
                    	        pane.setStyle("-fx-background-color: DAE6F3;");
                    	        //tile.setPadding(new Insets(15, 15, 15, 15));
                    	        tile.setHgap(1500);
                    	        tile.setVgap(25);
                    	        pics = userAlbum.getPics(user_name, album_name);
                    	        if(pics == null)
                    	        	System.out.println("oh");
                    	        if(pics == null)
                    	        	return;
                    	        for(int i = 0;i < pics.size();i++){
                    	        	System.out.println(pics.get(i));
                    	        	ImageView imageView;
                    	            imageView = createImageView(new File(pics.get(i).getPath()));
                    	            tile.getChildren().addAll(imageView);
                    	        }
                    	        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                    	        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                    	        scroll.setFitToWidth(true);
                    	        scroll.setContent(tile);
                    	        pane.setCenter(scroll);
                    	        if(scene !=null)
                    	        	scene.setRoot(null);
                    	        try{
                    	        	scene = new Scene(pane);
                    	        	stage.setScene(scene);
                    	        }catch(Exception e){
                    	        	
                    	        }
                    	        saveAlbums();
                    	        //stage.setScene(scene);
                    			
                    		}
                    	}
                        if(mouseEvent.getClickCount() == 2){
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(stage.getHeight() - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(stage.getWidth());
                                newStage.setHeight(stage.getHeight());
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane,Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }


	
	
}
