package com.payrespect.gametuorial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Game extends View{
    int scrw,scrh,playerx=0,playery=0;
    String DirButton1,DirButton2;
    boolean imagesLoaded = false;
    private GameThread T;
    Paint paint = new Paint();
    Bitmap[] main = new Bitmap[12];
    Bitmap IS[][] = new Bitmap[1][4];
    enum Direction{right,left,up,down,stop};
    Direction direction;
    public Game(Context con, AttributeSet attr){
        super(con,attr);
    }

    @Override
    protected void onSizeChanged(int sw, int sh, int esw, int esh){
        super.onSizeChanged(sw,sh,esw,esh);
        this.scrw=sw;
        this.scrh=sh;
        if(T==null){
            T = new GameThread();
            T.start();
        }
    }
    @Override
    protected void onDetachedFromWindow(){
        T.run=false;
        super.onDetachedFromWindow();
    }
    @Override
    protected void onDraw(Canvas canvas){
        if(!imagesLoaded){
            for(int i=0;i<5;i++) {
                main[i]=BitmapFactory.decodeResource(getResources(),R.drawable.main1+i);
                main[i]=Bitmap.createScaledBitmap(main[i],scrw/8,scrh/4,true);

            }
            Bitmap I = BitmapFactory.decodeResource(getResources(),R.drawable.dir);
            I=Bitmap.createScaledBitmap(I,scrw/8,scrh,true);
            for(int i=0;i<4;i++){
                IS[0][i]=Bitmap.createBitmap(I,0,scrh/4*i,scrw/8,scrh/4);
            }
            imagesLoaded=true;
        }
        paint.setColor(0xFF000000);
        paint.setTextSize(scrh/16);
        canvas.drawText("width : "+scrw+", height : "+scrh, 0,scrh/16,paint);
        if(direction==Direction.left){
            canvas.drawBitmap(main[1], playerx,playery,null);
        }
        else if(direction==Direction.right){
            canvas.drawBitmap(main[0], playerx,playery,null);
        }
        else if(direction==Direction.up){
            canvas.drawBitmap(main[2], playerx,playery,null);
        }
        else if(direction==Direction.down){
            canvas.drawBitmap(main[3], playerx,playery,null);
        }else{
            canvas.drawBitmap(main[4], playerx,playery,null);
        }
        canvas.drawBitmap(IS[0][0],scrw/8,scrh-scrh/2,null);
        canvas.drawBitmap(IS[0][1],0,scrh-scrh/4,null);
        canvas.drawBitmap(IS[0][2],scrw/4,scrh-scrh/4,null);
        canvas.drawBitmap(IS[0][3],scrw/8,scrh-scrh/4,null);


    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action==MotionEvent.ACTION_DOWN || action==MotionEvent.ACTION_MOVE||action==MotionEvent.ACTION_POINTER_DOWN){
            float tx =event.getX();
            float ty = event.getY();
            if(ty>=scrh-scrh/4&&ty<scrh&&tx>=0&&tx<scrw/8){
                direction=Direction.left;
            }
            else if(ty>=scrh-scrh/4&&ty<scrh&&tx>=scrw/8&&tx<scrw/4){
                direction=Direction.down;
            }
            else if(ty>=scrh-scrh/4&&ty<scrh&&tx>=scrw/4&&tx<scrw*3/8){
                direction=Direction.right;
            }
            else if(ty>=scrh-scrh/2&&ty<scrh-scrh/4 && tx>=scrw/8&&tx<scrw/4){
                direction=Direction.up;
            }else{
                direction=Direction.stop;
            }
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            direction=Direction.stop;
        }
        return true;
    }
    class GameThread extends Thread{
        boolean run = true;
        public void run() {
            while (run) {
                try {
                    int velocity = 10;
                    postInvalidate();
                    if(direction==Direction.up&&playery-velocity>0){
                        playery-=velocity;
                    }
                    else if(direction==Direction.down&&playery+velocity<scrh){
                        playery+=velocity;
                    }
                    else if(direction==Direction.left&&playerx-velocity>0){
                        playerx-=velocity;
                    }
                    else if(direction==Direction.right&&playerx+velocity<scrw){
                        playerx+=velocity;
                    }
                    //Log.e("player information","now Player is "+playerx+" , "+playery);
                    sleep(10);
                } catch (Exception e) {
                    Log.e("error log", "thread error");
                }
            }
        }
    }
}