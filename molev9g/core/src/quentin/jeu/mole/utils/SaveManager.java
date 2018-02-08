package quentin.jeu.mole.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;

public class SaveManager {
    
	public static GameData gd;
    private Save save = getSave();
    
    public SaveManager(){
        save = getSave();
    }
    
    public static class Save{
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
    
    private FileHandle file = Gdx.files.local("bin/save.json");

    private Save getSave(){
        Save save = new Save();
        if(file.exists()){
	        Json json = new Json();
	        save = json.fromJson(Save.class, Base64Coder.decodeString(file.readString()));
        }
        return save;
    }
    
    public void saveToJson(){
        Json json = new Json();
        json.setOutputType(OutputType.json);
        file.writeString(Base64Coder.encodeString(json.prettyPrint(save)), false);
    }
    
    
  /*  The last step to make this a working save system is to create methods for 
    retrieving and putting data in the Save. We’re going to use a loadDataValue
    (String key, Class type) method for loading a specific value. Basically this
     method returns the value with that specific key as a Class Object. 
     Example: loadDataValue(“name”, String.class) returns the value with key “name”
     as a String object. And for saving data we’re going to use a saveDataValue(String key, Object object)
     method. You give you value a name(key) and your object is your variable/object you want to save.
     Example: saveDataValue(“name”, textFieldName.getText()). 
     And lastly a getAllData method to return the ObjectMap, this is optional.*/
    
    @SuppressWarnings("null")
	public  float loadFloatValue(String key, Class<?> type){
        if(save.data.containsKey(key))
        	return  (Float) save.data.get(key);
        else 
        	return (Float) null;   //this if() avoids and exception, but check for null on load.
    }
    
    @SuppressWarnings("null")
	public  int loadintValue(String key, Class<?> type){
        if(save.data.containsKey(key))
        	return   (Integer) save.data.get(key);
        else 
        	return  (Integer) null;   //this if() avoids and exception, but check for null on load.
    }
    
    @SuppressWarnings("null")
   	public  boolean loadBoolValue(String key, Class<?> type){
           if(save.data.containsKey(key))
           	return    (Boolean) save.data.get(key);
           else 
           	return   (Boolean) null;   //this if() avoids and exception, but check for null on load.
       }
    
    public void saveDataValue(String key, Object object){
        save.data.put(key, object);
        saveToJson(); //Saves current save immediatly.
    }
    public ObjectMap<String, Object> getAllData(){
        return save.data;
    }
}