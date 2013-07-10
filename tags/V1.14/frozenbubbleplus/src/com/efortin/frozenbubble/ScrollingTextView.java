/*
 *                 [[ Frozen-Bubble ]]
 *
 * Copyright © 2000-2003 Guillaume Cottenceau.
 * Java sourcecode - Copyright © 2003 Glenn Sanson.
 * Additional source - Copyright © 2013 Eric Fortin.
 *
 * This code is distributed under the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
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
 *    Copyright © Google Inc.
 *
 *          [[ http://glenn.sanson.free.fr/fb/ ]]
 *          [[ http://www.frozen-bubble.org/   ]]
 */

package com.efortin.frozenbubble;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * This is an implementation of a vertically scrolling TextView.
 * 
 * The scroll direction is configurable, as is the number of times to
 * scroll the text.
 */
public class ScrollingTextView extends TextView implements Runnable
{
  private static final float   DEFAULT_SPEED  = 15.0f;
  public  static final boolean SCROLL_DOWN    = true;
  public  static final boolean SCROLL_UP      = false;
  public  static final int     SCROLL_FOREVER = -1;

  private Scroller scroller;

  private int      duration;
  private int      scrollCount;
  private boolean  scrollDirection;
  private boolean  scrollingPaused;
  private float    speed;
  private float    speed_was;
  private boolean  started;
  private int      y_distance;
  private int      y_offset;

  /*
   * Constructors.
   */
  public ScrollingTextView(Context context)
  {
    super(context);
    setup(context);
    init();
  }

  public ScrollingTextView(Context context, AttributeSet attributes)
  {
    super(context, attributes);
    setup(context);
    init();
  }

  /*
   * Private methods.
   */
  private void init()
  {
    scrollCount     = SCROLL_FOREVER;
    scrollDirection = SCROLL_DOWN;
    scrollingPaused = false;
    speed           = DEFAULT_SPEED;
    started         = false;
  }

  private void refreshScroll(boolean stopAnimation)
  {
  	if (stopAnimation)
  	{
  		duration = 0;
	    y_offset = scroller.getCurrY();
  	}
  	else
      duration = (int) (Math.abs(y_distance) * speed);

    scroller.startScroll(0, y_offset, 0, y_distance, duration);
  }

  private void setup(Context context)
  {
    scroller = new Scroller(context, new LinearInterpolator());
    setScroller(scroller);
  }

  private void startScroll()
  {
  	started           = true;
    int viewHeight    = getHeight();
    int visibleHeight = viewHeight - getPaddingBottom() - getPaddingTop();
    int lineHeight    = getLineHeight();

    if (scrollDirection == SCROLL_UP)
    {
      y_offset   = -visibleHeight;
      y_distance = visibleHeight + (getLineCount() * lineHeight);
    }
    else
    {
      y_offset   = getLineCount() * lineHeight;
      y_distance = -(visibleHeight + ((getLineCount() + 1) * lineHeight));
    }

    duration = (int) (Math.abs(y_distance) * speed);
    scroller.startScroll(0, y_offset, 0, y_distance, duration);

    if (scrollCount != 0)
    {
      if(scrollCount > 0)
        scrollCount--;

      post(this);
    }
  }

  /*
   * Protected methods.
   */
  @Override
  protected void onLayout(boolean changed,
                          int left, int top, int right, int bottom)
  {
    super.onLayout(changed, left, top, right, bottom);
    if (!started)
    	startScroll();
  }

  /*
   * Public methods.
   */
  @Override
  public void run()
  {
    if (scroller.isFinished())
    {
      startScroll();
    }
    else
    {
      post(this);
    }
  }

  public void abort()
  {
    scroller.forceFinished(true);
  }

  public float getSpeed()
  {
    return speed;
  }

  public boolean isScrolling()
  {
    return ((scrollCount != 0) || (!scroller.isFinished()));
  }

  public void setPaused(boolean paused)
  {
  	if (started)
  	{
	    if (paused && !scrollingPaused)
	    {
	      scrollingPaused = paused;
	      speed_was = speed;
	      setSpeed(0.0f);
	      refreshScroll(true);
	    }
	    else if (!paused && scrollingPaused)
	    {
	      scrollingPaused = paused;
	      setSpeed(speed_was);
	      refreshScroll(false);
	    }
  	}
  }

  public void setScrollDirection(boolean scrollDirection)
  {
    this.scrollDirection = scrollDirection;
  }

  /**
   * This method sets the additional number of times to scroll the
   * text.  The text will always scroll at least once.
   *
   * If the scroll count is set to -1, then the text will scroll
   * indefinitely.
   * @param  scrollCount  the additional number of times to scroll the
   *                      text.  If this parameter is zero, the text
   *                      will still scroll once.
   */
  public void setScrollRepeatLimit(int scrollCount)
  {
    this.scrollCount = scrollCount;
  }

  public void setSpeed(float speed)
  {
    this.speed = speed;
  }
}