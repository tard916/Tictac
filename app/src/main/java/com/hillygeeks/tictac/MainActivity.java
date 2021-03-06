package com.hillygeeks.tictac;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TicTacGame Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageButton btn = (ImageButton) findViewById(R.id.btn_slot5);
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showtoast("Designed Beautifully,Insanely,intelligently By Max Ahirwe(Tmaxletitgo@gmail.com)", 2500);
                return true;

            }
        });

    }


    /**
     * function called everytime a slot on the tic tac board game is clicked
     *
     * @param SlotButton
     */

    public void Slotclick(View SlotButton) {

        //initialize game if new,
        if (Game != null) {
            Log.v("Gamestatus:", "On goin..");
            //get the tag(string) of the clicked button
            String Buttontag = (String) (SlotButton.getTag());
            //convert into an int

            //checks if a win move has already been done and exit the method and show a winning toast
            if (Game.Board.winmove) {
                showtoast(Game.Board.Winmsg + ",Restart Game.", 1000);
                UpdateScoreTxt();
                return;
            } else if (Game.Board.isBoardFull()) {
                showtoast("Maan.. I Hate Draws!,Restart Game.", 1000);

            }


            int slot = Integer.valueOf(Buttontag);
            Log.v("slotclicking", " slot cliked:" + slot);

            //Try making the move if it the player is playing at his turn and the game board is not full
            if (!Game.Board.isBoardFull()) {
                if (Game.Board.FillSlot(slot, 2)) {
                    ImageButton Btn = (ImageButton) SlotButton;
                    Btn.setImageResource(R.drawable.circle);

                    //Trigger the pc to play if the game has not yet been won
                    if (!Game.Board.winmove) {
                        int pcmove = Game.Pcplay();

                        Log.v("Pcmove", "PC attempting move=>" + pcmove);
                        //check if the pc move went throuh and was done
                        if (pcmove != -1) {
                            Game.Board.FillSlot(pcmove, 1);
                            ButtonImageSet(pcmove, R.drawable.close);
                        }
                        //if the Winmove variable changes after the pc played means the game is over the pc won show the toast
                        if (Game.Board.winmove) {
                            showtoast(Game.Board.Winmsg, 1000);
                            UpdateScoreTxt();

                        } else if (Game.Board.isBoardFull()) {
                            showtoast("Maan.. I Hate Draws!,Restart Game.", 1000);
                        }
                        // Game.Board.Lastmoveplayer=1;
                    } else {
                        showtoast(Game.Board.Winmsg, 1000);
                        UpdateScoreTxt();
                    }

                }
            } else {
                showtoast("Hey,Chill..Just start a new game", 500);
                Log.v("Gamestatus:", "Board Full");
            }

        } else {
            Log.v("Gamestatus:", "Not Started.");
            showtoast("Hey,Chill..First Select who plays first from the buttons at the bottom", 1000);
        }

    }


    /**
     * function for dealing with the creation of a new game either its the pc or the user that starts first
     *
     * @param PlayButton
     */
    public void Playclick(View PlayButton) {
        final Context ctx = getApplicationContext();
        //clear out the view
        this.clearlayout();
        ImageButton Button = (ImageButton) PlayButton;
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if the user was playing a game before then print all the slots details
                if (Game != null) {
                    Log.v("Last player:", Game.Board.Lastmoveplayer == 1 ? "Computer" : "Human");
                    Log.v("GameStatus:", "Finishing,Print Slots Data");
                    Game.Board.SlotsDataPrint();
                }

                switch (v.getId()) {
                    case R.id.btn_user: {
                        //if the user choose to start first
                        Log.v("1stPlayer:", "USER");
                        Game = new TicTacGame();
                        clearlayout();
                        showtoast("You start!", 500);
                        break;
                    }

                    case R.id.btn_phone: {
                        //if the user choose the pc to start first
                        Log.v("1stPlayer:", "COMPUTER");
                        Game = new TicTacGame();
                        clearlayout();
                        showtoast("I start!", 500);

                        //play the first move and mark the slot on the GUI;


                        //int btn_slotid = Game.Board.MakeFirstPCRandomMove();
                        int btn_slotid = Game.MakeFirstPCRandomMove();
                        if (btn_slotid != -1) {
                            ButtonImageSet(btn_slotid, R.drawable.close);
                        }
                        break;
                    }
                }

            }
        });

    }


    /**
     * set ButtonImage with passed id to a passed in  Drawable image
     */

    public ImageButton ButtonImageSet(int viewid, int drawable) {

        Log.v("setdrawableid", String.valueOf(viewid));

        String ViewID = "btn_slot" + viewid;
        int id = getResources().getIdentifier(ViewID, "id", this.getPackageName());
        ImageButton Button = (ImageButton) findViewById(id);
        Button.setImageResource(drawable);
        return Button;

    }


    /**
     * show a toast that appears on a precise speed over milliseconds
     */
    public void showtoast(String msg, int Milliseconds) {
        final Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, Milliseconds);
    }


    /**
     * change score
     */
    public void UpdateScoreTxt() {
        TextView score = (TextView) findViewById(R.id.msgtxt);
        String msg = Game.PCplayerwins + "-" + Game.Humanplarerwins;
        score.setText(msg);
    }

    /**
     * clear out all buttons from icons
     */
    public void clearlayout() {
        for (int i = 1; i < 10; i++) {

            ButtonImageSet(i, R.drawable.empty);

        }
    }


}
