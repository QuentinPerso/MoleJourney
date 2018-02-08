package me.jeu.amolejourney;

import java.util.ArrayList;
import java.util.List;

import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.Shop;
import quentin.jeu.mole.utils.Save;

import com.android.vending.billing.IabHelper;
import com.android.vending.billing.IabResult;
import com.android.vending.billing.Inventory;
import com.android.vending.billing.Purchase;

import android.app.Activity;


public class DroidShop implements Shop{
	
	IabHelper mHelper;
	private Activity context;
	public  String SKU_COIN1  = "coin0";
	public  String SKU_COIN2  = "coin2";
	public  String SKU_COIN3  = "coin3";
	public  String SKU_COIN4  = "coin4";
	public  String SKU_COIN5  = "coin5";
	public  String SKU_COIN6  = "coin6";
	public  String SKU_COIN7  = "coin7";
	public  String SKU_COIN8  = "coin8";
	private String price1, price2, price3, price4,price5, price6, price7,price8;
	
	public DroidShop(Activity context){
		this.context=context;

		//List of IAP
		final List<String> additionalSkuList = new ArrayList<String>();
		additionalSkuList.add(SKU_COIN1);
        additionalSkuList.add(SKU_COIN2);
        additionalSkuList.add(SKU_COIN3);
        additionalSkuList.add(SKU_COIN4);
        additionalSkuList.add(SKU_COIN5);
        additionalSkuList.add(SKU_COIN6);
        additionalSkuList.add(SKU_COIN7);
        additionalSkuList.add(SKU_COIN8);
        
        //Initializing connection
        mHelper = new IabHelper(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkxZojz/P97g0vbIt7mx7emiGgZNRRZVL+tOfaWgSHyRIGbpPTeeoaqD9L1lzNAPpkZKPaWuLU1fwBVA3NEYNhWp0Oe63q168vBYlV4JOY+s1TphYydEq5/xxoXPHl0y8INKPspUK5iLEO0SumkHweuOVHixoVdrXHZ+gJlatZ4VWMcZxAoIjr6cbemGb3b5xdRd0NJ3TwgmXkEFOR/3523rkyBJMaTTRkCrNc4VNW6scWgcUS/ERlcW3CrA6zPz8VFJHtDjpF1/KPzAXTOzcFYJLe/a/gVDytXMDoh+bfwG5/pa5ox5klJTS1IybGaf+dHuZMT0IxWXnlqsaeY89HwIDAQAB");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				 if (!result.isSuccess()) {
			        // Oh noes, there was a problem.
			        System.out.println("problem");
			      }
				// Hooray, IAB is fully set up!
				System.out.println("setupfinished");
			    
				//then check purchase
				mHelper.queryInventoryAsync(true, additionalSkuList,mGotInventoryListener);
			   }
        });
	}

	@Override
	public void buyCoin1() {
		 mHelper.launchPurchaseFlow(context, SKU_COIN1, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
		
	}

	@Override
	public void buyCoin2() {
		 mHelper.launchPurchaseFlow(context, SKU_COIN2, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}
	
	@Override
	public void buyCoin3() {
		mHelper.launchPurchaseFlow(context, SKU_COIN3, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}
	
	@Override
	public void buyCoin4() {
		mHelper.launchPurchaseFlow(context, SKU_COIN4, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}

	@Override
	public void buyCoin5() {
		mHelper.launchPurchaseFlow(context, SKU_COIN5, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}

	@Override
	public void buyCoin6() {
		mHelper.launchPurchaseFlow(context, SKU_COIN6, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}

	@Override
	public void buyCoin7() {
		mHelper.launchPurchaseFlow(context, SKU_COIN7, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}
	
	@Override
	public void buyCoin8() {
		mHelper.launchPurchaseFlow(context, SKU_COIN8, 10001,   
	     		   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	}
	
	@Override
	public void checkprice() {
		MoleGame.price1=price1;
		MoleGame.price2=price2;
		MoleGame.price3=price3;
		MoleGame.price4=price4;
		MoleGame.price5=price5;
		MoleGame.price6=price6;
		MoleGame.price7=price7;
		MoleGame.price8=price8;
	}
	
	// Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
              // System.out.println("Failed to query inventory: " + result);
                return;
            }

           // System.out.println("Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            price1 = inventory.getSkuDetails(SKU_COIN1).getPrice();
            price2 = inventory.getSkuDetails(SKU_COIN2).getPrice();
            price3 = inventory.getSkuDetails(SKU_COIN3).getPrice();
            price4 = inventory.getSkuDetails(SKU_COIN4).getPrice();
            price5 = inventory.getSkuDetails(SKU_COIN5).getPrice();
            price6 = inventory.getSkuDetails(SKU_COIN6).getPrice();
            price7 = inventory.getSkuDetails(SKU_COIN7).getPrice();
            price8 = inventory.getSkuDetails(SKU_COIN8).getPrice();


            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
           /* Purchase MakiPurchase = inventory.getPurchase(SKU_COIN2);
            if (MakiPurchase != null && verifyDeveloperPayload(MakiPurchase)) {
                mHelper.consumeAsync(inventory.getPurchase(SKU_COIN2), mConsumeFinishedListener);
                return;
            }*/
		}
    };
    
    // Listener that's called when we finish purchasing the items 
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
 	= new IabHelper.OnIabPurchaseFinishedListener() {
		 @Override
		 public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			 if (result.isFailure()) {
				 System.out.println("Error purchasing: " + result);
				 return;
			 }    
			 else if (purchase.getSku().equals(SKU_COIN1)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN2)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN3)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN4)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN5)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN6)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN7)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 else if (purchase.getSku().equals(SKU_COIN8)) 
				 mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 
		}
	};
	
	
	// Listener that's called when we finish consuming consumable items 
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
    	@Override
    	public void onConsumeFinished(Purchase purchase, IabResult result) {
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
            	
            if      (purchase.getSku().equals(SKU_COIN1)) {
   				Save.gd.silvercoins+=10;
                Save.save();
                MoleGame.bought=true;
       		}
            else if (purchase.getSku().equals(SKU_COIN2)) {
            	Save.gd.silvercoins+=50;
                Save.save();
                MoleGame.bought=true;
   			 }
   			 else if (purchase.getSku().equals(SKU_COIN3)) {
   				Save.gd.silvercoins+=100;
                Save.save();
                MoleGame.bought=true;
   			 }
   			 else if (purchase.getSku().equals(SKU_COIN4)) {
   				Save.gd.silvercoins+=150;
                Save.save();
                MoleGame.bought=true;
   			 }
   			 else if (purchase.getSku().equals(SKU_COIN5)) {
    			Save.gd.silvercoins+=200;
                Save.save();
                MoleGame.bought=true;
    		}
   			 else if (purchase.getSku().equals(SKU_COIN6)) {
    			Save.gd.silvercoins+=250;
                Save.save();
                MoleGame.bought=true;
    		}
   			 else if (purchase.getSku().equals(SKU_COIN7)) {
    			Save.gd.silvercoins+=500;
                Save.save();
                MoleGame.bought=true;
    		}
   			 else if (purchase.getSku().equals(SKU_COIN8)) {
    			Save.gd.silvercoins+=1000;
                Save.save();
                MoleGame.bought=true;
    		}
            
            
            
            
            
               // alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                //complain("Error while consuming: " + result);
            }
           // updateUi();
          //  setWaitScreen(false);
        }
		
    };
	
	boolean verifyDeveloperPayload(Purchase p) {
	        //String payload = p.getDeveloperPayload();

	       /* 
	         * verify that the developer payload of the purchase is correct. It will be
	         * the same one that you sent when initiating the purchase.
	         *
	         * WARNING: Locally generating a random string when starting a purchase and
	         * verifying it here might seem like a good approach, but this will fail in the
	         * case where the user purchases an item on one device and then uses your app on
	         * a different device, because on the other device you will not have access to the
	         * random string you originally generated.
	         *
	         * So a good developer payload has these characteristics:
	         *
	         * 1. If two different users purchase an item, the payload is different between them,
	         *    so that one user's purchase can't be replayed to another user.
	         *
	         * 2. The payload must be such that you can verify it even when the app wasn't the
	         *    one who initiated the purchase flow (so that items purchased by the user on
	         *    one device work on other devices owned by the user).
	         *
	         * Using your own server to store and verify developer payloads across app
	         * installations is recommended.
	         */

	        return true;
	    }
	
}
