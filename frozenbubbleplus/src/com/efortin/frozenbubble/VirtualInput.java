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

import android.view.KeyEvent;

/**
 * This class encapsulates variables used to interface all possible
 * virtual player actions.
 * @author Eric Fortin
 */
public abstract class VirtualInput {
  protected boolean mWasCenter = false;
  protected boolean mWasDown   = false;
  protected boolean mWasLeft   = false;
  protected boolean mWasRight  = false;
  protected boolean mWasUp     = false;

  /*
   * The following are abstract methods that must be implemented by
   * descendants.  They must clear the appropriate action flag and
   * return its original value.
   */
  public abstract boolean actionCenter();
  public abstract boolean actionDown();
  public abstract boolean actionLeft();
  public abstract boolean actionRight();
  public abstract boolean actionUp();

  /**
   * Initialize class variables to defaults.
   */
  public final void init_vars() {
    mWasCenter = false;
    mWasDown   = false;
    mWasLeft   = false;
    mWasRight  = false;
    mWasUp     = false;
  }

  /**
   * Process virtual key presses.  This method only sets the
   * historical keypress flags, which must be cleared by ancestors
   * that inherit this class.
   * @param keyCode
   */
  public final void setAction(int keyCode) {
    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
      mWasCenter = true;
    }
    else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
      mWasDown = true;
    }
    else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
      mWasLeft = true;
    }
    else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
      mWasRight = true;
    }
    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
      mWasUp = true;
    }
  }
}
