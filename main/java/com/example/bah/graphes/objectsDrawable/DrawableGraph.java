package com.example.bah.graphes.objectsDrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.bah.graphes.model.Graph;
import com.example.bah.graphes.model.arc.ModelArc;
import com.example.bah.graphes.model.node.ModelNode;

import java.util.Iterator;

/**
 * Created by Bah on 03/10/2017.
 */

public class DrawableGraph extends Drawable {

    private Paint paintNode;
    private Paint paintArc;
    private Paint paintName;
    private Graph graph;
    //Permet l'affichage d'un arc provisoir lors de la creation
    private Path currentPath;

    public DrawableGraph(Graph graph)
    {

        this.graph = graph;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        //initialisation des paints
        this.paintNode = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paintName = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArc.setStyle(Paint.Style.STROKE);
        paintArc.setColor(Color.BLUE);
        paintNode.setColor(Color.RED);
        paintName.setColor(Color.BLACK);
        paintName.setTextAlign(Paint.Align.CENTER);

        Iterator<ModelNode> itNode = this.graph.getNodeIterator();
        Iterator<ModelArc> itArc = this.graph.getArcIterator();
        ModelNode node;
        ModelArc arc;

        canvas.drawPath(currentPath, paintArc);

        //parcour la list des arcs
        while(itArc.hasNext()){
            arc = itArc.next();
            //affiche l'arc
            canvas.drawPath(arc.getPath(), paintArc);
            float[] midPoint = {0f, 0f};
            float[] tangent = {0f, 0f};
            PathMeasure pm = new PathMeasure(arc.getPath(), false);
            pm.getPosTan(pm.getLength() * 0.50f, midPoint, tangent);
            //affiche le nom de l'arc au milieu
            canvas.drawText(arc.getName(), midPoint[0], midPoint[1], paintName);


        }
        //parcour la list des noeuds
        while(itNode.hasNext()){
            node = itNode.next();
            paintNode.setColor(node.getColor());
            //affiche le noeud
            canvas.drawOval(node.getRect(), paintNode);
            //affiche le nom du noeud
            canvas.drawText(node.getName(), node.getX(), node.getY(), paintName);
        }

    }



    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void setCurrentPath( Path currentPath){
        this.currentPath = currentPath;
    }

}