/*package me.jeu.amolejourney;

import java.util.Arrays;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.widget.GameRequestDialog;

import quentin.jeu.mole.Facebook;

public class Droidbook implements Facebook{

	private Activity context;
	CallbackManager callbackManager;
	GameRequestDialog requestDialog;
	GraphResponse invfriends;
	
	
	public Droidbook(Activity context){
		this.context=context;
		FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult result) {
				
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onError(FacebookException error) {
			}
        });
	}
	
	@Override
	public void signIn() {
		 LoginManager.getInstance().logInWithPublishPermissions(context,
				 Arrays.asList("public_profile", "user_friends"));
		
	}

	@Override
	public void signOut() {
		LoginManager.getInstance().logOut();
		
	}

	@Override
	public void asklife() {
		new GraphRequest(
			    AccessToken.getCurrentAccessToken(),
			    "/me/invitable_friends?fields=name,picture.width(300)s",
			    null,
			    HttpMethod.GET,
			    new GraphRequest.Callback() {
			        public void onCompleted(GraphResponse response) {
			           invfriends=response;
			        }
			    }
			).executeAsync();
		GameRequestContent content = new GameRequestContent.Builder()
        .setMessage("Help me! I cant jump anymore...")
       // .setTo(invfriends.get)
        .build();
		requestDialog.show(content);
		
	}
	
	@Override
	public void givelife() {
		 GameRequestContent content = new GameRequestContent.Builder()
         .setMessage("Here is a life for you")
         .setTo("USER_ID")
         .setActionType(ActionType.SEND)
         .setObjectId("YOUR_OBJECT_ID")
         .build();
		 requestDialog.show(content);
		
	}

	@Override
	public void submitScore(long score) {
		
	}

	@Override
	public void showAchieve() {
		
	}

	@Override
	public void showScores() {
		
	}

	@Override
	public boolean isSignedIn() {
		return false;
	}

}
*/