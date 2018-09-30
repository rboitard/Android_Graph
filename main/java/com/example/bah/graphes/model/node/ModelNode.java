package com.example.bah.graphes.model.node;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Created by Bah on 04/10/2017.
 */

public class ModelNode {

    private float x;
    private float y;
    private String name;
    private int idNode;
    private RectF rect;
    private int color;

    public ModelNode(float x, float y, String name )
    {
        rect = new RectF(x-25, y-25, x+25, y+25);
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = Color.RED;

    }

    public float getX() {
        return this.x;
    }



    public float getY() { return this.y; }

    public void setCoord(float x, float y) {
        this.x = x;
        this.y = y;
        this.rect.set(x-25, y-25, x+25, y+25);
    }
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public RectF getRect() { return this.rect; }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIdNode() {
        return idNode;
    }

    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }

    @Override
    public String toString() {
        return "ModelNode{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
