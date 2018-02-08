package quentin.jeu.mole.desktop;

import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.Shop;
import quentin.jeu.mole.utils.Save;

public class DeskShop implements Shop{
	
	public DeskShop(){
		
		/*// compute your public key and store it in base64EncodedPublicKey
		String test ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzetvZharEasbeLVswOBDz92HL0qxwKqs0DBCeH6QA+Q2KpCzQdX+i64aa0LRMcVOnZBAEnZtkqQ1AqdJUC8m0nitCA2E0UhflJtHJOvFACE+DsEXTaiPruz7mvzoFVEL/djFEYp+wBDL+7rmf3O4qVKnqsK3myF7oEQzEpkayCHdZn0RFFqE8dKn2vwIXdFK34R+5q5m6pLvmKg70yA7ROrWbHac3U9R4b+cWdm8mCqL2qxXH+p7tZqNB9gimGP3Qgrd2pd1Tte8yI+w6Zrk2e86JZmfWIrArbYZFlFGx4LoxFUaJ9QZy7wkCv5tSnn2oCvqHyEE1exTHkU2dxaJMQIDAQAB";
		String base64EncodedPublicKey =  DecrementEachletter(Splash.piece1)
				+ Charactertt.piece2 
				+ Tuto.piece2 
				+ Intro.piece1 
				+ Intro.piece5 
				+ "4LoxFUaJ9QZy7wkCv5tSnn2oCvqHyEE1exTHkU2dxaJMQIDAQAB";
			System.out.println(base64EncodedPublicKey);*/
	}
	
	/*private String DecrementEachletter(String string) {
		String output = null;
		String value = string;
		for(int i=0; i<string.length();i++){
			int charValue = value.charAt(i);
			String next = String.valueOf( (char) (charValue - 1));
			output=output+next;
			
		}
		output= output.substring(4, string.length()+4);
		return output;
	}*/
	
	@Override
	public void buyCoin1() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=10;
		Save.save();
	}

	@Override
	public void buyCoin2() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=50;
		Save.save();
	}
	
	@Override
	public void buyCoin3() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=100;
		Save.save();
		
	}
	
	@Override
	public void buyCoin4() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=150;
		Save.save();
		
	}

	@Override
	public void buyCoin5() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=200;
		Save.save();
		
	}

	@Override
	public void buyCoin6() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=250;
		Save.save();
	}

	@Override
	public void buyCoin7() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=500;
		Save.save();
	}
	
	@Override
	public void buyCoin8() {
		MoleGame.bought=true;
		Save.gd.silvercoins+=1000;
		Save.save();
	}
	
	@Override
	public void checkprice() {
		MoleGame.price1="0,99 €";
		MoleGame.price2="4,99 €";
		MoleGame.price3="9,99 €";
		MoleGame.price4="14,99 €";
		MoleGame.price5="19,99 €";
		MoleGame.price6="20,99 €";
		MoleGame.price7="30,99 €";
		MoleGame.price8="74,99 €";
	}

}
