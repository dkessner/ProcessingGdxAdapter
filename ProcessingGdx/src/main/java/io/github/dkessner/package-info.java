/**
 * This package re-implements the Processing API on top of libgdx.
 *
 * The underlying architecture differs slightly from Processing, but this should be
 * transparent to the user.
 *
 * ```
 * interface PConstants; // same as in Processing
 * 
 * class PImage implements PConstants;
 * 
 * class PGraphics extends PImage;
 * 
 * class PApplet extends PGraphics;
 * ```
 */
package io.github.dkessner;

