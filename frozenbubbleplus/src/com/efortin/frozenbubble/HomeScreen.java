/*
 *                 [[ Frozen-Bubble ]]
 *
 * Copyright (c) 2000-2003 Guillaume Cottenceau.
 * Java sourcecode - Copyright (c) 2003 Glenn Sanson.
 * Additional source - Copyright (c) 2013 Eric Fortin.
 *
 * This code is distributed under the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 or 3, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to:
 * Free Software Foundation, Inc.
 * 675 Mass Ave
 * Cambridge, MA 02139, USA
 *
 * Artwork:
 *    Alexis Younes <73lab at free.fr>
 *      (everything but the bubbles)
 *    Amaury Amblard-Ladurantie <amaury at linuxfr.org>
 *      (the bubbles)
 *
 * Soundtrack:
 *    Matthias Le Bidan <matthias.le_bidan at caramail.com>
 *      (the three musics and all the sound effects)
 *
 * Design & Programming:
 *    Guillaume Cottenceau <guillaume.cottenceau at free.fr>
 *      (design and manage the project, whole Perl sourcecode)
 *
 * Java version:
 *    Glenn Sanson <glenn.sanson at free.fr>
 *      (whole Java sourcecode, including JIGA classes
 *             http://glenn.sanson.free.fr/jiga/)
 *
 * Android port:
 *    Pawel Aleksander Fedorynski <pfedor@fuw.edu.pl>
 *    Eric Fortin <videogameboy76 at yahoo.com>
 *    Copyright (c) Google Inc.
 *
 *          [[ http://glenn.sanson.free.fr/fb/ ]]
 *          [[ http://www.frozen-bubble.org/   ]]
 */

package com.efortin.frozenbubble;

import org.jfedor.frozenbubble.FrozenBubble;
import org.jfedor.frozenbubble.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class HomeScreen extends Activity {
  /*
   * Provide unique IDs for the views associated with the relative
   * layout.  These are used to define relative view layout positions
   * with respect to other views in the layout.
   *
   * These IDs are generated automatically if using an XML layout, but
   * this object implements a RelativeLayout that is constructed purely
   * programmatically.
   */
  private final static int SCREEN_ID = 100;
  private final static int BTN1_ID   = 101;
  private final static int BTN2_ID   = 102;
  private final static int BTN3_ID   = 103;
  private final static int BTN4_ID   = 104;
  private final static int BTN5_ID   = 105;
  private final static int BTN6_ID   = 106;
  private final static int BTN7_ID   = 107;
  private final static int BTN8_ID   = 108;

  private static int buttonSelected = BTN1_ID;
  private static int buttonSelPage1 = BTN1_ID;
  private static int buttonSelPage2 = BTN4_ID;
  private static int buttonSelPage3 = BTN7_ID;

  private Boolean homeShown       = false;
  private Boolean musicOn         = true;
  private ImageView myImageView   = null;
  private RelativeLayout myLayout = null;
  private ModPlayer myModPlayer   = null;
  private Thread splashThread     = null;

  /**
   * Given that we are using a relative layout for the home screen in
   * order to display the background image and various buttons, this
   * function adds the buttons to the layout to provide game options to
   * the player.
   * <p>The buttons are defined in relation to one another so that when
   * using keys to navigate the buttons, the appropriate button will be
   * highlighted.
   */
  private void addHomeButtons() {
    /*
     * Construct the 2 player game button.
     */
    Button start2pGameButton = new Button(this);
    start2pGameButton.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN2_ID;
        buttonSelPage1 = BTN2_ID;
        /*
         * Display the 2 player mode buttons page.
         */
        displayButtonPage(2);
      }
    });
    start2pGameButton.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    start2pGameButton.setText("2 Player");
    start2pGameButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    start2pGameButton.setWidth((int) (start2pGameButton.getTextSize() * 10));
    start2pGameButton.setHorizontalFadingEdgeEnabled(true);
    start2pGameButton.setFadingEdgeLength(5);
    start2pGameButton.setShadowLayer(5, 5, 5, R.color.black);
    start2pGameButton.setId(BTN2_ID);
    start2pGameButton.setFocusable(true);
    start2pGameButton.setFocusableInTouchMode(true);
    LayoutParams myParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams1.addRule(RelativeLayout.CENTER_VERTICAL);
    myParams1.topMargin = 15;
    myParams1.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(start2pGameButton, myParams1);
    /*
     * Construct the 1 player game button.
     */
    Button start1pGameButton = new Button(this);
    start1pGameButton.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN1_ID;
        buttonSelPage1 = BTN1_ID;
        /*
         * Process the button tap and start/resume a 1 player game.
         */
        startFrozenBubble(VirtualInput.PLAYER1, 1, FrozenBubble.LOCALE_LOCAL);
      }
    });
    start1pGameButton.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    start1pGameButton.setText("1 Player");
    start1pGameButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    start1pGameButton.setWidth((int) (start1pGameButton.getTextSize() * 10));
    start1pGameButton.setHorizontalFadingEdgeEnabled(true);
    start1pGameButton.setFadingEdgeLength(5);
    start1pGameButton.setShadowLayer(5, 5, 5, R.color.black);
    start1pGameButton.setId(BTN1_ID);
    start1pGameButton.setFocusable(true);
    start1pGameButton.setFocusableInTouchMode(true);
    LayoutParams myParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams2.addRule(RelativeLayout.ABOVE, start2pGameButton.getId());
    myParams2.topMargin = 15;
    myParams2.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(start1pGameButton, myParams2);
    /*
     * Construct the options button.
     */
    Button optionsButton = new Button(this);
    optionsButton.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN3_ID;
        buttonSelPage1 = BTN3_ID;
        /*
         * Process the button tap and start the preferences activity.
         */
        startPreferencesScreen();
      }
    });
    optionsButton.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    optionsButton.setText("Options");
    optionsButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    optionsButton.setWidth((int) (optionsButton.getTextSize() * 10));
    optionsButton.setHorizontalFadingEdgeEnabled(true);
    optionsButton.setFadingEdgeLength(5);
    optionsButton.setShadowLayer(5, 5, 5, R.color.black);
    optionsButton.setId(BTN3_ID);
    optionsButton.setFocusable(true);
    optionsButton.setFocusableInTouchMode(true);
    LayoutParams myParams3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams3.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams3.addRule(RelativeLayout.BELOW, start2pGameButton.getId());
    myParams3.topMargin = 15;
    myParams3.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(optionsButton, myParams3);
  }

  /**
   * Given that we are using a relative layout for the home screen in
   * order to display the background image and various buttons, this
   * function adds the buttons to the layout to provide multiplayer game
   * options to the player.
   * <p>The buttons are defined in relation to one another so that when
   * using keys to navigate the buttons, the appropriate button will be
   * highlighted.
   */
  private void addMultiplayerButtons() {
    /*
     * Construct the LAN game button.
     */
    Button startLanGameButton = new Button(this);
    startLanGameButton.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN5_ID;
        buttonSelPage2 = BTN5_ID;
        /*
         * Display the player ID buttons page.
         */
        displayButtonPage(3);
      }
    });
    startLanGameButton.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    startLanGameButton.setText("Local Network");
    startLanGameButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    startLanGameButton.setWidth((int) (startLanGameButton.getTextSize() * 10));
    startLanGameButton.setHorizontalFadingEdgeEnabled(true);
    startLanGameButton.setFadingEdgeLength(5);
    startLanGameButton.setShadowLayer(5, 5, 5, R.color.black);
    startLanGameButton.setId(BTN5_ID);
    startLanGameButton.setFocusable(true);
    startLanGameButton.setFocusableInTouchMode(true);
    LayoutParams myParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams1.addRule(RelativeLayout.CENTER_VERTICAL);
    myParams1.topMargin = 15;
    myParams1.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(startLanGameButton, myParams1);
    /*
     * Construct the Player vs. CPU game button.
     */
    Button startCPUGameButton = new Button(this);
    startCPUGameButton.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN4_ID;
        buttonSelPage2 = BTN4_ID;
        /*
         * Process the button tap and start a 2 player game.
         */
        startFrozenBubble(VirtualInput.PLAYER1, 2, FrozenBubble.LOCALE_LOCAL);
      }
    });
    startCPUGameButton.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    startCPUGameButton.setText("Player vs. CPU");
    startCPUGameButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    startCPUGameButton.setWidth((int) (startCPUGameButton.getTextSize() * 10));
    startCPUGameButton.setHorizontalFadingEdgeEnabled(true);
    startCPUGameButton.setFadingEdgeLength(5);
    startCPUGameButton.setShadowLayer(5, 5, 5, R.color.black);
    startCPUGameButton.setId(BTN4_ID);
    startCPUGameButton.setFocusable(true);
    startCPUGameButton.setFocusableInTouchMode(true);
    LayoutParams myParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams2.addRule(RelativeLayout.ABOVE, startLanGameButton.getId());
    myParams2.topMargin = 15;
    myParams2.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(startCPUGameButton, myParams2);
    /*
     * Construct the Internet game button.
     */
    Button startIPGameButton = new Button(this);
    startIPGameButton.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN6_ID;
        buttonSelPage2 = BTN6_ID;
        /*
         * Process the button tap and start an internet game.
         */
        //startFrozenBubble(2, FrozenBubble.LOCALE_INTERNET);
        Toast.makeText(getApplicationContext(),
            "Not available in this version.", Toast.LENGTH_SHORT).show();
      }
    });
    startIPGameButton.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    startIPGameButton.setText("Internet");
    startIPGameButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    startIPGameButton.setWidth((int) (startIPGameButton.getTextSize() * 10));
    startIPGameButton.setHorizontalFadingEdgeEnabled(true);
    startIPGameButton.setFadingEdgeLength(5);
    startIPGameButton.setShadowLayer(5, 5, 5, R.color.black);
    startIPGameButton.setId(BTN6_ID);
    startIPGameButton.setFocusable(true);
    startIPGameButton.setFocusableInTouchMode(true);
    LayoutParams myParams3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams3.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams3.addRule(RelativeLayout.BELOW, startLanGameButton.getId());
    myParams3.topMargin = 15;
    myParams3.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(startIPGameButton, myParams3);
  }

  /**
   * Given that we are using a relative layout for the home screen in
   * order to display the background image and various buttons, this
   * function adds the buttons to the layout to provide player ID
   * selection options to the player.
   * <p>The buttons are defined in relation to one another so that when
   * using keys to navigate the buttons, the appropriate button will be
   * highlighted.
   */
  private void addPlayerSelectButtons() {
    /*
     * Construct the player 2 button.
     */
    Button player2Button = new Button(this);
    player2Button.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN8_ID;
        buttonSelPage3 = BTN8_ID;
        /*
         * Process the button tap and start a 2 player game.
         */
        startFrozenBubble(VirtualInput.PLAYER2, 2, FrozenBubble.LOCALE_LAN);
      }
    });
    player2Button.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    player2Button.setText("Player 2");
    player2Button.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    player2Button.setWidth((int) (player2Button.getTextSize() * 10));
    player2Button.setHorizontalFadingEdgeEnabled(true);
    player2Button.setFadingEdgeLength(5);
    player2Button.setShadowLayer(5, 5, 5, R.color.black);
    player2Button.setId(BTN8_ID);
    player2Button.setFocusable(true);
    player2Button.setFocusableInTouchMode(true);
    LayoutParams myParams1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams1.addRule(RelativeLayout.CENTER_VERTICAL);
    myParams1.topMargin = 15;
    myParams1.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(player2Button, myParams1);
    /*
     * Construct the player 1 button.
     */
    Button player1Button = new Button(this);
    player1Button.setOnClickListener(new Button.OnClickListener(){
      public void onClick(View v){
        buttonSelected = BTN7_ID;
        buttonSelPage3 = BTN7_ID;
        /*
         * Process the button tap and start a 2 player game.
         */
        startFrozenBubble(VirtualInput.PLAYER1, 2, FrozenBubble.LOCALE_LAN);
      }
    });
    player1Button.setOnTouchListener(new Button.OnTouchListener(){
      public boolean onTouch(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          v.requestFocus();
        return false;
      }
    });
    player1Button.setText("Player 1");
    player1Button.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    player1Button.setWidth((int) (player1Button.getTextSize() * 10));
    player1Button.setHorizontalFadingEdgeEnabled(true);
    player1Button.setFadingEdgeLength(5);
    player1Button.setShadowLayer(5, 5, 5, R.color.black);
    player1Button.setId(BTN7_ID);
    player1Button.setFocusable(true);
    player1Button.setFocusableInTouchMode(true);
    LayoutParams myParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                                              LayoutParams.WRAP_CONTENT);
    myParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
    myParams2.addRule(RelativeLayout.ABOVE, player2Button.getId());
    myParams2.topMargin = 15;
    myParams2.bottomMargin = 15;
    /*
     * Add view to layout.
     */
    myLayout.addView(player1Button, myParams2);
  }

  private void cleanUp() {
    if (myModPlayer != null) {
      myModPlayer.destroyMusicPlayer();
      myModPlayer = null;
    }
  }

  /**
   * Manage a set of button "pages", where each page displays buttons.
   * The pages are indexed by a unique identifier.  When a valid page
   * identifier is provided, all buttons corresponding to other pages
   * are removed and the buttons for the requested page ID are added.
   * @param pageID - the requested page identifier (1-based).
   */
  private void displayButtonPage(int pageID) {
    if (pageID == 1) {
      buttonSelected = buttonSelPage1;
      removeViewByID(BTN4_ID);
      removeViewByID(BTN5_ID);
      removeViewByID(BTN6_ID);
      removeViewByID(BTN7_ID);
      removeViewByID(BTN8_ID);
      addHomeButtons();
      selectInitialButton();
    }
    else if (pageID == 2) {
      buttonSelected = buttonSelPage2;
      removeViewByID(BTN1_ID);
      removeViewByID(BTN2_ID);
      removeViewByID(BTN3_ID);
      removeViewByID(BTN7_ID);
      removeViewByID(BTN8_ID);
      addMultiplayerButtons();
      selectInitialButton();
    }
    else if (pageID == 3) {
      buttonSelected = buttonSelPage3;
      removeViewByID(BTN1_ID);
      removeViewByID(BTN2_ID);
      removeViewByID(BTN3_ID);
      removeViewByID(BTN4_ID);
      removeViewByID(BTN5_ID);
      removeViewByID(BTN6_ID);
      addPlayerSelectButtons();
      selectInitialButton();
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      /*
       * When one of the multiplayer game buttons was selected, if the
       * back button was pressed, remove the multiplayer buttons and
       * display the home buttons.  The 2 player button becomes selected
       * by default on the home screen.
       *
       * Otherwise if one of the base level buttons was selected, then
       * terminate the home screen activity.
       */
      if ((buttonSelected == BTN4_ID) ||
          (buttonSelected == BTN5_ID) ||
          (buttonSelected == BTN6_ID)) {
        displayButtonPage(1);
      }
      else if ((buttonSelected == BTN7_ID) ||
               (buttonSelected == BTN8_ID)) {
        displayButtonPage(2);
      }
      else {
        cleanUp();
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onCreate(android.os.Bundle)
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    restoreGamePrefs();
    /*
     * Configure the window presentation and layout.
     */
    setWindowLayout();
    myLayout = new RelativeLayout(this);
    myLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                              LayoutParams.FILL_PARENT));
    myImageView = new ImageView(this);

    if (FrozenBubble.numPlayers != 0)
      startFrozenBubble(FrozenBubble.myPlayerId,
                        FrozenBubble.numPlayers,
                        FrozenBubble.gameLocale);
    else if (getIntent().hasExtra("startHomeScreen")) {
      setBackgroundImage(R.drawable.home_screen);
      setContentView(myLayout);
      startHomeScreen();
    }
    else {
      setBackgroundImage(R.drawable.splash);
      setContentView(myLayout);
      /*
       * Thread for managing the splash screen.
       */
      splashThread = new Thread() {
        @Override
        public void run() {
          try {
            synchronized(this) {
              /*
               * TODO: The splash screen waits before launching the
               * game activity.  Change this so that the game activity
               * is started immediately, and notifies the splash screen
               * activity when it is done loading saved state data and
               * preferences, so the splash screen functions as a
               * distraction from game loading latency.  There is no
               * advantage in doing this right now, because there is no
               * perceivable lag.
               */
              /*
               * Display the splash screen image for 3 seconds.
               */
              wait(3000);
            }
          } catch (InterruptedException e) {
          } finally {
            runOnUiThread(new Runnable() {
              public void run() {
                startHomeScreen();
              }
            });
          }
        }
      };
      splashThread.start();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (myModPlayer != null) {
      myModPlayer.pausePlay();
    }
  }

  @Override
  public void onResume() {
    super.onPause();
    if (myModPlayer != null) {
      restoreGamePrefs();
      if (musicOn)
        myModPlayer.unPausePlay();
    }
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
   * Invoked when the screen is touched.
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      if (splashThread != null) {
        synchronized(splashThread) {
          splashThread.notifyAll();
        }
      }
    }
    return true;
  }

  private void removeViewByID(int id) {
    if (myLayout != null) {
      myLayout.removeView(myLayout.findViewById(id));
    }
  }

  private void restoreGamePrefs() {
    SharedPreferences mConfig = getSharedPreferences(FrozenBubble.PREFS_NAME,
                                                     Context.MODE_PRIVATE);
    musicOn = mConfig.getBoolean("musicOn", true );
  }

  private void selectInitialButton() {
    /*
     * Select the last button that was pressed.
     */
    Button selectedButton = (Button) myLayout.findViewById(buttonSelected);
    selectedButton.requestFocus();
    selectedButton.setSelected(true);
  }

  private void setBackgroundImage(int resId) {
    if (myImageView.getParent() != null)
      myLayout.removeView(myImageView);

    myImageView.setBackgroundColor(getResources().getColor(R.color.black));
    myImageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                                 LayoutParams.FILL_PARENT));
    myImageView.setImageResource(resId);
    myImageView.setId(SCREEN_ID);
    myLayout.addView(myImageView);
  }

  /**
   * Set the window layout according to the game preferences.
   * <p>Requesting that the title bar be removed <b>must</b> be
   * performed before setting the view content by applying the XML
   * layout, or it will generate an exception.
   */
  private void setWindowLayout() {
    final int flagFs   = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    final int flagNoFs = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
    /*
     * Remove the title bar.
     */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    /*
     * Set full screen mode based on the game preferences.
     */
    SharedPreferences mConfig =
      getSharedPreferences(FrozenBubble.PREFS_NAME, Context.MODE_PRIVATE);
    boolean fullscreen = mConfig.getBoolean("fullscreen", true);

    if (fullscreen) {
      getWindow().addFlags(flagFs);
      getWindow().clearFlags(flagNoFs);
    }
    else {
      getWindow().clearFlags(flagFs);
      getWindow().addFlags(flagNoFs);
    }
  }

  /**
   * Start the game with the specified number of players in the
   * specified locale.  A 1 player game can only be played locally.
   * @param myPlayerId - the local player ID.
   * @param numPlayers - the number of players (1 or 2)
   * @param gameLocale - the location of the opponent.  A local opponent
   * will be played by the CPU.  A LAN opponent will be played over the
   * network using multicasting, and an internet opponent will be played
   * using TCP.
   */
  private void startFrozenBubble(int myPlayerId,
                                 int numPlayers,
                                 int gameLocale) {
    /*
     * Since the default game activity creates its own player,
     * destroy the current player.
     */
    cleanUp();
    /*
     * Create an intent to launch the activity to play the game.
     */
    Intent intent = new Intent(this, FrozenBubble.class);
    intent.putExtra("myPlayerId", (int)myPlayerId);
    intent.putExtra("numPlayers", (int)numPlayers);
    intent.putExtra("gameLocale", (int)gameLocale);
    startActivity(intent);
    /*
     * Terminate the splash screen activity.
     */
    finish();
  }

  private void startHomeScreen() {
    if (!homeShown) {
      homeShown = true;
      setBackgroundImage(R.drawable.home_screen);
      if ((buttonSelected == BTN1_ID) ||
          (buttonSelected == BTN2_ID) ||
          (buttonSelected == BTN3_ID))
        addHomeButtons();
      else if ((buttonSelected == BTN4_ID) ||
               (buttonSelected == BTN5_ID) ||
               (buttonSelected == BTN6_ID))
        addMultiplayerButtons();
      else
        addPlayerSelectButtons();
      setContentView(myLayout);
      myLayout.setFocusable(true);
      myLayout.setFocusableInTouchMode(true);
      myLayout.requestFocus();
      /*
       * Highlight the appropriate button to show as selected.
       */
      selectInitialButton();
      /*
       * Create a new music player to play the home screen music.
       */
      myModPlayer = new ModPlayer(this, R.raw.introzik, musicOn, false);
    }
  }

  private void startPreferencesScreen() {
    Intent intent = new Intent(this, PreferencesActivity.class);
    startActivity(intent);
  }
}
