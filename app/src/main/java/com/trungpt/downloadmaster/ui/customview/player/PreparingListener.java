package com.trungpt.downloadmaster.ui.customview.player;

/**
 * Created by Trung on 12/1/2015.
 */
public interface PreparingListener
{
    public void buffering();
    public void ended();
    public void idle();
    public void prepare();
    public void ready();
}
